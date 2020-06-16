package main.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import main.Status;
import main.Grid;
import main.dataStructures.Edge;
import main.dataStructures.MinIndexedDHeap;
import main.dataStructures.Node;

public class AStar {

    final int NUMBER_OF_NODES;
    private int edgeCount;
    private Grid grid;

    private double[] distance;
    private double[] score;
    private Integer[] previous;
    private List<List<Edge>> adjacencyList;

    public AStar(Grid grid) {
        this.grid = grid;
        adjacencyList = grid.getAdjacencyList();
        NUMBER_OF_NODES = grid.getNumberOfNodes();
        edgeCount = grid.getEdgeCount();
    }

    public List<List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public List<Integer> start(int start, int end) {

        return reconstructPath(start, end);
    }

    public double aStar(int start, int end) {

        // Indexed Priority Queue (ipq) contains nodes that were added as most promising neighbours
        // On top, there is the most promising one.
        // Lookup O(1)
        int degree = edgeCount / NUMBER_OF_NODES;
        MinIndexedDHeap<Double> openSet = new MinIndexedDHeap(degree, NUMBER_OF_NODES);
        openSet.insert(start, 0.0);

        // Maintain an array of the minimum distance to each node.
        distance = new double[NUMBER_OF_NODES];
        Arrays.fill(distance, Double.POSITIVE_INFINITY);
        distance[start] = 0.0;

        grid.addStartEnd(start, end);
        System.out.println(grid.displayStatus());
        System.out.println("--");

        // List for tracking parent nodes
        // previous[child's index] = parent node
        previous = new Integer[NUMBER_OF_NODES];

        // Displays which nodes are already visited
        // closedSet[node's id] = true/false (visited/not visited)
        boolean[] closedSet = new boolean[NUMBER_OF_NODES];

        // Represents current best guess
        // score[n] = distance[n] + heuristic[n]
        score = new double[NUMBER_OF_NODES];
        Arrays.fill(score, Double.POSITIVE_INFINITY);
        score[start] = heuristic(start, end);

        while (!openSet.isEmpty()) {
            grid.displayClosedSet(closedSet);
            System.out.println(grid.displayOpenSet(openSet));


            // Gets the top node's id from the heap
            int nodeId = openSet.peekMinKeyIndex();


            // If its the end node, terminate
            if (nodeId == end) {
                grid.getListOfNodes().get(nodeId).setStatus(Status.FINISH);
                System.out.println(grid.displayStatus());
                System.out.println("--");
                return distance[end];
            }

            // Poll the value form the heap, set it as visited
            double minvalue = openSet.pollMinValue();
            closedSet[nodeId] = true; // REMOVE if needed

            // Traverse through neighbours (edges)
            for (Edge neighbour : adjacencyList.get(nodeId)) {

                // We cannot get a shorter path by revisiting
                // a node we have already visited before
                if (closedSet[neighbour.getTo()]) {
                   continue;
                }


                double newDist = distance[nodeId] + neighbour.getCost();
                if (newDist < distance[neighbour.getTo()]) {
                    previous[neighbour.getTo()] = nodeId;
                    distance[neighbour.getTo()] = newDist;
                    score[neighbour.getTo()] = newDist + heuristic(nodeId, end);

                    // Insert the cost of going to a node for the first time in the PQ,
                    // or try and update it to a better value by calling decrease
                    if (!openSet.contains(neighbour.getTo())) {
                        openSet.insert(neighbour.getTo(), score[neighbour.getTo()]);
                    } else {
                        openSet.decrease(neighbour.getTo(), score[neighbour.getTo()]);
                    }
                }

            }

        }
         return Double.POSITIVE_INFINITY;



        //----------------------
        // Alternative solution
        /*

        HashMap<Integer, Double> openSet = new HashMap<Integer, Double>();
        openSet.put(start, 0.0);

        LinkedList<Integer> closedSet = new LinkedList<Integer>();

        cameFrom = new Integer[NUMBER_OF_NODES];

        gScore = new double[NUMBER_OF_NODES];
        Arrays.fill(gScore, Double.POSITIVE_INFINITY);
        gScore[start] = 0.0;

        fScore = new double[NUMBER_OF_NODES];
        Arrays.fill(fScore, Double.POSITIVE_INFINITY);
        fScore[start] = h(start, end);
        int count = 0;
        while(!openSet.isEmpty()) {
            System.out.println(table.displayOpenSet(openSet));
            System.out.println(table.displayClosedSet(closedSet));

            System.out.println(openSet.size());
            //System.out.println(count);
            //count++;

            int minKey = -1;
            double minValue = Double.POSITIVE_INFINITY;
            for ( int index : openSet.keySet() ) {
                double value = openSet.get(index);
                if (value < minValue) {
                    minValue = value;
                    minKey = index;
                }
            }
            System.out.println("minindex: " + minKey + " minvalue: " + minValue );



            int current = minKey;

            if (current == end) {
                return gScore[current];
            } else if (current == -1) {
                break;
            }

            openSet.remove(current);
            System.out.println("current" + current);
            closedSet.add(current);
            System.out.println(openSet.size());

            for (Edge neighbour : graph.get(current)) {
                if (closedSet.contains(neighbour.getTo())) {
                    continue;
                }

                double tentaive_gScore = gScore[current] + neighbour.getCost();

                if (tentaive_gScore < gScore[neighbour.getTo()]) {

                    cameFrom[neighbour.getTo()] = current;
                    gScore[neighbour.getTo()] = tentaive_gScore;
                    fScore[neighbour.getTo()] = gScore[neighbour.getTo()] + h(current, end);

                    if (!openSet.containsKey(neighbour.getTo())) {
                        openSet.put(neighbour.getTo(), fScore[neighbour.getTo()]);
                    } else {
                        if (fScore[neighbour.getTo()] < openSet.get(neighbour.getTo())) {
                            openSet.replace(neighbour.getTo(), fScore[neighbour.getTo()]);
                        }
                    }

                }

            }


        }
        return Double.POSITIVE_INFINITY;
*/

    }

    /**
     * Returns approximate distance between nodes (lower better)
     * Implementation - Manhattan distance
     * @param current current node index
     * @param end end node index
     * @return calculation
     */
    private double heuristic(int current, int end) {
        Node currentNode = grid.getListOfNodes().get(current);
        Node endNode = grid.getListOfNodes().get(end);

        double dx = Math.abs(currentNode.getX() - endNode.getX());
        double dy = Math.abs(currentNode.getY() - endNode.getY());

        return dx + dy;
    }

    public List<Integer> reconstructPath(int start, int end) {
        if (end < 0 || end >= NUMBER_OF_NODES) {
            throw new IllegalArgumentException("Invalid node index");
        }
        if (start < 0 || start >= NUMBER_OF_NODES) {
            throw new IllegalArgumentException("Invalid node index");
        }

        List<Integer> path = new ArrayList<>();
        double dist = aStar(start, end);
        if (dist == Double.POSITIVE_INFINITY) {
            System.out.println("No path!");
            return path;
        }

        for (Integer at = end; at != null; at = previous[at]) {
            path.add(at);

            grid.getListOfNodes().get(at).setStatus(Status.PATH);
            System.out.println(grid.displayStatus());
            System.out.println("--");
        }

        Collections.reverse(path);

        return path;
    }
}
