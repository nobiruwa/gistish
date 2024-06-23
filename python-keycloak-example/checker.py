import inspect
import logging

from keycloak import KeycloakAdmin
from keycloak.keycloak_openid import KeycloakOpenID

logging.basicConfig(encoding='utf-8', level=logging.INFO, format='%(asctime)s - %(levelname)s - %(name)s - %(message)s')

class RealmChecker(object):

    def __init__(self, configuration, realm_name, accum_errors=True):
        self._logger = logging.getLogger(type(self).__name__)
        self._configuration = configuration
        self._realm_name = realm_name

        # masterレルムの管理者
        keycloak_admin = KeycloakAdmin(
            server_url=configuration['server_url'],
            username=configuration['admin_username'],
            password=configuration['admin_password'],
        )

        # 操作を開始する前にトークンの取得が必要
        keycloak_admin.connection.get_token()

        keycloak_admin.change_current_realm(realm_name)

        self._keycloak_admin = keycloak_admin

    def _assert(self, condition, error_message):
        parent = inspect.getouterframes(inspect.currentframe())[1]

        try:
            if not condition():
                self._logger.warning(f'{parent.function} - {self._realm_name} - {error_message}')
        except Exception as e:
            self._logger.warning(f'{parent.function} - {self._realm_name} -  KeycloakAdminのエラー: {e}')

    # Clientが存在する場合
    def client_should_exist(self, client_id):
        self._assert(
            lambda: self._keycloak_admin.get_client_id(client_id),
            f'{client_id}が存在しません。'
        )

    # Client Secretを持つ場合
    def client_should_have_client_secret(self, client_id):
        self._assert(
            lambda: self._keycloak_admin.get_client(self._keycloak_admin.get_client_id(client_id)).get('secret'),
            f'{client_id}がclient secretを持っていません。'
        )

    # Service account rolesにrealm-managementクライントのmanage-usersロールを持つ場合
    def client_should_have_client_role(self, client_id, owner_client_id, client_role_name):
        self._assert(
            lambda: self._keycloak_admin.get_client_service_account_user(self._keycloak_admin.get_client_id(client_id))['id'] in [user['id'] for user in self._keycloak_admin.get_client_role_members(self._keycloak_admin.get_client_id(owner_client_id), client_role_name)],
            f'{client_id}が{owner_client_id}クライアントの{client_role_name}を持っていません。',
        )

    # Userが存在する場合
    def user_should_exist(self, user_id):
        self._assert(
            lambda: self._keycloak_admin.get_user_id(user_id),
            f'{user_id}が存在しません。',
        )

    # Attributeを持つ場合
    def user_should_have_attribute(self, user_id, attribute_name, attribute_value):
        self._assert(
            lambda: self._keycloak_admin.get_user(self._keycloak_admin.get_user_id(user_id)).get('attributes'),
            f'{user_id}がattributeを1つも持っていません。',
        )
        self._assert(
            lambda: self._keycloak_admin.get_user(self._keycloak_admin.get_user_id(user_id)).get('attributes').get(attribute_name),
            f'{user_id}が{attribute_name}を持っていません。',
        )
        self._assert(
            lambda: 1 == len(self._keycloak_admin.get_user(self._keycloak_admin.get_user_id(user_id)).get('attributes').get(attribute_name)),
            f'{user_id}がattribute [{attribute_name}]を持っていないか、複数設定されています。',
        )
        self._assert(
            lambda: [attribute_value] == self._keycloak_admin.get_user(self._keycloak_admin.get_user_id(user_id)).get('attributes').get(attribute_name),
            f'{user_id}のattribute [{attribute_name}]に{attribute_value}を設定してください。',
        )

    # rolesにrealm-managementクライントのrealm-adminロールを持つ場合
    def user_should_have_client_role(self, user_id, owner_client_id, client_role_name):
        self._assert(lambda: self._keycloak_admin.get_user(self._keycloak_admin.get_user_id(user_id))['id'] in [user['id'] for user in self._keycloak_admin.get_client_role_members(self._keycloak_admin.get_client_id(owner_client_id), client_role_name)],
            f'{user_id}が{owner_client_id}クライアントの{client_role_name}を持っていません。',
        )

    def client_should_get_users_by_client_secret(self, client_id, client_secret):
        keycloak_admin = KeycloakAdmin(
            server_url=self._configuration['server_url'],
            realm_name=self._realm_name,
            client_id=client_id,
            client_secret_key=client_secret,
        )

        self._assert(
            lambda: keycloak_admin.get_users(),
            f'client [{client_id}] client secret [{client_secret}] でユーザー一覧を取得できませんでした。client secretの値を確認するとともに、realm-managementクライアントのmanage-usersロールを与えてあるかを確認してください。'
        )

    def user_should_get_token_by_password(self, user_id, user_password, client_id):
        keycloak_openid = KeycloakOpenID(
            server_url=self._configuration['server_url'],
            realm_name=self._realm_name,
            client_id=client_id,
        )

        self._assert(
            lambda: keycloak_openid.token(
                user_id,
                user_password
            ),
            f'user [{user_id}], password [{user_password}], client [{client_id}] でパスワード認証しましたがトークンを取得できませんでした。'
        )

def realm_check():
    configuration = {
        'server_url': 'http://localhost:8080/',
        'admin_username': 'admin',
        'admin_password': 'admin',
    }

    realm_checker = RealmChecker(configuration, 'testrealm')

    clients = ['testclient', 'testclient_with_client_secret', 'testclient_with_manage_users']

    for client in clients:
        realm_checker.client_should_exist(client)

    realm_checker.client_should_have_client_secret('testclient_with_client_secret')
    realm_checker.client_should_have_client_role('testclient_with_manage_users', 'realm-management', 'manage-users')

    users = ['testuser', 'testuser_with_attribute', 'testuser_with_realm_admin', 'testuser_with_password']

    for user in users:
        realm_checker.user_should_exist(user)

    realm_checker.user_should_have_attribute('testuser_with_attribute', 'attribute1', 'value1')
    realm_checker.user_should_have_client_role('testuser_with_realm_admin', 'realm-management', 'realm-admin')
    realm_checker.user_should_get_token_by_password('testuser_with_password', 'password', 'testclient')

    realm_checker.client_should_get_users_by_client_secret('testclient_with_client_secret_manage_users', '9UIEmaiJHORXvDQBvZWAY14jmrlBvonY')


def main():
    realm_check()

if __name__ == '__main__':
    main()
