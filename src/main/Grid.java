package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import main.dataStructures.MinIndexedDHeap;
import main.dataStructures.Node;
import main.dataStructures.Edge;
import main.dataStructures.Queue;
import main.dataStructures.Stack;


/**
 * Represents the main grid.
 * Data stored in the listOfNodes(Nodes) and adjacencyList(Edges)
 */

//TODO: make sure dfs is correct, now its slightly weird
public class Grid {

    private final static int ROW_SIZE = 12;
    private final static int COLUMN_SIZE = 12;
    private final static int NUMBER_OF_NODES = ROW_SIZE * COLUMN_SIZE;
    private final double DEFAULT_WEIGHT_COST = 100.0;
    final int DEFAULT_EDGE_COST = 1;

    // List of nodes, with Node specific properties
    private List<Node> listOfNodes;
    // List of lists of edges
    // adjacencyList.get(A).getTo() -> from node A to node 'To', with edge specific properties ??
    private List<List<Edge>> adjacencyList;
    private int edgeCount;

    /**
     * Initializes listOfNodes and adjacencyList by reserving memory and setting connections between neighbours.
     */
    public Grid() {
        listOfNodes = new LinkedList<Node>();
        adjacencyList = new ArrayList<>(NUMBER_OF_NODES);
        createEmptyLists();
        setAllNeighbours();
    }

    /**
     * Creates an empty listOfNodes and adjacencyList. O(n)
     */
    public void createEmptyLists() {
        int counter = 0;
        int currentX = 0;
        int currentY = 0;

        for (int i = 0; i < NUMBER_OF_NODES; i++) {

            Node newNode = new Node(currentX, currentY, counter);
            listOfNodes.add(newNode);

            adjacencyList.add(new ArrayList<>());

            if (currentX == ROW_SIZE - 1) {
                currentY++;
                currentX = 0;
            } else {
                currentX++;
            }
            counter++;
        }
    }

    /**
     * Sets all neighbours for listOfNodes and  adjacencyList. O(n)
     */
    private void setAllNeighbours() {

        for (int index = 0; index < NUMBER_OF_NODES; index++) {

            // Right neighbour
            if ((index % ROW_SIZE) + 1 < ROW_SIZE) {
                addEdge(index, index + 1, DEFAULT_EDGE_COST);
                listOfNodes.get(index).setNeighbour(listOfNodes.get(index + 1));
            }

            // Bottom neighbour
            if (index + ROW_SIZE < NUMBER_OF_NODES) {
                addEdge(index, index + ROW_SIZE, DEFAULT_EDGE_COST);
                listOfNodes.get(index).setNeighbour(listOfNodes.get(index + ROW_SIZE));
            }

            // Left neighbour
            if ((index % ROW_SIZE) - 1 >= 0) {
                addEdge(index, index - 1, DEFAULT_EDGE_COST);
                listOfNodes.get(index).setNeighbour(listOfNodes.get(index - 1));
            }

            // Top neighbour
            if (index - ROW_SIZE > 0) {
                addEdge(index, index - ROW_SIZE, DEFAULT_EDGE_COST);
                listOfNodes.get(index).setNeighbour(listOfNodes.get(index - ROW_SIZE));
            }

        }

    }

    /**
     * Adds a directed edge to the adjacencyList. O(1)
     *
     * @param from - The index of the node the directed edge starts at
     * @param to   - The index of the node the directed edge end at
     * @param cost - The cost of the edge
     */
    public void addEdge(int from, int to, int cost) {
        edgeCount++;
        adjacencyList.get(from).add(new Edge(to, cost));
    }

    /**
     * Returns the total number of nodes in the grid
     * @return Total number of nodes
     */
    public int getNumberOfNodes() {
        return NUMBER_OF_NODES;
    }

    /**
     * Returns a listOfNodes
     * @return List<Node> listOfNodes
     */
    public List<Node> getListOfNodes() {
        return listOfNodes;
    }

    /**
     * Returns an adjacencyList
     * @return List<List<Edge>> adjacencyList
     */
    public List<List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    /**
     * Returns an edgeCount
     * @return int edgeCount
     */
    public int getEdgeCount() {
        return edgeCount;
    }

    /**
     * Sets a wall at the given index and removes edges towards it
     * @param index Node to set as wall
     */
    public void addWall(int index) {

        listOfNodes.get(index).setStatus(Status.WALL);

        for (Node neighbourNode : listOfNodes.get(index).getNeighbours()) {

            for (Edge edge : adjacencyList.get(neighbourNode.getIndex())) {
                if (edge.getTo() == index) {
                    adjacencyList.get(neighbourNode.getIndex()).remove(edge);
                    break;
                }
            }
        }
    }

    /**
     * Sets start, end nodes with specific marking and removes walls around them
     * @param start Start Node
     * @param end End Node
     */
    public void addStartEnd(int start, int end) {

        listOfNodes.get(start).setStatus(Status.START);
        listOfNodes.get(start).setStatus(Status.END);

        for (Node neighbourNode : listOfNodes.get(start).getNeighbours()) {
            for (Edge edge : adjacencyList.get(neighbourNode.getIndex())) {
                if (edge.getTo() == start) {
                    edge.setCost(DEFAULT_EDGE_COST);
                    break;
                }
            }
        }

        for (Node neighbourNode : listOfNodes.get(end).getNeighbours()) {
            for (Edge edge : adjacencyList.get(neighbourNode.getIndex())) {
                if (edge.getTo() == end) {
                    edge.setCost(DEFAULT_EDGE_COST);
                    break;
                }
            }
        }
    }

