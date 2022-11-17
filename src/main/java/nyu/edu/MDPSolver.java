package nyu.edu;

import nyu.edu.dto.Edge;
import nyu.edu.dto.Node;

import java.util.*;

public class MDPSolver {

    private boolean isMin;
    private double tolerance;
    private double iterations;
    private double discountFactor;

    public MDPSolver(boolean isMin, double tol, double iter, double df) {
        this.isMin = isMin;
        this.tolerance = tol;
        this.iterations = iter;
        this.discountFactor = df;
    }

    public void solveMDP(Map<Node, List<Edge>> graph) {
        List<Node> nodes = new ArrayList<>(graph.keySet());
        nodes.sort(Comparator.comparing(Node::getLabel));
        List<Node> policyNodes = computeInitialPolicy(graph, nodes);
        do {
            valueIteration(graph, nodes);
        } while (!updatePolicy(graph, policyNodes));
    }

    private void valueIteration(Map<Node, List<Edge>> graph, List<Node> nodes) {
        for (int i = 1; i <= this.iterations; i++) {
            for (Node node : nodes) {
                computeNodeValue(graph, node);
            }
            if (checkValueTolerance(nodes)) {
                break;
            }
        }
    }

    private void computeNodeValue(Map<Node, List<Edge>> graph, Node node) {
        double val = node.getReward();
        if (node.isDecisionNode()) {
            Node policyNode = node.getPolicyMove();
            val += this.discountFactor * node.getAlpha() * policyNode.getPreviousValue();
            double mulFac = (1.0 - node.getAlpha()) / (graph.get(node).size() <= 1 ? 1.0 : (double)(graph.get(node).size() - 1));
            for (Edge edge : graph.get(node)) {
                if (edge.getNode().equals(policyNode)) {
                    continue;
                }
                val += this.discountFactor * mulFac * edge.getNode().getPreviousValue();
            }
        } else {
            for (Edge edge : graph.get(node)) {
                val += this.discountFactor * edge.getWeight() * edge.getNode().getPreviousValue();
            }
        }
        node.setCurrValue(val);
    }

    private boolean checkValueTolerance(List<Node> nodes) {
        boolean canTerminate = true;
        for (Node node : nodes) {
            canTerminate = canTerminate && Util.abs(Double.compare(node.getCurrValue(), node.getPreviousValue())) < this.tolerance;
            node.setPreviousValue(node.getCurrValue());
        }
        return canTerminate;
    }

    private boolean updatePolicy(Map<Node, List<Edge>> graph, List<Node> policyNodes) {
        boolean canTerminate = true;
        for (Node node : policyNodes) {
            Node policyNode = getPolicyNode(graph, node);
            canTerminate = canTerminate && node.getPolicyMove().equals(policyNode);
            node.setPolicyMove(policyNode);
        }
        return canTerminate;
    }

    private List<Node> computeInitialPolicy(Map<Node, List<Edge>> graph, List<Node> nodes) {
        List<Node> policyNodes = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isDecisionNode()) {
                Node policyNode = getPolicyNode(graph, node);
                node.setPolicyMove(policyNode);
                policyNodes.add(node);
            }
        }
        return policyNodes;
    }

    private Node getPolicyNode(Map<Node, List<Edge>> graph, Node node) {
        Optional<Edge> policyEdge;
        Node policyNode = null;
        if (this.isMin) {
            policyEdge = graph.get(node).stream().min((edge1, edge2) -> (Util.compareDoubleValues(edge1.getNode().getCurrValue(), edge2.getNode().getCurrValue())));
        } else {
            policyEdge = graph.get(node).stream().max((edge1, edge2) -> (Util.compareDoubleValues(edge1.getNode().getCurrValue(), edge2.getNode().getCurrValue())));
        }
        if (policyEdge.isPresent()) {
            policyNode = policyEdge.get().getNode();
        }
        return policyNode;
    }

    public void printSolution(Map<Node, List<Edge>> graph) {
        List<String> assignments = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (Node node : graph.keySet()) {
            if (node.isDecisionNode()) {
                assignments.add(node.getLabel() + " -> " + node.getPolicyMove().getLabel());
            }
            values.add(node.getLabel() + "=" + String.format("%.3f", node.getCurrValue()));
        }
        assignments.sort(String::compareTo);
        values.sort(String::compareTo);
        assignments.forEach(System.out::println);
        System.out.println();
        values.forEach(value -> System.out.print(value + "  "));
        System.out.println();
    }
}
