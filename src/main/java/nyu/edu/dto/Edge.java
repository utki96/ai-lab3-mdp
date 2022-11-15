package nyu.edu.dto;

public class Edge {

    public Edge() {
        this.weight = 0.0;
        this.node = null;
    }

    public Edge(Node node) {
        this.node = node;
        this.weight = 0.0;
    }

    public Edge(Node node, double wt) {
        this.node = node;
        this.weight = wt;
    }

    private Node node;
    private double weight;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
