package pathfinder.model;

public class Node {
    private int row, col;
    
    // Status
    private boolean isStart = false;
    private boolean isEnd = false;
    private boolean isWall = false;
    private boolean isVisited = false;
    private boolean isPath = false;
    
    // Costs
    private int gCost = Integer.MAX_VALUE;
    private int hCost = 0;
    private Node parent = null;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public boolean isStart() { return isStart; }
    public void setStart(boolean isStart) { this.isStart = isStart; }

    public boolean isEnd() { return isEnd; }
    public void setEnd(boolean isEnd) { this.isEnd = isEnd; }

    public boolean isWall() { return isWall; }
    public void setWall(boolean isWall) { this.isWall = isWall; }

    public boolean isVisited() { return isVisited; }
    public void setVisited(boolean isVisited) { this.isVisited = isVisited; }

    public boolean isPath() { return isPath; }
    public void setPath(boolean isPath) { this.isPath = isPath; }

    public int getGCost() { return gCost; }
    public void setGCost(int gCost) { this.gCost = gCost; }

    public int getHCost() { return hCost; }
    public void setHCost(int hCost) { this.hCost = hCost; }

    public int getFCost() { return (gCost == Integer.MAX_VALUE ? Integer.MAX_VALUE : gCost + hCost); }

    public Node getParent() { return parent; }
    public void setParent(Node parent) { this.parent = parent; }

    public void reset() {
        isVisited = false;
        isPath = false;
        gCost = Integer.MAX_VALUE;
        hCost = 0;
        parent = null;
    }
    
    public void fullReset() {
        reset();
        isStart = false;
        isEnd = false;
        isWall = false;
    }
}
