#!/usr/bin/env python3
# -*- coding:utf-8 -*-

import matplotlib.pyplot as plt
import networkx as nx

try:
    import pygraphviz
    from networkx.drawing.nx_agraph import graphviz_layout
except ImportError:
    try:
        import pydot
        from networkx.drawing.nx_pydot import graphviz_layout
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
