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

import main.Status;
import main.Table;
import main.dataStructures.MinIndexedDHeap;
import main.dataStructures.Edge;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Dijkstra {

    final int NUMBER_OF_NODES;
    private int edgeCount;
    private Table table;

    private double[] dist;
    private Integer[] prev;
    private List<List<Edge>> graph;

    // TODO: think about saving table and graph. Duplicate a little (also AStar)
    public Dijkstra(Table table) {
        this.table = table;
        graph = table.getGraph();
        NUMBER_OF_NODES = table.getNumberOfNodes();
        edgeCount = table.getEdgeCount();
    }

    public List<List<Edge>> getGraph() {
        return graph;
    }

    // Run Dijkstra's algorithm on a directed graph to find the shortest path
    // from a starting node to an ending node. If there is no path between the
    //starting node and the destination node the returned value is set to bb
    // Double.POSITIVE_INFINITY

    // Remove end node if you want to have shortest path to every node
    public double dijkstra(int start, int end) {

        // Keep an Indexed Priority Queue (ipq) of the next most promising node
        // to visit.
        int degree = edgeCount / NUMBER_OF_NODES;
        MinIndexedDHeap<Double> ipq = new MinIndexedDHeap(degree, NUMBER_OF_NODES);
        ipq.insert(start, 0.0);

        // Maintain an array of the minimum distance to each node.
        dist = new double[NUMBER_OF_NODES];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[start] = 0.0;

        table.addStartEnd(start, end);
        System.out.println(table.displayStatus());
        System.out.println("--");

        boolean[] visited = new boolean[NUMBER_OF_NODES];
        prev = new Integer[NUMBER_OF_NODES];

        while (!ipq.isEmpty()) {
            int nodeId = ipq.peekMinKeyIndex();

            visited[nodeId] = true;
            double minvalue = ipq.pollMinValue();

            // We already found a better path before we got to
            // processing this node so we can ignore it
            if (minvalue > dist[nodeId]) {
                continue;
            }

            for (Edge edge : graph.get(nodeId)) {

                // We cannot get a shorter path by revisiting
                // a node we have already visited before
                if (visited[edge.getTo()]) {
                    continue;
                }

                System.out.println("edge:" + prev[edge.getTo()] + " edegTo:" +edge.getTo());
                table.getAdjacencyList().get(nodeId).setStatus(Status.VISITED);
                System.out.println(table.displayStatus());


                // Relax edge by updating minimum cost if applicable
                double newDist = dist[nodeId] + edge.getCost();
                if (newDist < dist[edge.getTo()]) {
                    prev[edge.getTo()] = nodeId;
                    dist[edge.getTo()] = newDist;

                    System.out.println("Inside");
                    System.out.println("--");


                    // Insert the cost of going to a node for the first time in the PQ,
                    // or try and update it to a better value by calling decrease
                    if (!ipq.contains(edge.getTo())) {
                        ipq.insert(edge.getTo(), newDist);
                    } else {
                        ipq.decrease(edge.getTo(), newDist);
                    }
                }
            }
            // Once we've processed the end node we can return early (without
            // necessarily visiting the whole graph) because we know we cannot get a
            // shortest path by routing through any other nodes since Dijkstra's is
            //greedy and there are no negative edge weights.
            if (nodeId == end) {
                table.getAdjacencyList().get(nodeId).setStatus(Status.FINISH);
                System.out.println(table.displayStatus());
                System.out.println("--");
                return dist[end];
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
            return path;
        }

        for (Integer at = end; at != null; at = prev[at]) {
            path.add(at);

            table.getAdjacencyList().get(at).setStatus(Status.PATH);
            System.out.println(table.displayStatus());
            System.out.println("--");
        }
        Collections.reverse(path);
        return path;
    }

}


       
       