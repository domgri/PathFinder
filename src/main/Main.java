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

    //final static int ROW_SIZE = 12;
    //final static int COLUMN_SIZE = 12;

    public static void main(String[] args) {

        // Initialising table
        Table table = new Table();
        /*
        LinkedList<Node> graph = (LinkedList<Node>) table.getAdjacencyList(); // unweighted graph
        System.out.println(table.toString());
        System.out.println(table.toStringNeighboursAdjacencyList());
        System.out.println(table.displayStatus());
        System.out.println(table.toStringNeighboursGraph());
        */



        // Algorithms
        /*
        // 1. BFS
        BFS bfs = new BFS(table);
        LinkedList<Node> bfsResult = bfs.bfs(graph.get(5), graph.get(22));

        // 2. DFS (might not work properly)
        DFS dfs = new DFS(table);
        LinkedList<Node> dfsResult = dfs.dfs(graph.get(5), graph.get(22));

*/
        table.generateWalls(20);

        /*
        // 3. Dijkstra's Shortest Path
        long startTimeD = System.nanoTime();

        Dijkstra dijkstra = new Dijkstra(table);

        for(int index: dijkstra.reconstructPath(0, 624)) {
            System.out.println(index);
        }

        long endTimeD   = System.nanoTime();
        long totalTimeD = endTimeD - startTimeD;


         */





        /*
        for (int i = 61; i <= 69; i++) {
            table.addWall(i);
        }

        table.addWall(51);
        table.addWall(41);
        table.addWall(31);

         */

        // 4. AStar



        long startTimeA = System.nanoTime();
        AStar aStar = new AStar(table);

        for (int index : aStar.reconstructPath(0, 143)) {
            System.out.println(index);
        }

        System.out.println(table.displayStatus());

        long endTimeA   = System.nanoTime();
        long totalTimeA = endTimeA - startTimeA;

        //System.out.println(totalTimeD);
        System.out.println(totalTimeA);
        //.....

    }

}
