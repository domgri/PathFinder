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

        // generating walls
        grid.generateWalls(10);

        LinkedList<Node> graph = (LinkedList<Node>) grid.getListOfNodes(); // unweighted graph

        // Algorithms

        /*
        // 1. BFS
        BFS bfs = new BFS(grid);
        LinkedList<Node> bfsResult = bfs.start(graph.get(0), graph.get(117));


        // 2. DFS (might not work properly)
        grid.clearNodes();
        DFS dfs = new DFS(grid);
        LinkedList<Node> dfsResult = dfs.start(graph.get(0), graph.get(117));

         */




        // 3. Dijkstra's Shortest Path

        grid.clearNodes();
        Dijkstra dijkstra = new Dijkstra(grid);

        for(int index: dijkstra.reconstructPath(0, 143)) {
            System.out.println(index);
        }





        // 4. AStar


        grid.clearNodes();
        AStar aStar = new AStar(grid);

        for (int index : aStar.reconstructPath(0, 143)) {
            System.out.println(index);
        }

        System.out.println(grid.displayStatus());


        //System.out.println(totalTimeD);
        //System.out.println(totalTimeA);
        //...


    }



}
