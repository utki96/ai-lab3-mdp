package nyu.edu;

import nyu.edu.dto.Args;
import nyu.edu.dto.Edge;
import nyu.edu.dto.Node;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Args inputArgs = new Args(1.0, 0.01, 100, false);
        try {
            if (args.length > 5 || args.length < 1) {
                throw new RuntimeException("Invalid no of arguments provided: " + args.length);
            }
            String filePath = args[args.length - 1];
            for (int i = 0; i < args.length - 1; i++) {
                updateInputArgs(args[i], inputArgs);
            }

            Map<Node, List<Edge>> graph = InputParser.parseInput(filePath);
            if (Util.isCycleInGraph(graph)) {
                MDPSolver mdpSolver = new MDPSolver(inputArgs.isMin(), inputArgs.getTol(), inputArgs.getIter(), inputArgs.getDf());
                mdpSolver.solveMDP(graph);
                mdpSolver.printSolution(graph);
            } else {
                Node root = Util.getRootOfGraph(graph);
                TreeSolver treeSolver = new TreeSolver(inputArgs.isMin(), inputArgs.getDf());
                treeSolver.solveNetwork(graph, root);
                treeSolver.printSolution(graph, root);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }
        System.exit(1);
    }

    private static void updateInputArgs(String arg, Args inputArgs) throws RuntimeException {
        if (arg.contains("min")) {
            inputArgs.setMin(true);
        }
        if (arg.contains("df")) {
            inputArgs.setDf(Util.parseDoubleFromInput(arg));
        }
        if (arg.contains("tol")) {
            inputArgs.setTol(Util.parseDoubleFromInput(arg));
        }
        if (arg.contains("iter")) {
            inputArgs.setIter(Util.parseDoubleFromInput(arg));
        }
    }
}