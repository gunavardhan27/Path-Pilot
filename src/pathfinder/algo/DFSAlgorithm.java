package pathfinder.algo;

import pathfinder.model.Grid;
import pathfinder.model.Node;

import java.util.Stack;

public class DFSAlgorithm extends AlgorithmBase {

    private Stack<Node> stack;

    public DFSAlgorithm(Grid grid, Node startNode, Node endNode) {
        super(grid, startNode, endNode);
        stack = new Stack<>();
        stack.push(startNode);
    }

    @Override
    public boolean runStep() {
        if (stack.isEmpty()) {
            return true; 
        }

        Node current = stack.pop();

        if (current == endNode) {
            reconstructPath();
            return true; 
        }

        if (current.isVisited()) return false;

        if (current != startNode) {
            current.setVisited(true);
        }
        nodesExplored++;

        // Add neighbors in reverse to get intuitive direction mapping visually (optional)
        for (Node neighbor : grid.getNeighbors(current)) {
            if (!neighbor.isVisited() && !neighbor.isWall() && !stack.contains(neighbor)) {
                neighbor.setParent(current);
                stack.push(neighbor);
            }
        }

        return false;
    }
}
