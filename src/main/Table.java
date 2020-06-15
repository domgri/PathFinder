package main;


import main.algorithms.Dijkstra;
import main.dataStructures.MinIndexedDHeap;
import main.dataStructures.Node;
import main.dataStructures.Edge;

//TODO: simplyfy to only one representation

import java.util.*;

/**
 * Represents the table, where all the algorithms should be performed.
 * Consists of Nodes, connected with each other.
 * Connections are saved in Grid and Adjacency List
 */

//TODO: make sure dfs is correct, now its slightly weird
public class Table {

    private final static int ROW_SIZE = 12;
    private final static int COLUMN_SIZE = 12;
    private final static int NUMBER_OF_NODES = ROW_SIZE * COLUMN_SIZE;
    private final double DEFAULT_WALL_COST = 100.0;
    final int DEFAULT_COST = 1;

    private ArrayList<ArrayList<Node>> grid;
    private List<Node> adjacencyList;
    private List<List<Edge>> graph;
    private int edgeCount;

    public Table() {
        grid = setUpGrid();
        adjacencyList = setUpAdjacencyList();
        createGraph();
    }

    public int getNumberOfNodes() {
        return NUMBER_OF_NODES;
    }

    public List<List<Edge>> getGraph() {
        return graph;
    }

    public int getEdgeCount() {
        return edgeCount;
    }


    /**
     * O(n^2)
     * @return Empty grid
     */
    private ArrayList<ArrayList<Node>>setUpGrid(){

        ArrayList<ArrayList<Node>> newGrid = new ArrayList<ArrayList<Node>>();

        int counter = 0;
        for (int i = 0; i < COLUMN_SIZE; i++) {
            ArrayList<Node> newRow = new ArrayList<Node>();
            for(int j = 0; j < ROW_SIZE; j++) {
                Node newNode = new Node(i, j, counter);
                newRow.add(newNode);
                counter++;
            }
            newGrid.add(newRow);
        }

        for (int i = 0; i < COLUMN_SIZE; i++) {
            for(int j = 0; j < ROW_SIZE; j++) {
                setAllNeighbours(newGrid, null, newGrid.get(i).get(j), i, j);
            }
        }


        return newGrid;
    }

    //TODO: make nicer: ifs and method header
    /**
     * O(1)
     * @param newGrid
     * @param newList
     * @param newNode
     * @param x
     * @param y
     */
    private void setAllNeighbours(ArrayList<ArrayList<Node>> newGrid, LinkedList<Node> newList, Node newNode, int x, int y) {

        int index = newNode.getIndex();
        if (newGrid != null) {

            // Right neighbour
            if ((index % ROW_SIZE) + 1 < ROW_SIZE) {
                newNode.setNeighbour(newGrid.get(x).get(y + 1));
            }

            // Bottom neighbour
            if (index + ROW_SIZE < ROW_SIZE * COLUMN_SIZE) {
                newNode.setNeighbour(newGrid.get(x + 1).get(y));
            }

            // Left neighbour
            if ((index % ROW_SIZE) - 1 >= 0) {
                newNode.setNeighbour(newGrid.get(x).get(y - 1));
            }

            // Top neighbour
            if (index - ROW_SIZE > 0) {
                newNode.setNeighbour(newGrid.get(x - 1).get(y));
            }
            // think for list implementation
        } else if (newList != null) {

            // Right neighbour
            if ((index % ROW_SIZE) + 1 < ROW_SIZE) {
                newNode.setNeighbour(newList.get(index + 1));
            }

            // Bottom neighbour
            if (index + ROW_SIZE < ROW_SIZE * COLUMN_SIZE) {
                newNode.setNeighbour(newList.get(index + ROW_SIZE));
            }

            // Left neighbour
            if ((index % ROW_SIZE) - 1 >= 0) {
                newNode.setNeighbour(newList.get(index - 1));
            }

            // Top neighbour
            if (index - ROW_SIZE > 0) {
                newNode.setNeighbour(newList.get(index - ROW_SIZE));
            }
        }

    }

    /**
     * O(n)
     * @return
     */
    private LinkedList<Node> setUpAdjacencyList() {

        LinkedList<Node> newList = new LinkedList<Node>();
        int counter = 0;
        int x = 0;
        int y = 0;
        for (int i = 0; i < ROW_SIZE * COLUMN_SIZE; i++) {
            Node newNode = new Node(x, y, counter);
            newList.add(newNode);
            if (x == ROW_SIZE - 1) {
                y++;
                x = 0;
            } else {
                x++;
            }
            counter++;
        }

        x = 0;
        y = 0;
        for (int i = 0; i < ROW_SIZE * COLUMN_SIZE; i++) {
            setAllNeighbours(null, newList, newList.get(i), x, y);
            if (x == ROW_SIZE - 1) {
                y++;
                x = 0;
            } else {
                x++;
            }

        }

        return newList;
    }



    public List<Node> getAdjacencyList() {
        return adjacencyList;
    }

    // Grid for now
    @Override
    public String toString() {
        String result = "";
        result += "--------------\n";
        for (int i = 0; i < COLUMN_SIZE; i++) {
            result += "|";
            for(int j = 0; j < ROW_SIZE; j++) {
                result += grid.get(i).get(j).getIndex() + " ";
            }
            result += "|\n";
        }
        result += "--------------\n";
        return result;
    }

    public String toStringCoordinatesGrid() {
        String result = "";
        for (int i = 0; i < COLUMN_SIZE; i++) {
            for(int j = 0; j < ROW_SIZE; j++) {
                result += grid.get(i).get(j).getCoordinate() + " ";
            }
            result += "\n";
        }
        return result;
    }

