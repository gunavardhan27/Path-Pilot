package pathfinder.algo;

import pathfinder.model.Grid;
import pathfinder.model.Node;

import java.util.LinkedList;
import java.util.Queue;

public class BFSAlgorithm extends AlgorithmBase {

    private Queue<Node> queue;

    public BFSAlgorithm(Grid grid, Node startNode, Node endNode) {
        super(grid, startNode, endNode);
        queue = new LinkedList<>();
        
        startNode.setVisited(true);
        queue.add(startNode);
    }

    @Override
    public boolean runStep() {
        if (queue.isEmpty()) {
            return true; 
        }

        Node current = queue.poll();

        if (current == endNode) {
            reconstructPath();
            return true; 
        }
        
        if (current != startNode) {
            current.setVisited(true);
        }
        nodesExplored++;

        for (Node neighbor : grid.getNeighbors(current)) {
            if (!neighbor.isVisited() && !neighbor.isWall() && !queue.contains(neighbor)) {
                neighbor.setParent(current);
                neighbor.setVisited(true); // mark visited ahead of time for BFS efficiency
                queue.add(neighbor);
            }
        }

        return false;
    }
}
