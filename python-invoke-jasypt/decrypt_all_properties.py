#!/usr/bin/env python
# -*- coding:utf-8 -*-

import csv
import os
import re

import jasypt

RE_ENC = re.compile(r'ENC\(.+\)')

class SecretProperties(object):
    # クラス変数でCSVヘッダを定義する
    csv_header = ['path', 'tenant_id', 'user_name_enc', 'password_enc', 'user_name', 'password' ]

    def __init__(self, jasypt_password, path):
        paths = os.path.split(path)

        ## パス部
        self.path = path
        # pathの末尾はファイル名
        self.file_name = paths[-1]
        # ファイルの親ディレクトリはテナントID
        self.tenant_id = os.path.split(paths[-2])[-1]

        ## ファイルの内容
        with open(path, encoding='utf-8') as f:
            content = f.read()
            groups = RE_ENC.findall(content)

            assert len(groups) == 2, f'{self.path}: ENC()は2つちょうどあるはずです'
            # 1個目のマッチはユーザー名
            self.user_name_enc = groups[0]
            self.user_name = jasypt.decrypt_enc(jasypt_password, self.user_name_enc)
            # 2個目のマッチはパスワード
            self.password_enc = groups[1]
            self.password = jasypt.decrypt_enc(jasypt_password, self.password_enc)

    def write_csv(self, csv_dict_writer):
        # ヘッダに合わせてメンバーを取得する
        row = []
        variables = vars(self)
        var_dict = {}
        for column in SecretProperties.csv_header:
            assert variables[column] is not None, f'{self.path}: {column}がNoneです'
            var_dict[column] = variables[column]

        csv_dict_writer.writerow(var_dict)

RE_TENANT_ID_LIKE = re.compile(r'[a-z]{3}[a-z0-9]{8}_[0-9]{4}')

def find_file(directory_path, jasypt_password):
    # テナントIDを名前に持つディレクトリのみリスト
    tenant_ids = [x for x in os.listdir(directory_path) if RE_TENANT_ID_LIKE.match(x)]
    # {テナントID}/secret.propertiesをリスト(存在チェックはしない)
    secret_properties_paths = [os.path.join(tenant_id, 'secret.properties') for tenant_id in tenant_ids]
    # SecretPropertiesに変換して返却
    return [
        SecretProperties(jasypt_password=jasypt_password, path=os.path.join(directory_path, secret_properties_path))
        for secret_properties_path
        in secret_properties_paths
    ]


def main(directory_path, jasypt_password):
    secret_properties_list = find_file(directory_path, jasypt_password)

    # CSVに変換
    with open('secret_properties.csv', mode='w', encoding='cp932', newline='') as f:
        csv_writer = csv.DictWriter(f, SecretProperties.csv_header)

        for secret_properties in secret_properties_list:
            secret_properties.write_csv(csv_writer)

if __name__ == '__main__':
    import sys
    main(sys.argv[1], sys.argv[2])
