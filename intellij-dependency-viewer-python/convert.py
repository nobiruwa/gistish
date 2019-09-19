#!/usr/bin/env python3
# -*- coding:utf-8 -*-

import argparse

import re
import xml.etree.ElementTree as ET

TEMPLATE = """\
library(igraph)

g <- graph(edges=c(
{0}
))

# http://alstd.hatenablog.com/entry/2014/12/16/234349
plot(g,
 vertex.size=15, #ノードの大きさ
 vertex.shape="rectangle", #ノードの形
 vertex.label=V(g)$name, #ノード属性nameをノードラベルにする。
 vertex.color=ifelse(V(g)$Faction==1,"Pink","Lightgreen"), #ノード属性Factionを用いてノードに色づけ
 vertex.label.color="gray50", #ノードのラベルの色
 vertex.label.font=2, #ノードのラベルのスタイル 1: 普通, 2: 太字, 3: 斜体, 4: 太字斜体, 5: ギリシャ文字
 vertex.frame.color="white", #ノードの枠の色
 vertex.label.cex=0.8, #ノードラベルの文字サイズ
 edge.width=E(g)$weight, #エッジ属性weightをエッジの太さとする
 edge.color="gray80", #エッジの色
 layout=layout.fruchterman.reingold) #ネットワークのレイアウト手法
"""

def parse_direction(root):
    # R言語のigraphの仕様に合わせて
    # [from0, to0, from1, to1, ...]という配列を作る
    result = []

    for file_ in root.findall('./file'):
        from_ = file_.attrib['path']

        for dependency in file_.findall('./dependency'):
            to = dependency.attrib['path']

            result.append((from_, to))

    return result

def suppress_jar(path: str):
    if path.find('!') >= 0:
        m = re.search(r'^.*\/([^\/!]+)!.*$', path)
        return m.group(1)
    else:
        return path

def suppress_jars(parsed):
    return [(x, suppress_jar(y)) for (x, y) in parsed]

def flatten(tuple_list):
    result = []

    for (x, y) in tuple_list:
        result.append(x)
        result.append(y)

    return result

def remove_duplicate(tuple_list):
    return list(set(tuple_list))

def write(node, indent=2):
    return '{0}"{1}"'.format(' ' * indent, node)

def write_all(parsed):
    return TEMPLATE.format(',\n'.join([
            write(x) for x in parsed
        ])
    )

def parse_argument():
    parser = argparse.ArgumentParser(description='Process some integers.')
    parser.add_argument('input', metavar='IN', type=str,
                        help='Intelli J IDEAの依存関係XMLフォーマット')
    parser.add_argument('output', metavar='OUT', type=str,
                        help='an integer for the accumulator')

    args = parser.parse_args()

    return args


def main():
    args = parse_argument()
    with open(args.input) as inp:
        root = ET.fromstring(inp.read())
        with open(args.output, 'w') as out:
            out.write(
                write_all(
                    flatten(
                        remove_duplicate(
                            suppress_jars(
                                parse_direction(root))))))

if __name__ == '__main__':
    main()
