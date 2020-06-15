package main.algorithms;

import main.dataStructures.Node;
import main.dataStructures.Stack;
import main.Status;
import main.Table;

import java.util.List;
import java.util.LinkedList;

public class DFS {

    final int NUMBER_OF_NODES;
    private Table table;

    public DFS(Table table) {
        this.table = table;
        NUMBER_OF_NODES = table.getNumberOfNodes();
    }

    public LinkedList<Node> dfs(Node start, Node end) {

        // Perform a DFS from 'start' node
        // Explanation: ParentTracker[child's index] = parent node
        LinkedList<Node> parentTracker = solveDfs(start, end, (LinkedList<Node>)table.getListOfNodes());


        // Return reconstructed (reversed) path from s -> e
        return reconstructPath(start, end, parentTracker, (LinkedList<Node>)table.getListOfNodes());
    }

    public LinkedList<Node> solveDfs(Node start, Node end, LinkedList<Node> graph) {

        Stack<Node> stack = new Stack<Node>();
        // Ensures which nodes are already visited
        boolean[] visitedNodes = new boolean[NUMBER_OF_NODES];
        // relationsList[child's index] = parent node
        LinkedList<Node> relationsList = new LinkedList<Node>();
        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            relationsList.add(null);
        }

        stack.push(start);
        visitedNodes[start.getIndex()] = true;
        graph.get(start.getIndex()).setStatus(Status.START);

        // O(n) ( worst case: O(n * 4) )
        while (!stack.isEmpty()) {

            // Gets front node
            Node node = stack.pop();

            // If exit node, terminate
            if (node == end) {
                return relationsList;
            }

            // Gets all node's neighbours
            List<Node> neighbours = graph.get(node.getIndex()).getNeighbours();

            for (Node next : neighbours) {
                // Add to queue unvisited neighbour, mark it as visited, set relation with parent
                if (!visitedNodes[next.getIndex()]) {
                    stack.push(next);
                    visitedNodes[next.getIndex()] = true;
                    graph.get(next.getIndex()).setStatus(Status.VISITED);
                    relationsList.set(next.getIndex(), node);

                    System.out.println(table.displayStatus());
                    System.out.println("--");
                }
            }
        }

        return relationsList;
    }

    /**
     * Reconstructs actual path and reverses it
     * @param start The Starting node
     * @param end The node to finish with
     * @param parentTracker list of solved BFS with parent and children relations
     * @return list of node from 'start -> end'
     */
    public LinkedList<Node> reconstructPath(Node start, Node end, LinkedList<Node> parentTracker, LinkedList<Node> graph) {

        // Reconstruct path going backwards from end, using parentTracker
        LinkedList<Node> path = new LinkedList<Node>();
        for (Node at = end; at != null; at = parentTracker.get(at.getIndex())) {
            path.add(at);
        }

        // Reverse the path, starting from the last Node (to have 'start -> end' traversal)
        LinkedList<Node> reversedList = new LinkedList<Node>();
        for (int i = path.size() - 1; i >= 0; i--) {
            reversedList.add(path.get(i));
            graph.get(path.get(i).getIndex()).setStatus(Status.PATH);

            System.out.println(table.displayStatus());
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
