/**
 * Grid (in Adjacency List) and DFS (recursively) implementation
 * @author Domas
 * @lastUpdate 2020-06-07
 * TODO: DFS
 *
 */

package main;
import main.algorithms.*;
import main.dataStructures.Node;

import java.util.*;


public class Main {

    public static void main(String[] args) {

        // Initialising table
        Grid grid = new Grid();

        // Walls generation
        grid.generateWalls(10);

        LinkedList<Node> listOfNodes = (LinkedList<Node>) grid.getListOfNodes(); // unweighted graph

        // Algorithms

        // 1. BFS
        BFS(grid, 0, 117);


        // 2. DFS (might not work properly)
        DFS(grid, 0, 117);

        // 3. Dijkstra's Shortest Path
        List<Integer> dijkstraPath = Dijkstra(grid, 0, 117);

        for (Integer index : dijkstraPath) {
            System.out.println(index);
        }

        // 4. AStar
        List<Integer> aStarPath = AStar(grid, 0, 117);

        for (Integer index : aStarPath) {
            System.out.println(index);
        }

    }

    public static void BFS(Grid grid, int start, int end) {
        ClearGrid(grid);

        BFS bfs = new BFS(grid);
        Node startNode = grid.getListOfNodes().get(start);
        Node endNode = grid.getListOfNodes().get(end);
        LinkedList<Node> bfsResult = bfs.start(startNode, endNode);
    }

    public static void DFS(Grid grid, int start, int end) {
        ClearGrid(grid);

        DFS dfs = new DFS(grid);
        Node startNode = grid.getListOfNodes().get(start);
        Node endNode = grid.getListOfNodes().get(end);
        LinkedList<Node> dfsResult = dfs.start(startNode, endNode);
    }

    public static List<Integer> Dijkstra(Grid grid, int start, int end) {
        ClearGrid(grid);

        Dijkstra dijkstra = new Dijkstra(grid);
        return dijkstra.start(start, end);
    }

    public static List<Integer> AStar(Grid grid, int start, int end) {
        ClearGrid(grid);

        AStar aStar = new AStar(grid);
        return aStar.start(start, end);

    }

    public static void ClearGrid(Grid grid) {
        grid.clearNodes();
    }



}
