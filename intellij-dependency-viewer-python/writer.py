#!/usr/bin/env python3
# -*- coding:utf-8 -*-

import matplotlib.pyplot as plt
import networkx as nx

try:
    import pygraphviz
    from networkx.drawing.nx_agraph import graphviz_layout
    import networkx.drawing.nx_agraph as drawing
except ImportError:
    try:
        import pydot
        from networkx.drawing.nx_pydot import graphviz_layout
        import networkx.drawing.nx_pydot as drawing
    except ImportError:
        raise ImportError("This example needs Graphviz and either "
                          "PyGraphviz or pydot")

def draw(graph, file_path):
    nx.draw_networkx(graph)
    plt.savefig(file_path)

def draw(graph, file_path):
    nx.draw_networkx(graph)
    plt.savefig(file_path)

def draw_dot(graph, file_path):
    pos = graphviz_layout(graph, prog='dot')

    nx.draw_networkx(graph, pos)
    plt.axis('equal')

    plt.savefig(file_path)

def write_dot(graph, file_path, global_config=None):
    drawing.write_dot(graph, file_path)

    if global_config is not None:
        with open(file_path) as f:
            contents = f.readlines()

        contents.insert(1, global_config + '\n')

        with open(file_path, 'w') as f:
            f.write(''.join(contents))
