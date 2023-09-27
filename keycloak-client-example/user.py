import csv
import os
import urllib.parse
import urllib.request
import json

import authenticate

def load_csv(
        csv_path='user.csv',
):
    with open(csv_path, newline='') as f:
        reader = csv.DictReader(
            f,
            fieldnames=[
                'base_url', 'realm', 'realm-management:username', 'realm-management:password', 'realm-management:client-id', 'username', 'password', 'first_name', 'last_name', 'email', 'groups', 'attributes:birthday', 'attributes:deleted', 'attributes:type', 'attributes:notice'
            ],
            delimiter=',')

        next(reader)

        for row in reader:
            authorization = authenticate.authenticate(
                base_url=row['base_url'],
                realm=row['realm'],
                grant_type='password',
                client_id=row['realm-management:client-id'],
                username=row['realm-management:username'],
                password=row['realm-management:password'],
            )
            print('authorization...', authorization['access_token'])

            new_user = user_object(
                groups=row['groups'].split(':'),
                attributes={
                    'birthday': row['attributes:birthday'],
                    'deleted': row['attributes:deleted'],
                    'type': row['attributes:type'],
                    'notice': row['attributes:notice'],
                },
            )
            print(new_user)
            new_user_created = create_user(
                access_token=authorization['access_token'],
                base_url=row['base_url'],
                realm=row['realm'],
                new_user=new_user,
            )
            print(new_user_created)

def create_user(
        access_token='',
        base_url='http://localhost:18080',
        base_path='/auth/admin/realms/{0}/users',
        realm='test',
        new_user={},
):
    # https://www.keycloak.org/docs-api/21.0.1/rest-api/index.html#_users_resource
    # https://www.keycloak.org/docs-api/21.0.1/rest-api/index.html#_userrepresentation
    url = base_url + base_path.format(realm)
    method = 'POST'
    headers = {
        'Authorization': 'Bearer {0}'.format(access_token),
        'Content-Type' : 'application/json',
    }

    json_data = json.dumps(new_user).encode('utf-8')

    request = urllib.request.Request(url, data=json_data, method=method, headers=headers)
    try:
        with urllib.request.urlopen(request) as response:
            print(response.reason)
            return True
    except urllib.error.URLError as e:
        print(e.reason)
        print(e.readlines())
        return False

def user_object(
        username='testuser01',
        password='password',
        first_name='John',
        last_name='Do',
        email='john-do@example.com',
        groups=[],
        attributes={},
):
    return {
        'username': username,
        'firstName': first_name,
        'lastName': last_name,
        'email': email,
        'emailVerified': False,
        'enabled': True,
        'groups': groups,
        'attributes': attributes,
        'credentials': [{
            'type':'password',
            'value': password,
            'temporary': False,
        }],
    }

def main():
    load_csv()

if __name__ == '__main__':
    main()
