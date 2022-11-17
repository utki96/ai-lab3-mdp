package nyu.edu;

import nyu.edu.dto.Constants;
import nyu.edu.dto.Edge;
import nyu.edu.dto.Node;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class InputParser {

    public static Map<Node, List<Edge>> parseInput(String filePath) {
        List<String> instructions = readInputFile(filePath);
        Map<Node, List<Edge>> graph = createGraph(instructions);
        postProcessGraph(graph);
        return graph;
    }

    private static void postProcessGraph(Map<Node, List<Edge>> graph) {
        for (Node node : graph.keySet()) {
            List<Edge> edges = graph.get(node);
            boolean isProbMissing = false;
            for (Edge edge : edges) {
                if (edge.getWeight() == 0.0) {
                    isProbMissing = true;
                    break;
                }
            }
            // For node having a single edge or missing probability values for edges
            if (isProbMissing) {
                if (edges.size() == 1) {
                    edges.get(0).setWeight(Constants.DEFAULT_WEIGHT);
                } else if (node.getAlpha() == 0.0) {
                    node.setDecisionNode(true);
                    node.setAlpha(1.0);
                }
            }
        }
    }

    private static Map<Node, List<Edge>> createGraph(List<String> instructions) throws RuntimeException {
        Map<Node, List<Edge>> graph = new HashMap<>();
        Map<String, Node> nodeMap = new HashMap<>();

        for (String instruction : instructions) {
            if (instruction.contains(Constants.REWARD_DELIM)) {
                handleRewardInst(graph, nodeMap, instruction);
            } else if (instruction.contains(Constants.EDGE_DELIM)) {
                handleEdgesInst(graph, nodeMap, instruction);
            } else if (instruction.contains(Constants.WEIGHT_DELIM)) {
                handleWeightInst(graph, nodeMap, instruction);
            } else {
                throw new RuntimeException("Failed to parse input. Invalid input line: " + instruction);
            }
        }

        return graph;
    }

    private static void handleRewardInst(Map<Node, List<Edge>> graph, Map<String, Node> nodeMap, String instruction) {
        String label = instruction.substring(0, instruction.indexOf(Constants.REWARD_DELIM)).trim();
        String value = instruction.substring(instruction.indexOf(Constants.REWARD_DELIM)+ 1).trim();
        double reward;
        try {
            reward = Double.parseDouble(value);
        } catch (Exception ex) {
            throw new RuntimeException("Invalid value: " + value + ". Error: " + ex.getMessage());
        }
        Node node = nodeMap.getOrDefault(label, new Node(label));
        node.setReward(reward);
        node.setCurrValue(reward);
        node.setPreviousValue(reward);
        nodeMap.put(label, node);
        graph.put(node, graph.getOrDefault(node, new ArrayList<>()));
    }

    private static void handleEdgesInst(Map<Node, List<Edge>> graph, Map<String, Node> nodeMap, String instruction) {
        String label = instruction.substring(0, instruction.indexOf(Constants.EDGE_DELIM)).trim();
        String[] edgeLabels = instruction.substring(instruction.indexOf(Constants.EDGES_START_DELIM) + 1,
                instruction.indexOf(Constants.EDGES_END_DELIM)).trim().split(Constants.EDGES_DELIM);

        Node node = nodeMap.getOrDefault(label, new Node(label));
        nodeMap.put(label, node);
        List<Edge> edges = graph.getOrDefault(node, new ArrayList<>());
        int index = -1;
        if (! edges.isEmpty()) {
            index = 0;
        }
        for (String edgeLabel : edgeLabels) {
            edgeLabel = edgeLabel.trim();
            Node edgeNode = nodeMap.getOrDefault(edgeLabel, new Node(edgeLabel));
            nodeMap.put(edgeLabel, edgeNode);
            if (index >= 0) {
                edges.get(index++).setNode(edgeNode);
            } else {
                edges.add(new Edge(edgeNode));
            }
        }
        graph.put(node, edges);
    }

    private static void handleWeightInst(Map<Node, List<Edge>> graph, Map<String, Node> nodeMap, String instruction) throws RuntimeException {
        String label = instruction.substring(0, instruction.indexOf(Constants.WEIGHT_DELIM)).trim();
        Node node = nodeMap.get(label);
        if (node == null) {
            throw new RuntimeException("Node not found in map for instruction: " + instruction);
        }
        List<Edge> edges = graph.get(node);
        if (edges.isEmpty()) {
            throw new RuntimeException("No edges to assign probabilities for node: " + node.getLabel() + ", instruction: " + instruction);
        }
        String[] probValues = instruction.substring(instruction.indexOf(Constants.WEIGHT_DELIM) + 1).trim().split(Constants.WEIGHT_SPLIT_DELIM);
        List<Double> values = new ArrayList<>();
        for (String prob : probValues) {
            prob = prob.trim();
            if (!prob.isEmpty()) {
                double value;
                try {
                    value = Double.parseDouble(prob);
                } catch (Exception ex) {
                    throw new RuntimeException("Invalid value: " + prob + ", instruction: " + instruction + ". Error: " + ex.getMessage());
                }
                values.add(value);
            }
        }
        if (values.size() < 1) {
            throw new RuntimeException("No values to assign probabilities for node: " + node.getLabel() + ", instruction: " + instruction);
        } else if (values.size() == 1) {
            node.setDecisionNode(true);
            node.setAlpha(values.get(0));
        } else if (values.size() == edges.size()) {
            double sum = 0.0;
            for (int index = 0; index < values.size(); index++) {
                edges.get(index).setWeight(values.get(index));
                sum += values.get(index);
            }
            if (Util.abs(Double.compare(sum, 1.0)) > 0.01) {
                throw new RuntimeException("Probabilities do not sum up to 1.0 for node: " + node.getLabel() + ", instruction: " + instruction);
            }
        } else {
            throw new RuntimeException("Inconsistent count of edges and probabilities for node: " + node.getLabel() + ", instruction: " + instruction);
        }
    }

    private static List<String> readInputFile(String filePath) throws RuntimeException {
        List<String> instructions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while (line != null) {
                line = line.trim();
                if (! (line.startsWith(Constants.COMMENT_LINE) || line.isEmpty())) {
                    instructions.add(line);
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException fnfEx) {
            throw new RuntimeException("Invalid path for file: " + filePath + ". Error msg: " + fnfEx.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Failed to read Input File. Error msg: " + ex.getMessage());
        }
        // Sorting instructions in the order: ASSIGNMENT -> EDGES -> Weights
        instructions.sort((String a, String b) -> {
            if (a.contains(Constants.REWARD_DELIM)) {
                return -1;
            }
            if (b.contains(Constants.REWARD_DELIM)) {
                return 1;
            }
            if (a.contains(Constants.EDGE_DELIM)) {
                return -1;
            }
            if (b.contains(Constants.EDGE_DELIM)) {
                return 1;
            }
            return -1;
        });
        return instructions;
    }
}
