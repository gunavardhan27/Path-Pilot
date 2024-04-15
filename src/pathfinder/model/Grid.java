package pathfinder.model;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private int rows;
    private int cols;
    private Node[][] nodes;
    
    // Diagonal config
    private boolean diagonalsEnabled = false;

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.nodes = new Node[rows][cols];
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                nodes[r][c] = new Node(r, c);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Node getNode(int r, int c) {
        if (r >= 0 && r < rows && c >= 0 && c < cols) {
            return nodes[r][c];
        }
        return null;
    }

    public void setDiagonalsEnabled(boolean enabled) {
        this.diagonalsEnabled = enabled;
    }

    public boolean isDiagonalsEnabled() {
        return this.diagonalsEnabled;
    }

    public List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int r = node.getRow();
        int c = node.getCol();

        // 4 directions: Up, Right, Down, Left
        int[][] dirs = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        for (int[] d : dirs) {
            Node n = getNode(r + d[0], c + d[1]);
            if (n != null && !n.isWall()) {
                neighbors.add(n);
            }
        }

        if (diagonalsEnabled) {
            // 4 diagonals: Top-Right, Bottom-Right, Bottom-Left, Top-Left
            int[][] diagDirs = {{-1, 1}, {1, 1}, {1, -1}, {-1, -1}};
            for (int[] d : diagDirs) {
                Node n = getNode(r + d[0], c + d[1]);
                if (n != null && !n.isWall()) {
                    // Prevent cutting through corners if we want strict grid (optional but standard)
                    // We will allow it simply here unless both adjacents are walls
                    Node n1 = getNode(r + d[0], c);
                    Node n2 = getNode(r, c + d[1]);
                    if ((n1 != null && !n1.isWall()) || (n2 != null && !n2.isWall())) {
                         neighbors.add(n);
                    }
                }
            }
        }

        return neighbors;
    }

    public void resetPaths() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                nodes[r][c].reset();
            }
        }
    }

    public void fullReset() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                nodes[r][c].fullReset();
            }
        }
    }
}
