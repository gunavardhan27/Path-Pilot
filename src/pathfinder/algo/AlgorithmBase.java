package pathfinder.algo;

import pathfinder.model.Grid;
import pathfinder.model.Node;

public abstract class AlgorithmBase {
    protected Grid grid;
    protected Node startNode;
    protected Node endNode;
    
    // Metrics
    protected int nodesExplored = 0;
    protected int pathLength = 0;
    protected long timeTaken = 0; // we may measure time differently, but can be tracked here.

    public AlgorithmBase(Grid grid, Node startNode, Node endNode) {
        this.grid = grid;
        this.startNode = startNode;
        this.endNode = endNode;
    }

    /**
     * Step the pathfinding algorithm by one iteration for visualization.
     * @return true if the algorithm is finished (path found or impossible), false if still running.
     */
    public abstract boolean runStep();

    /**
     * Backtrack from end node to start node to mark the path.
     * Returns the length of the path.
     */
    protected void reconstructPath() {
        Node current = endNode.getParent();
        while (current != null && current != startNode) {
            current.setPath(true);
            pathLength++;
            current = current.getParent();
        }
        if (pathLength > 0) {
            // including start and end
            pathLength++; 
        }
    }

    public int getNodesExplored() {
        return nodesExplored;
    }

    public int getPathLength() {
        return pathLength;
    }

    public long getTimeTaken() {
        return timeTaken;
    }
}
