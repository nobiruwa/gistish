#!/usr/bin/env python3
# -*- coding:utf-8 -*-

import re

# 削除してよいプリフィックス
prefix_re = re.compile(r'.*(src/main/java|src/main/webapp|src/main/resources)')
# 削除してよいサフィックス
suffix_re = re.compile(r'\.java$')
# 削除してよいディレクトリパス
directory_re = re.compile(r'.*/') # 最長一致

def _to_fqdn(path):
    fqdn = '.'.join(
        list(
            filter(lambda x: not '' == x,
                   path.split('/')
            )
        )
    )

    return fqdn

def path_to_fqdn(path):
    # 拡張子別の処理
    def _path_to_fqdn(_path):
        if _path.endswith('.java'):
            return _to_fqdn(suffix_re.sub('', _path))
        else:
            return _path

    # jar, zipのなかにある場合は
    # !以降を取り出して加工する
    if '!' in path:
        [jar, pathInJar] = path.split('!')

        fqdn = _path_to_fqdn(pathInJar)

        return '{0}!{1}'.format(jar, fqdn)
    else:
        # src/main/javaまで削る
        fqdn = _path_to_fqdn(prefix_re.sub('', path))

        return fqdn

def path_to_class_name(path):
    return suffix_re.sub('', directory_re.sub('', path))
