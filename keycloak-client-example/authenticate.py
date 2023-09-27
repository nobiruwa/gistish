import csv
import os
import urllib.parse
import urllib.request
import json

def authenticate(
        base_url='http://localhost:18080',
        base_path='/auth/realms/{0}/protocol/openid-connect/token',
        realm='test',
        grant_type='password',
        client_id='admin-cli',
        username='realm-master',
        password='realm-master',
):
    url = base_url + base_path.format(realm)
    # Content-Type: application/x-www-form-urlencoded
    data = urllib.parse.urlencode({
        'grant_type': grant_type,
        'username': username,
        'password': password,
        'client_id': client_id,
    })
    data = data.encode('ascii')
    req = urllib.request.Request(url, data)

    with urllib.request.urlopen(req) as response:
        return json.loads(response.read())
