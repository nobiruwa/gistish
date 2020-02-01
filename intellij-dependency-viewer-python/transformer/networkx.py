#!/usr/bin/env python3
# -*- coding:utf-8 -*-

import networkx as nx
from networkx.algorithms.dag import descendants
from networkx.algorithms.traversal.depth_first_search import dfs_tree

def to_digraph(dependencies):
    graph = nx.DiGraph()

    for dependency in dependencies:
        graph.add_node(dependency.from_)
        graph.add_node(dependency.to_)

        graph.add_edge(dependency.from_, dependency.to_)

    return graph

def to_graph(dependencies):
    graph = nx.Graph()

    for dependency in dependencies:
        graph.add_node(dependency.from_)
        graph.add_node(dependency.to_)

        graph.add_edge(dependency.from_, dependency.to_)

    return graph

def sub_graph(graph, nodes):
    return graph.subgraph(nodes)

def bfs_tree(graph, root):
    return dfs_tree(graph, root)
