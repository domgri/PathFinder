package main.algorithms;

import main.Status;
import main.Table;
import main.dataStructures.Edge;
import main.dataStructures.MinIndexedDHeap;
import main.dataStructures.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AStar {

    final int NUMBER_OF_NODES;
    private int edgeCount;
    private Table table;

    private double[] gScore;
    private double[] fScore;
    private Integer[] cameFrom;
    private List<List<Edge>> graph;

    public AStar(Table table) {
        this.table = table;
        graph = table.getAdjacencyList();
        NUMBER_OF_NODES = table.getNumberOfNodes();
        edgeCount = table.getEdgeCount();
    }


    public List<List<Edge>> getGraph() {
        return graph;
    }

    public double aStar(int start, int end) {

//    openSet := {start}
        int degree = edgeCount / NUMBER_OF_NODES;
        MinIndexedDHeap<Double> openSet = new MinIndexedDHeap(degree, NUMBER_OF_NODES);
        openSet.insert(start, 0.0);

//    cameFrom := an empty map
        cameFrom = new Integer[NUMBER_OF_NODES];

        boolean[] closedSet = new boolean[NUMBER_OF_NODES];

//    gScore := map with default value of Infinity
//    gScore[start] := 0
        gScore = new double[NUMBER_OF_NODES];
        Arrays.fill(gScore, Double.POSITIVE_INFINITY);
        gScore[start] = 0.0;

//    // For node n, fScore[n] := gScore[n] + h(n). fScore[n] represents our current best guess as to
//    fScore := map with default value of Infinity
//    fScore[start] := h(start)
        fScore = new double[NUMBER_OF_NODES];
        Arrays.fill(fScore, Double.POSITIVE_INFINITY);
        fScore[start] = h(start, end);
//
//    while openSet is not empty
        while (!openSet.isEmpty()) {
            table.displayClosedSet(closedSet);
            System.out.println(table.displayOpenSet(openSet));


//    current := the node in openSet having the lowest fScore[] value
            int current = openSet.peekMinKeyIndex();

//        if current = goal
//            return reconstruct_path(cameFrom, current)
            if (current == end) {
                table.getListOfNodes().get(current).setStatus(Status.FINISH);
                System.out.println(table.displayStatus());
                System.out.println("--");
                return gScore[end];
            }

//        openSet.Remove(current)
            double minvalue = openSet.pollMinValue();
            closedSet[current] = true; // REMOVE if needed
            //table.getAdjacencyList().get(nodeId).setStatus(Status.CLOSED);

            System.out.println("mnvalue " + minvalue);
            System.out.println("gScore[current] " + gScore[current]);

//            for each neighbor of current
            for (Edge neighbour : graph.get(current)) {

                if (closedSet[neighbour.getTo()]) {
                   continue;
                }
                //table.getAdjacencyList().get(current).setStatus(Status.VISITED);

    // d(current,neighbor) is the weight of the edge from current to neighbor
//    // tentative_gScore is the distance from start to the neighbor through current
//    tentative_gScore := gScore[current] + d(current, neighbor)
                double newDist = gScore[current] + neighbour.getCost();
//            if tentative_gScore < gScore[neighbor]
                if (newDist < gScore[neighbour.getTo()]) {

//    // This path to neighbor is better than any previous one. Record it!
//    cameFrom[neighbor] := current
                    cameFrom[neighbour.getTo()] = current;
//    gScore[neighbor] := tentative_gScore
                    gScore[neighbour.getTo()] = newDist;
//    fScore[neighbor] := gScore[neighbor] + h(neighbor)
                    fScore[neighbour.getTo()] = newDist + h(current, end);
//                if neighbor not in openSet
//                    openSet.add(neighbor)
                    // Adds by using guess===65465
                    if (!openSet.contains(neighbour.getTo())) {
                        openSet.insert(neighbour.getTo(), fScore[neighbour.getTo()]);
                    } else {
                        openSet.decrease(neighbour.getTo(), fScore[neighbour.getTo()]);
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

    private double h(int current, int end) {

        Node currentNode = table.getListOfNodes().get(current);
        Node endNode = table.getListOfNodes().get(end);

        System.out.println("currentNode:" + currentNode.getIndex() + " x: " + currentNode.getX() + " y:" + currentNode.getY());

        System.out.println("endNode:" + endNode.getIndex() + " x: " + endNode.getX() + " y:" + endNode.getY());


        /*
        double xs = Math.abs(endNode.getX() - currentNode.getX());
        double ys = Math.abs(endNode.getY() - currentNode.getY());

        return Math.hypot(xs, ys);
         */

        double dx = Math.abs(currentNode.getX() - endNode.getX());
        double dy = Math.abs(currentNode.getY() - endNode.getY());
        System.out.println("dx" + dx + " " + (currentNode.getX() - endNode.getX()));
        return dx + dy;
        //return Math.hypot(dx, dy);
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
            System.out.println("Positiv einfinity");
            return path;
        }

        for (Integer at = end; at != null; at = cameFrom[at]) {
            path.add(at);

            table.getListOfNodes().get(at).setStatus(Status.PATH);
            System.out.println(table.displayStatus());
            System.out.println("--");
        }
        Collections.reverse(path);
        return path;
    }
}
