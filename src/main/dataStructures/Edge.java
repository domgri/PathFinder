package main.dataStructures;

// An edge class to represent a directed edge
// between two nodes with a certain cost.
public class Edge {
    int to;
    double cost;

    public Edge(int to, double cost) {
        this.to = to;
        this.cost = cost;
    }

    public int getTo() {
        return to;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double newCost) {
        this.cost = newCost;
    }
}