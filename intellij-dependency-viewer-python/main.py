#!/usr/bin/env python3
# -*- coding:utf-8 -*-

import core
import filter_
import reader
import transformer.path
import transformer.networkx
import writer

import sys


def create_dependency(x, reverse):
    if reverse:
        return core.Dependency(
            transformer.path.path_to_fqdn(x.to_),
            transformer.path.path_to_fqdn(x.from_),
        )
    else:
        return core.Dependency(
            transformer.path.path_to_fqdn(x.from_),
            transformer.path.path_to_fqdn(x.to_)
        )

def main(input_path, output_path, root=None, reverse=False):
    r = reader.IntelliJXmlReader()
    dependencies = r.from_file(input_path)

    transformed = [
        create_dependency(x, reverse)
        for x in filter_.remove_if_to_is_in_jar(dependencies)
    ]

    graph = transformer.networkx.to_digraph(transformed, transformer.path.path_to_fqdn)

    if root is None:
        writer.write_dot(graph, output_path, 'graph [rankdir=LR]')
    else:
        descendants = transformer.networkx.descendants(graph, root)
        nodes = [root] + list(descendants)
        subgraph = transformer.networkx.sub_graph(graph, nodes)

        writer.write_dot(subgraph, output_path, 'graph [rankdir=LR]')


if __name__ == '__main__':
    # 要素の削除を行うためのコピー配列を作成する
    args = sys.argv.copy()

    # 簡易な引数のパースを行う
    if '--reverse' in args:
        reverse = True
        args.remove('--reverse')
    else:
        reverse = False

    # 残った引数の数で動作を分岐する
    if len(args) >= 4:
        # python3 main.py samples/two_tree.xml /tmp/two_tree.dot org.example.layer1.Root2
        # python3 main.py samples/two_tree.xml /tmp/two_tree_reverse.dot org.example.layer1.Root2 --reverse
        main(args[1], args[2], root=args[3], reverse=reverse)
    else:
        # python3 main.py samples/two_tree.xml /tmp/two_tree.dot
        # python3 main.py samples/two_tree.xml /tmp/two_tree_reverse.dot --reverse
        main(args[1], args[2], root=None, reverse=reverse)
