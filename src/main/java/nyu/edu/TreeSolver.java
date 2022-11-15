package nyu.edu;

import nyu.edu.dto.Edge;
import nyu.edu.dto.Node;

import java.util.*;

public class TreeSolver {

    private boolean isMin;

    public TreeSolver(boolean isMin) {
        this.isMin = isMin;
    }

    public void solveNetwork(Map<Node, List<Edge>> graph, Node root) {
        Set<Node> solved = new HashSet<>();
        solveDFSUtil(graph, root, solved);
    }

    private void solveDFSUtil(Map<Node, List<Edge>> graph, Node node, Set<Node> solved) {
        solved.add(node);
        for (Edge edge : graph.get(node)) {
            if (! solved.contains(edge.getNode())) {
                solveDFSUtil(graph, edge.getNode(), solved);
            }
        }
        computeNodeValue(graph, node);
    }

    private void computeNodeValue(Map<Node, List<Edge>> graph, Node node) {
        node.setCurrValue(node.getReward());
        if (node.isDecisionNode()) {
            // Decision node = alpha * maxEdge + { (1-alpha) / (count of other edges) * Sum(other edges) }
            Optional<Edge> maxEdgeOpt = Optional.empty();
            if (this.isMin) {
                maxEdgeOpt = graph.get(node).stream().min((edge1, edge2) -> (int) (edge1.getNode().getCurrValue() - edge2.getNode().getCurrValue()));
            } else {
                maxEdgeOpt = graph.get(node).stream().max((edge1, edge2) -> (int) (edge1.getNode().getCurrValue() - edge2.getNode().getCurrValue()));
            }
            if (maxEdgeOpt.isPresent()) {
                Edge maxEdge = maxEdgeOpt.get();
                node.setPolicyMove(maxEdge.getNode());
                node.setCurrValue(node.getCurrValue() + node.getAlpha() * maxEdge.getNode().getCurrValue());
                double mulFac = (1 - node.getAlpha()) / (graph.get(node).size() <= 1 ? 1 : graph.get(node).size() - 1);
                for (Edge edge : graph.get(node)) {
                    if (edge.equals(maxEdge)) {
                        continue;
                    }
                    node.setCurrValue(node.getCurrValue() + mulFac * edge.getNode().getCurrValue());
                }
            }
        } else {
            for (Edge edge : graph.get(node)) {
                node.setCurrValue(node.getCurrValue() + edge.getWeight() * edge.getNode().getCurrValue());
            }
        }
    }

    public void printSolution(Map<Node, List<Edge>> graph, Node root) {
        Set<Node> printed = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        printDFSUtil(graph, root, printed, sb);
        System.out.println();
        System.out.println(sb);
    }

    private void printDFSUtil(Map<Node, List<Edge>> graph, Node node, Set<Node> printed, StringBuilder sb) {
        printed.add(node);
        for (Edge edge : graph.get(node)) {
            if (! printed.contains(edge.getNode())) {
                printDFSUtil(graph, edge.getNode(), printed, sb);
            }
        }
        sb.append(node.getLabel()).append("=").append(node.getCurrValue()).append("  ");
        if (node.isDecisionNode()) {
            System.out.println(node.getLabel() + " -> " + node.getPolicyMove().getLabel());
        }
    }
}
