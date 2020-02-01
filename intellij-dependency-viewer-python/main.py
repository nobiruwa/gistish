#!/usr/bin/env python3
# -*- coding:utf-8 -*-

import core
import filter_
import reader
import transformer.path
import transformer.networkx
import writer

import sys

def main(input_path, root, output_path):
    r = reader.IntelliJXmlReader()
    dependencies = r.from_file(input_path)

    transformed = [
        core.Dependency(
            # transformer.path.path_to_fqdn(x.from_),
            # transformer.path.path_to_fqdn(x.to_)
            transformer.path.path_to_class_name(x.from_),
            transformer.path.path_to_class_name(x.to_)
        )
        for x in filter_.remove_if_to_is_in_jar(dependencies)
    ]

    graph = transformer.networkx.to_digraph(transformed)
    descendants = transformer.networkx.descendants(graph, root)
    nodes = [root] + list(descendants)
    subgraph = transformer.networkx.sub_graph(graph, nodes)

    writer.draw_dot(subgraph, output_path)

if __name__ == '__main__':
    # main('samples/two_tree.xml', 'Root2', 'output/two_tree.png')
    main(sys.argv[1], sys.argv[2], sys.argv[3])
