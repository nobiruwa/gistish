#!/usr/bin/env python3
# -*- coding:utf-8 -*-

import glob
from xml.dom import minidom
from xml.etree.ElementTree import Element, SubElement, tostring
import os.path
import re
import sys

RE_VERSION = re.compile(r'(?<=-)[\.0-9]+(-SNAPSHOT)?')

def find_jars(dir_):
    return glob.glob(os.path.join(dir_, '**', '*.jar'))

def create_pseudo_dependency(jarpath):
    jarfilename = os.path.basename(jarpath)
    jarname = os.path.splitext(jarfilename)[0]
    versionmatch = RE_VERSION.search(jarname)
    version = versionmatch[0] if versionmatch else '0.0.1'
    modulename = jarname.replace('-' + version, '') if version else jarname

    # This is a `jar`
    return {
        'jarpath': jarpath,
        'modulename': modulename,
        'version': version,
    }

def create_pom_dependency(jar):
    root = Element('dependency')

    groupId = SubElement(root, 'groupId')
    groupId.text = 'com.example'

    artifactId = SubElement(root, 'artifactId')
    artifactId.text = jar['modulename']

    version = SubElement(root, 'version')
    version.text = jar['version']

    scope = SubElement(root, 'scope')
    scope.text = 'compile'

    return (
        minidom
        .parseString(
            tostring(root, encoding='UTF-8').decode('UTF-8')
        )
        .toprettyxml(indent = '  ')
        .replace('<?xml version="1.0" ?>\n', '')
    )

def create_pom_dependency_lines(dir_):
    return ''.join(
        [
            create_pom_dependency(create_pseudo_dependency(jarpath))
            for jarpath in find_jars(dir_)
        ]
    )

def main(dir_):
    print(create_pom_dependency_lines(dir_))

if __name__ == '__main__':
    assert len(sys.argv) == 2
    assert os.path.isdir(sys.argv[1])
    main(sys.argv[1])