    public String toStringNeighboursGrid() {
        String result = "";
        for (int i = 0; i < COLUMN_SIZE; i++) {
            for(int j = 0; j < ROW_SIZE; j++) {
                result += grid.get(i).get(j).getIndex() + " [";
                for (Node neighbour : grid.get(i).get(j).getNeighbours()) {
                    result += neighbour.getIndex() + " ";
                }
                result = result.substring(0, result.length() - 1);
                result += "], ";

            }
            result += "\n";
        }
        return result;
    }

    public String toStringNeighboursAdjacencyList() {

        String result = "";
        int counter = 0;
        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            result += adjacencyList.get(i).getIndex() + " [";
            for (Node neighbour : adjacencyList.get(i).getNeighbours()) {
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
            for (Edge neighbour : graph.get(i)) {
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
            result += getStatusAsSymbol(adjacencyList.get(i).getStatus()) + " ";
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
            adjacencyList.get(index).setStatus(Status.START);
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
                adjacencyList.get(index).setStatus(Status.OPEN);
            }
        }

        return displayStatus();
    }

    public void displayClosedSet(boolean[] closedSet) {

        for (int i = 0; i < closedSet.length; i++) {
            if (closedSet[i]) {
                adjacencyList.get(i).setStatus(Status.VISITED);
            }
        }

    }

    public String displayClosedSet(LinkedList<Integer> closedSet) {

        for(Integer index : closedSet) {
            adjacencyList.get(index).setStatus(Status.VISITED);
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
            case FINISH:
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

    public void createGraph() {

        createEmptyGraph();
        setAllNeighboursGraph();

    }

    // Construct an empty graph with numberOfNodes nodes including the source and sink nodes
    private void createEmptyGraph() {
        graph = new ArrayList<>(NUMBER_OF_NODES);
        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            graph.add(new ArrayList<>());
        }
    }

    //TODO: literally the same method as in others
    private void setAllNeighboursGraph() {


        for (int index = 0; index < NUMBER_OF_NODES; index++) {
            //System.out.println(index);
            // Right neighbour
            if ((index % ROW_SIZE) + 1 < ROW_SIZE) {
                addEdge(index, index + 1, DEFAULT_COST);
            }

            // Bottom neighbour
            if (index + ROW_SIZE < NUMBER_OF_NODES) {
                addEdge(index, index + ROW_SIZE, DEFAULT_COST);
            }

            // Left neighbour
            if ((index % ROW_SIZE) - 1 >= 0) {
                addEdge(index, index - 1, DEFAULT_COST);
            }

            // Top neighbour
            if (index - ROW_SIZE > 0) {
                addEdge(index, index - ROW_SIZE, DEFAULT_COST);
            }




        }

    }

    /**
     * Adds a directed edge to the graph
     *
     * @param from - The index of the node the directed edge starts at
     * @param to   - The index of the node the directed edge end at
     * @param cost - The cost of the edge
     */
    public void addEdge(int from, int to, int cost) {
        edgeCount++;
        graph.get(from).add(new Edge(to, cost));
    }

    // TODO: feature tpo implement
    public void addWeight(int index) {

        adjacencyList.get(index).setStatus(Status.WALL);

        for (Node neighbourNode : adjacencyList.get(index).getNeighbours()) {
            for (Edge edge : graph.get(neighbourNode.getIndex())) {
                if (edge.getTo() == index) {
                    edge.setCost(DEFAULT_WALL_COST);
                    break;
                }
            }

        }
    }

    public void addWall(int index) {

        adjacencyList.get(index).setStatus(Status.WALL);



        for (Node neighbourNode : adjacencyList.get(index).getNeighbours()) {

            System.out.println( neighbourNode.getIndex() + "before");
            for (Edge edge : graph.get(neighbourNode.getIndex())) {
                System.out.println(edge.getTo());
            }

            System.out.println("after");
            for (Edge edge : graph.get(neighbourNode.getIndex())) {
                if (edge.getTo() == index) {
                    graph.get(neighbourNode.getIndex()).remove(edge);
                    break;
                }

                /*
                if (edge.getTo() == index) {
                    graph.get(neighbourNode.getIndex()).remove(index);
                    edge.setCost(DEFAULT_WALL_COST);
                    break;
                }
                */

            }
            for (Edge edge : graph.get(neighbourNode.getIndex())) {
                System.out.println(edge.getTo());
            }

        }
    }

    public void addStartEnd(int start, int end) {
        adjacencyList.get(start).setStatus(Status.START);
        adjacencyList.get(start).setStatus(Status.FINISH);

        for (Node neighbourNode : adjacencyList.get(start).getNeighbours()) {
            for (Edge edge : graph.get(neighbourNode.getIndex())) {
                if (edge.getTo() == start) {
                    edge.setCost(DEFAULT_COST);
                    break;
                }
            }
        }

        for (Node neighbourNode : adjacencyList.get(end).getNeighbours()) {
            for (Edge edge : graph.get(neighbourNode.getIndex())) {
                if (edge.getTo() == end) {
                    edge.setCost(DEFAULT_COST);
                    break;
                }
            }
        }
    }

    public void generateWalls(int percent) {

        if ((percent < 0) || (percent > 100)) {
            throw new IllegalArgumentException("Number should be between 0 and 100");
        }

        Random r = new Random();

        for (Node node : adjacencyList) {
            if (r.nextInt(101) < percent) {
                addWall(node.getIndex());
            }
        }



    }

}
