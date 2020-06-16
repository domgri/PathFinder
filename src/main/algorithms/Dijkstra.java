package main.algorithms;

/**
 * This file contains implementation od Dijkstra's shortest path algoritm
 * from a start node to a specific ending node. Dijkstra's can also be modified to find the shortest path between a starting node and all other nodes
 * in the graph with minimal effort.
 * DijkstrasShortestPathAdjacencyListWithDHeap
 * Source: https://www.youtube.com/watch?v=09_LlHjoEiY&t=2357s
 **/

//TODO: think about the implementation on the main list.
// Maybe change current implementation to this within everywhere or
// figure out another solution

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.Status;
import main.Grid;
import main.dataStructures.MinIndexedDHeap;
import main.dataStructures.Edge;

public class Dijkstra {

    final int NUMBER_OF_NODES;
    private int edgeCount;
    private Grid grid;

    private double[] distance;
    private Integer[] previous;
    private List<List<Edge>> adjacencyList;

    // TODO: think about saving table and graph. Duplicate a little (also AStar)
    // GRID only used for visualisations. All calculations performed in adjacency list
    public Dijkstra(Grid grid) {
        this.grid = grid;
        adjacencyList = grid.getAdjacencyList();
        NUMBER_OF_NODES = grid.getNumberOfNodes();
        edgeCount = grid.getEdgeCount();
    }

    public List<List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    // Run Dijkstra's algorithm on a directed graph to find the shortest path
    // from a starting node to an ending node. If there is no path between the
    //starting node and the destination node the returned value is set to be
    // Double.POSITIVE_INFINITY


    public double dijkstra(int start, int end) {

        // Indexed Priority Queue (ipq) contains nodes that were added as neighbours.
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

        while (!openSet.isEmpty()) {
            grid.displayClosedSet(closedSet);
            System.out.println(grid.displayOpenSet(openSet));

            // Gets the top node's id from the heap
            int nodeId = openSet.peekMinKeyIndex();


            // Poll the value form the heap, set it as visited
            double minvalue = openSet.pollMinValue();
            closedSet[nodeId] = true;

            // If we have a better path, continue with another node
            if (minvalue > distance[nodeId]) {
                continue;
            }

            // Otherwise, traverse through neighbours (edges)
            for (Edge edge : adjacencyList.get(nodeId)) {

                // We cannot get a shorter path by revisiting
                // a node we have already visited before
                if (closedSet[edge.getTo()]) {
                    continue;
                }

                // Relax edge by updating minimum cost if applicable
                double newDist = distance[nodeId] + edge.getCost();
                if (newDist < distance[edge.getTo()]) {
                    previous[edge.getTo()] = nodeId;
                    distance[edge.getTo()] = newDist;

                    System.out.println("Inside");
                    System.out.println("--");


                    // Insert the cost of going to a node for the first time in the PQ,
                    // or try and update it to a better value by calling decrease
                    if (!openSet.contains(edge.getTo())) {
                        openSet.insert(edge.getTo(), newDist);
                    } else {
                        openSet.decrease(edge.getTo(), newDist);
                    }
                }
            }
            // Once we've processed the end node we can return early (without
            // necessarily visiting the whole graph) because we know we cannot get a
            // shortest path by routing through any other nodes since Dijkstra's is
            //greedy and there are no negative edge weights.
            if (nodeId == end) {
                grid.getListOfNodes().get(nodeId).setStatus(Status.FINISH);
                System.out.println(grid.displayStatus());
                System.out.println("--");
                return distance[end];
            }

        }

        // End node is unreachable
        return Double.POSITIVE_INFINITY;


    }

    /**
     * Reconstructs the shortest path (of nodes) from 'start' to 'end' inclusive.
     *
     * @return AN array of node indexes of the shortest path from 'start' to 'end'. If 'start' and 'end' are not connected then an empty array
     * is returned
     */
    public List<Integer> reconstructPath(int start, int end) {
        if (end < 0 || end >= NUMBER_OF_NODES) {
            throw new IllegalArgumentException("Invalid node index");
        }
        if (start < 0 || start >= NUMBER_OF_NODES) {
            throw new IllegalArgumentException("Invalid node index");
        }

        List<Integer> path = new ArrayList<>();
        double dist = dijkstra(start, end);
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


       
       