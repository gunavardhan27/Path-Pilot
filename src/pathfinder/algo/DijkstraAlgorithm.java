package pathfinder.algo;

import pathfinder.model.Grid;
import pathfinder.model.Node;

import java.util.Comparator;
import java.util.PriorityQueue;

public class DijkstraAlgorithm extends AlgorithmBase {

    private PriorityQueue<Node> openSet;

    public DijkstraAlgorithm(Grid grid, Node startNode, Node endNode) {
        super(grid, startNode, endNode);
        
        // PriorityQueue based only on gCost
        openSet = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return Integer.compare(n1.getGCost(), n2.getGCost());
            }
        });

        startNode.setGCost(0);
        openSet.add(startNode);
    }

    @Override
    public boolean runStep() {
        if (openSet.isEmpty()) {
            return true; // Finished, no path
        }

        Node current = openSet.poll();

        if (current == endNode) {
            reconstructPath();
            return true; // Path found
        }

        if (current.isVisited()) return false;

        if (current != startNode) {
            current.setVisited(true);
        }
        nodesExplored++;

        for (Node neighbor : grid.getNeighbors(current)) {
            if (neighbor.isVisited() || neighbor.isWall()) continue;

            int moveCost = (current.getRow() != neighbor.getRow() && current.getCol() != neighbor.getCol()) ? 14 : 10;
            int tentativeGCost = current.getGCost() + moveCost;

            if (tentativeGCost < neighbor.getGCost()) {
                neighbor.setGCost(tentativeGCost);
                neighbor.setParent(current);
                
                openSet.remove(neighbor);
                openSet.add(neighbor);
            }
        }

        return false;
    }
}
