package nyu.edu;

import nyu.edu.dto.Edge;
import nyu.edu.dto.Node;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Util {

    public static boolean isCycleInGraph(Map<Node, List<Edge>> graph) {
        Set<Node> checked = new HashSet<>();
        for (Node node: graph.keySet()) {
            if (! checked.contains(node)) {
                Set<Node> recStack = new HashSet<>();
                if (isCycleDFS(graph, node, checked, recStack)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isCycleDFS(Map<Node, List<Edge>> graph, Node node, Set<Node> checked, Set<Node> recStack) {
        System.out.println(node.getLabel() + " -> ");
        if (recStack.contains(node)) {
            return true;
        }
        if (checked.contains(node)) {
            return false;
        }
        recStack.add(node);
        checked.add(node);
        for (Edge edge : graph.get(node)) {
            if (isCycleDFS(graph, edge.getNode(), checked, recStack)) {
                return true;
            }
        }
        recStack.remove(node);
        return false;
    }

    // To be used to get the root of CYCLIC graphs only
    public static Node getRootOfGraph(Map<Node, List<Edge>> graph) throws RuntimeException {
        Set<Node> children = new HashSet<>();
        for (Node node : graph.keySet()) {
            children.addAll(graph.get(node).stream().map(Edge::getNode).collect(Collectors.toSet()));
        }
        int c = 0;
        Node root = null;
        for (Node node : graph.keySet()) {
            if (! children.contains(node)) {
                root = node;
                c++;
            }
        }
        if (c < 1) {
            throw new RuntimeException("No root found in the Decision Network. Please check input.");
        }
        if (c > 1) {
            throw new RuntimeException("More than one root found in the Decision Network. Please check input.");
        }
        return root;
    }
}
