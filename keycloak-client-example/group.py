import csv
import os
import urllib.parse
import urllib.request
import json

import authenticate

def get_group_by_name(
        access_token='',
        base_url='http://localhost:18080',
        base_path='/auth/admin/realms/{0}/groups',
        realm='test',
        groupname='',
):
    url = base_url + base_path.format(realm) + '?search=' + groupname
    headers = {
        'Authorization': 'Bearer {0}'.format(access_token),
    }
    request = urllib.request.Request(url, headers=headers)
    try:
        with urllib.request.urlopen(request) as response:
            print(response.reason)
            json_data = json.loads(response.read())[0]
            return json_data
    except urllib.error.URLError as e:
        print(e.reason)
        print(e.readlines())
        return False

def create_subgroup(
        access_token,
        base_url,
        base_path,
        realm,
        parent_id,
        new_group,
):
    # https://www.keycloak.org/docs-api/21.0.1/rest-api/index.html#_addchild
    # https://www.keycloak.org/docs-api/21.0.1/rest-api/index.html#_grouprepresentation
    url = base_url + base_path.format(realm, parent_id)
    method = 'POST'
    headers = {
        'Authorization': 'Bearer {0}'.format(access_token),
        'Content-Type' : 'application/json',
    }

    json_data = json.dumps(new_group).encode('utf-8')

    request = urllib.request.Request(url, data=json_data, method=method, headers=headers)
    try:
        with urllib.request.urlopen(request) as response:
            new_group_created = get_group_by_name(
                access_token=access_token,
                base_url=base_url,
                realm=realm,
                groupname=new_group['name'],
            )
            print(new_group_created)
            if new_group.get('subGroups'):
                parent_id = new_group_created['id']
                for sub_group in new_group['subGroups']:
                    create_subgroup(
                        access_token,
                        base_url,
                        base_path='/auth/admin/realms/{0}/groups/{1}/children',
                        realm=realm,
                        parent_id=parent_id,
                        new_group=sub_group,
                    )

            return True
    except urllib.error.URLError as e:
        print(e.reason)
        print(e.readlines())
        return False

def create_group(
        access_token='',
        base_url='http://localhost:18080',
        base_path='/auth/admin/realms/{0}/groups',
        realm='test',
        new_group={},
):
    # https://www.keycloak.org/docs-api/21.0.1/rest-api/index.html#_groups_resource
    # https://www.keycloak.org/docs-api/21.0.1/rest-api/index.html#_grouprepresentation
    url = base_url + base_path.format(realm)
    method = 'POST'
    headers = {
        'Authorization': 'Bearer {0}'.format(access_token),
        'Content-Type' : 'application/json',
    }

    json_data = json.dumps(new_group).encode('utf-8')

    request = urllib.request.Request(url, data=json_data, method=method, headers=headers)
    try:
        with urllib.request.urlopen(request) as response:
            new_group_created = get_group_by_name(
                access_token=access_token,
                base_url=base_url,
                realm=realm,
                groupname=new_group['name'],
            )
            print(new_group_created)
            if new_group.get('subGroups'):
                parent_id = new_group_created['id']
                for sub_group in new_group['subGroups']:
                    create_subgroup(
                        access_token,
                        base_url,
                        base_path='/auth/admin/realms/{0}/groups/{1}/children',
                        realm=realm,
                        parent_id=parent_id,
                        new_group=sub_group,
                    )

            return True
    except urllib.error.URLError as e:
        print(e.reason)
        print(e.readlines())
        return False

# def new_group():

def main():
    csv_path='group.csv'

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

    create_group(
        access_token=authorization['access_token'],
        base_url=row['base_url'],
        realm=row['realm'],
        new_group = {
        'name': 'first',
        'subGroups': [{
            'name': 'first-first',
        }, {
            'name': 'first-second',
        }]
    })

if __name__ == '__main__':
    main()
