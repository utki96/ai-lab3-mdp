package nyu.edu;

import nyu.edu.dto.Edge;
import nyu.edu.dto.Node;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String filePath = "/home/utkarshtyg/Documents/input3.txt";
        Map<Node, List<Edge>> graph = InputParser.parseInput(filePath);
        if (Util.isCycleInGraph(graph)) {
            System.out.println("Cycle found in graph");
        } else {
            System.out.println("No Cycle in graph");
            Node root = Util.getRootOfGraph(graph);
            System.out.println("Root: " + root.getLabel());
            TreeSolver treeSolver = new TreeSolver(false);
            treeSolver.solveNetwork(graph, root);
            treeSolver.printSolution(graph, root);
        }
    }
}