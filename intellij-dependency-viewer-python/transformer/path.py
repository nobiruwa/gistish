#!/usr/bin/env python3
# -*- coding:utf-8 -*-

import re

src_main_java_re = re.compile(r'.*src/main/java')
suffix_java_re = re.compile(r'\.java$')
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
    # jarのなかにある場合は
    # !以降を取り出して加工する
    if 'jar!' in path:
        [jar, pathInJar] = path.split('!')

        fqdn = _to_fqdn(suffix_java_re.sub('', pathInJar))

        return '{0}!{1}'.format(jar, fqdn)
    else:
        # src/main/javaまで削る
        new_path = suffix_java_re.sub('', src_main_java_re.sub('', path))

        fqdn = _to_fqdn(new_path)

        return fqdn

def path_to_class_name(path):
    return suffix_java_re.sub('', directory_re.sub('', path))
