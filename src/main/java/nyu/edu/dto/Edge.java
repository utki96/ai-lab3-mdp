package nyu.edu.dto;

public class Edge {

    public Edge(Node node) {
        this.node = node;
        this.weight = 0.0;
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
