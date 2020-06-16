package main.algorithms;

import java.util.LinkedList;
import java.util.List;

import main.dataStructures.Node;
import main.dataStructures.Queue;
import main.Status;
import main.Grid;

public class BFS {

    //TODO: make sure its the best approach (both bfs and dfs)
    final int NUMBER_OF_NODES;;
    private Grid grid;

    public BFS(Grid grid) {
        this.grid = grid;
        NUMBER_OF_NODES = grid.getNumberOfNodes();
    }

    //TODO: time complexity
    /**
     * Breadth-first search algorithm implementation without recursion. O(?)
     * @param start Node to start from
     * @param end Node to finish
     * @return Path from start to finish
     */
    public LinkedList<Node>start(Node start, Node end) {

        // Perform a BFS from 'start' node
        // Explanation: ParentTracker[child's index] = parent node
        // Later used in path building process
        LinkedList<Node> parentTracker = solveBfs(start, end, (LinkedList<Node>) grid.getListOfNodes());

        // Return reconstructed (reversed) path from s -> e
        return reconstructPath(start, end, parentTracker, (LinkedList<Node>) grid.getListOfNodes());
    }

    /**
     * The main function to solve graph using BFS
     * @param start Node to start with
     * @return Path from finish to start node
     */
    public LinkedList<Node> solveBfs(Node start, Node end, LinkedList<Node> graph) {

        // Main implementation difference from DFS - Queue
        Queue<Node> queue = new Queue<Node>();

        // Displays which nodes are already visited
        // visitedNodes[node's id] = true/false (visited/not visited)
        boolean[] visitedNodes = new boolean[NUMBER_OF_NODES];

        // List for tracking parent nodes
        // previous[child's index] = parent node
        LinkedList<Node> previous = new LinkedList<Node>();


        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            previous.add(null);
        }

        // First node set up
        queue.enqueue(start);
        visitedNodes[start.getIndex()] = true;
        graph.get(start.getIndex()).setStatus(Status.START);


        // O(n) ( worst case: O(n * 4) ) ?
        while (!queue.isEmpty()) {
            grid.displayClosedSet(visitedNodes);
            System.out.println(grid.displayQueue(queue));

            // Gets front node from the queue
            Node node = queue.dequeue();

            // If its exit node, terminate
            if (node == end) {
                return previous;
            }
            // Otherwise, get all node's neighbours
            List<Node> neighbours = graph.get(node.getIndex()).getNeighbours();

            // Traverse through neighbours
            for (Node next : neighbours) {
                // Add to queue unvisited neighbour, mark it as visited, set relation with parent
                if (!visitedNodes[next.getIndex()]) {
                    queue.enqueue(next);
                    visitedNodes[next.getIndex()] = true;
                    previous.set(next.getIndex(), node);

                    System.out.println(grid.displayStatus());
                    System.out.println("--");
                }
            }
        }

        return previous;
    }

    /**
     * Returns reconstructed and reversed (start -> end) path
     * @param start The Starting node
     * @param end The node to finish with
     * @param previous list parent & children relations
     * @return list of node from 'start -> end
     */
    public LinkedList<Node> reconstructPath(Node start, Node end, LinkedList<Node> previous, LinkedList<Node> listOfNodes) {

        // Reconstruct path going backwards from end, using parentTracker
        LinkedList<Node> path = new LinkedList<Node>();
        for (Node at = end; at != null; at = previous.get(at.getIndex())) {
            path.add(at);
        }

        // Reverse the path, starting from the last Node (to have 'start -> end' traversal)
        LinkedList<Node> reversedList = new LinkedList<Node>();
        for (int i = path.size() - 1; i >= 0; i--) {
            reversedList.add(path.get(i));
            listOfNodes.get(path.get(i).getIndex()).setStatus(Status.PATH);

            System.out.println(grid.displayStatus());
            System.out.println("--");
        }

        // Return the path
        if (reversedList.get(0) == start) {
            return reversedList;
            // If start and end are connected return empty path
        } else {
            return new LinkedList<Node>();
        }

    }
}
