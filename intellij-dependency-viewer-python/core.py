#!/usr/bin/env python3
# -*- coding:utf-8 -*-

class Dependency(object):
    def __init__(self, from_, to_, original=None):
        self._from_ = from_
        self._to_ = to_
        self._original = original

    @property
    def original(self):
        return self._original

    @property
    def from_(self):
        if self._original is None:
            return self._from_
        else:
            return self._original.from_

    @property
    def to_(self):
        if self._original is None:
            return self._to_
        else:
            return self._original.to_

    def __repr__(self):
        return '({0}, {1})'.format(self.from_, self.to_)
