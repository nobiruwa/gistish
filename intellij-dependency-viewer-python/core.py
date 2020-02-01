#!/usr/bin/env python3
# -*- coding:utf-8 -*-

class Dependency(object):
    def __init__(self, from_, to_):
        self.from_ = from_
        self.to_ = to_

    def __repr__(self):
        return '({0}, {1})'.format(self.from_, self.to_)
