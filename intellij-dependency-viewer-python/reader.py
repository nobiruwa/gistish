#!/usr/bin/env python3
# -*- coding:utf-8 -*-

from core import Dependency
from xml.etree import ElementTree as ET


class Reader(object):
    def from_file(self, path):
      pass


class IntelliJXmlReader(Reader):
    def from_file(self, path):
        doc = self.__read(path)
        root = doc.getroot()
        dependencies = self.__parse(root)

        return dependencies

    def __read(self, path):
        return ET.parse(path)

    def __parse(self, dom):
        dependencies = []

        for file_element in dom.findall('./file'):
            from_path = file_element.get('path')

            for dependency_element in file_element.findall('./dependency'):
                to_path = dependency_element.get('path')

                dependencies.append(Dependency(from_path, to_path))

        return dependencies
