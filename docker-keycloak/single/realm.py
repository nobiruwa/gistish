import csv
import os
import urllib.parse
import urllib.request
import json

import authenticate

def create_realm(
        access_token='',
        base_url='http://localhost:18080',
        base_path='/admin/realms',
        new_realm='test',
        realm_template_path='template/realm.json',
):
    url = base_url
    method = 'POST'
    headers = {
        'Authorization': 'Bearer {0}'.format(access_token),
        'Content-Type' : 'application/json',
    }

    with open(realm_template_path) as f:
        json_template_data = f.read()
        json_data = json_template_data.replace('${realm}', new_realm).encode('utf-8')
        print(json.loads(json_data))

    request = urllib.request.Request(url, data=json_data, method=method, headers=headers)
    print(headers)
    try:
        with urllib.request.urlopen(request) as response:
            print(response.reason)
            # json_data = json.loads(response.read())[0]

            # print(json_data)

            return True
    except urllib.error.URLError as e:
        print(e.reason)
        print(e.readlines())
        return False


def main():
    csv_path='realm.csv'

    with open(csv_path, newline='') as f:
        reader = csv.DictReader(
            f,
            fieldnames=[
                'base_url', 'realm', 'realm-management:username', 'realm-management:password', 'realm-management:client-id'
            ],
            delimiter=',')

        next(reader)
        row = next(reader)

        authorization = authenticate.authenticate(
            base_url=row['base_url'],
            realm=row['realm'],
            grant_type='password',
            client_id=row['realm-management:client-id'],
            username=row['realm-management:username'],
            password=row['realm-management:password'],
        )
        print('authorization...', authorization['access_token'])

    create_realm(
        access_token=authorization['access_token'],
        base_url=row['base_url'],
        new_realm='realm1',
        realm_template_path='template/realm.json',
    )

if __name__ == '__main__':
    main()