    /**
     * Randomly places wall, according to the given percentage. O(n)
     * @param percent An approximate level of walls density (from 0 to 100)
     */
    public void generateWalls(int percent) {

        if ((percent < 0) || (percent > 100)) {
            throw new IllegalArgumentException("Number should be between 0 and 100");
        }

        Random r = new Random();

        for (Node node : listOfNodes) {
            if (r.nextInt(101) < percent) {
                addWall(node.getIndex());
            }
        }

    }

    /**
     * Clears symbolic representation of Nodes. O(n)
     */
    public void clearNodes() {
        for (Node node : listOfNodes) {
            if (node.getStatus() == Status.WALL) {
                node.setStatus(Status.WALL);
            } else {
                node.setStatus(Status.EMPTY);
            }

        }
    }


    @Override
    public String toString() {

        return "";
    }

    public String toStringNeighboursAdjacencyList() {

        String result = "";
        int counter = 0;
        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            result += listOfNodes.get(i).getIndex() + " [";
            for (Node neighbour : listOfNodes.get(i).getNeighbours()) {
                result += neighbour.getIndex() + " ";
            }
            result = result.substring(0, result.length() - 1);
            result += "], ";
            if (counter == ROW_SIZE - 1) {
                counter = 0;
                result += "\n";
            } else {
                counter++;
            }

        }

        return result;
    }

    public String toStringNeighboursGraph() {

        String result = "";
        int counter = 0;
        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            result += i + " [";
            for (Edge neighbour : adjacencyList.get(i)) {
                result += neighbour.getTo() + " ";
            }
            result = result.substring(0, result.length() - 1);
            result += "], ";
            if (counter == ROW_SIZE - 1) {
                counter = 0;
                result += "\n";
            } else {
                counter++;
            }

        }

        return result;
    }

    public String displayStatus() {

        String result = "";
        int counter = 0;
        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            result += getStatusAsSymbol(listOfNodes.get(i).getStatus()) + " ";
            if (counter == ROW_SIZE - 1) {
                counter = 0;
                result += "\n";
            } else {
                counter++;
            }
        }

        /*
        try {
            Thread.sleep(10);
            //new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch(Exception ex) {

        }

         */


        return result;
    }

    public String displayDebug(MinIndexedDHeap openSet, boolean[] closedSet) {

        /*
        System.out.println("openSet length " + openSet.im.length);
        for (int i = 0; i < openSet.im.length; i++) {
            if (openSet.im[i] > 0) {
                System.out.println(openSet.im[i]);
                adjacencyList.get(openSet.im[i]).setStatus(Status.VISITED);
            }

        }

         */
        /*
        for (int index: openSet.im) {
            adjacencyList.get(index).setStatus(Status.VISITED);
        }

         */
    /*
        for (int i = 0; i < closedSet.length; i++) {
            if (closedSet[i]) {
                adjacencyList.get(i).setStatus(Status.CLOSED);
            }
        }
    */


        return displayStatus();
    }

    public String displayOpenSet(HashMap<Integer, Double> openSet) {

        for ( int index : openSet.keySet() ) {
            listOfNodes.get(index).setStatus(Status.START);
        }

//        for (int i = 0; i < closedSet.length; i++) {
//            if (closedSet[i]) {
//                adjacencyList.get(i).setStatus(Status.CLOSED);
//            }
//        }

        return displayStatus();

    }

    public String displayOpenSet(MinIndexedDHeap openSet) {

        for (int index : openSet.im) {
            if (index != -1) {
                listOfNodes.get(index).setStatus(Status.OPEN);
            }
        }

        return displayStatus();
    }

    public String displayQueue(Queue<Node> openSet) {

        for (Node node : openSet) {
            listOfNodes.get(node.getIndex()).setStatus(Status.OPEN);
        }

        return displayStatus();
    }

    public String displayStack(Stack<Node> openSet) {

        for (Node node : openSet) {
            listOfNodes.get(node.getIndex()).setStatus(Status.OPEN);
        }

        return displayStatus();
    }

    public void displayClosedSet(boolean[] closedSet) {

        for (int i = 0; i < closedSet.length; i++) {
            if (closedSet[i]) {
                listOfNodes.get(i).setStatus(Status.VISITED);
            }
        }

    }

    public String displayClosedSet(LinkedList<Integer> closedSet) {

        for(Integer index : closedSet) {
            listOfNodes.get(index).setStatus(Status.VISITED);
        }

        return displayStatus();
    }

    public String getStatusAsSymbol(Status status) {

        switch(status) {
            case EMPTY:
                return " ";
            case VISITED:
                return "V";
            case START:
                return "S";
            case END:
                return "E";
            case PATH:
                return "+";
            case WALL:
                return "█";
            case OPEN:
                return "O";
            default:
                return "error";
        }

    }


    // TODO: possible feature (useful for Dijkstra, AStar)
    public void addWeight(int index) {

        listOfNodes.get(index).setStatus(Status.WALL);

        for (Node neighbourNode : listOfNodes.get(index).getNeighbours()) {
            for (Edge edge : adjacencyList.get(neighbourNode.getIndex())) {
                if (edge.getTo() == index) {
                    edge.setCost(DEFAULT_WEIGHT_COST);
                    break;
                }
            }

        }
    }



}
