package pathfinder.algo;

import pathfinder.model.Grid;
import pathfinder.model.Node;

import java.util.Comparator;
import java.util.PriorityQueue;

public class AStarAlgorithm extends AlgorithmBase {

    public enum HeuristicType {
        MANHATTAN, EUCLIDEAN, DIAGONAL
    }

    private PriorityQueue<Node> openSet;
    private HeuristicType heuristicType;

    public AStarAlgorithm(Grid grid, Node startNode, Node endNode, HeuristicType heuristicType) {
        super(grid, startNode, endNode);
        this.heuristicType = heuristicType;
        
        // PriorityQueue based on fCost, then hCost
        openSet = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                int fCmp = Integer.compare(n1.getFCost(), n2.getFCost());
                if (fCmp == 0) {
                    return Integer.compare(n1.getHCost(), n2.getHCost());
                }
                return fCmp;
            }
        });

        startNode.setGCost(0);
        startNode.setHCost(calculateHeuristic(startNode, endNode));
        openSet.add(startNode);
    }

    private int calculateHeuristic(Node current, Node target) {
        int dx = Math.abs(current.getRow() - target.getRow());
        int dy = Math.abs(current.getCol() - target.getCol());

        switch (heuristicType) {
            case MANHATTAN:
                // Multiply by 10 to keep integer costs (assuming basic cost is 10)
                return 10 * (dx + dy);
            case DIAGONAL:
                // Assuming orthogonal move cost 10, diagonal move cost 14
                return 10 * (dx + dy) + (14 - 2 * 10) * Math.min(dx, dy);
            case EUCLIDEAN:
                return (int) (10 * Math.sqrt(dx * dx + dy * dy));
            default:
                return 0;
        }
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

        if (current != startNode) {
            current.setVisited(true);
        }
        nodesExplored++;

        for (Node neighbor : grid.getNeighbors(current)) {
            if (neighbor.isVisited() || neighbor.isWall()) continue;

            // distance between current and neighbor
            int moveCost = (current.getRow() != neighbor.getRow() && current.getCol() != neighbor.getCol()) ? 14 : 10;
            int tentativeGCost = current.getGCost() + moveCost;

            if (tentativeGCost < neighbor.getGCost() || !openSet.contains(neighbor)) {
                neighbor.setGCost(tentativeGCost);
                neighbor.setHCost(calculateHeuristic(neighbor, endNode));
                neighbor.setParent(current);

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                } else {
                    // Update in PQ by removing and re-adding
                    openSet.remove(neighbor);
                    openSet.add(neighbor);
                }
            }
        }

        return false;
    }
}
