package nyu.edu.dto;

public class Node {

    public Node() {
        this.reward = 0.0;
        this.currValue = 0.0;
        this.previousValue = 0.0;
    }

    public Node(String label) {
        this.label = label;
        this.reward = 0.0;
        this.currValue = 0.0;
        this.previousValue = 0.0;
    }

    private String label;
    private double previousValue;
    private double currValue;
    private double reward;
    private boolean isDecisionNode;
    private double alpha;
    private Node policyMove;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(double previousValue) {
        this.previousValue = previousValue;
    }

    public double getCurrValue() {
        return currValue;
    }

    public void setCurrValue(double currValue) {
        this.currValue = currValue;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public boolean isDecisionNode() {
        return isDecisionNode;
    }

    public void setDecisionNode(boolean decisionNode) {
        isDecisionNode = decisionNode;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public Node getPolicyMove() {
        return policyMove;
    }

    public void setPolicyMove(Node policyMove) {
        this.policyMove = policyMove;
    }

    @Override
    public String toString() {
        return String.format("%s : Reward = %f, Value = %f", this.label, this.reward, this.currValue);
    }
}
