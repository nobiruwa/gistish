#!/usr/bin/env python3
# -*- coding:utf-8 -*-

def remove_if_to_is_in_jar(dependencies):
    return [
        x for x in dependencies
        if not 'jar!' in x.to_
    ]
