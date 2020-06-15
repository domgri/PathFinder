package main.dataStructures;

import main.Status;

import java.util.LinkedList;
import java.util.List;

/**
 * Main node class
 */
public class Node {
    private int x;
    private int y;
    private int index;
    private List<Node> neighbours = new LinkedList<Node>();
    private Status status;

    public Node(int x, int y, int index) {
        this.x = x;
        this.y = y;
        this.index = index;
        this.status = Status.EMPTY;
    }

    public String getCoordinate() {
        return "(" + x + ", " + y + ")";
    }

    public List<Node> getNeighbours() {
        return neighbours;
    }

    public void setNeighbour(Node neighbour) {
        neighbours.add(neighbour);
    }

    public int getIndex() {
        return index;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status newStatus) {
        this.status = newStatus;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
