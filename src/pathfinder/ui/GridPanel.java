package pathfinder.ui;

import pathfinder.model.Grid;
import pathfinder.model.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class GridPanel extends JPanel {
    private Grid grid;
    private Node startNode;
    private Node endNode;

    private int cellSize = 25;
    
    // To handle dragging type
    private enum DrawMode {
        DRAW_WALL, ERASE_WALL, MOVE_START, MOVE_END, NONE
    }
    
    private DrawMode currentDrawMode = DrawMode.NONE;

    public GridPanel(Grid grid, Node initialStart, Node initialEnd) {
        this.grid = grid;
        this.startNode = initialStart;
        this.endNode = initialEnd;

        this.startNode.setStart(true);
        this.endNode.setEnd(true);

        this.setPreferredSize(new Dimension(grid.getCols() * cellSize, grid.getRows() * cellSize));
        this.setBackground(Color.DARK_GRAY);

        setupMouseListeners();
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    private void setupMouseListeners() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Node clickedNode = getNodeFromCoords(e.getX(), e.getY());
                if (clickedNode == null) return;

                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (clickedNode == startNode) {
                        currentDrawMode = DrawMode.MOVE_START;
                    } else if (clickedNode == endNode) {
                        currentDrawMode = DrawMode.MOVE_END;
                    } else {
                        currentDrawMode = DrawMode.DRAW_WALL;
                        clickedNode.setWall(true);
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    currentDrawMode = DrawMode.ERASE_WALL;
                    if (clickedNode != startNode && clickedNode != endNode) {
                        clickedNode.setWall(false);
                    }
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentDrawMode = DrawMode.NONE;
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Node draggedNode = getNodeFromCoords(e.getX(), e.getY());
                if (draggedNode == null) return;

                switch (currentDrawMode) {
                    case MOVE_START:
                        if (draggedNode != endNode && !draggedNode.isWall()) {
                            startNode.setStart(false);
                            startNode = draggedNode;
                            startNode.setStart(true);
                        }
                        break;
                    case MOVE_END:
                        if (draggedNode != startNode && !draggedNode.isWall()) {
                            endNode.setEnd(false);
                            endNode = draggedNode;
                            endNode.setEnd(true);
                        }
                        break;
                    case DRAW_WALL:
                        if (draggedNode != startNode && draggedNode != endNode) {
                            draggedNode.setWall(true);
                        }
                        break;
                    case ERASE_WALL:
                        if (draggedNode != startNode && draggedNode != endNode) {
                            draggedNode.setWall(false);
                        }
                        break;
                    case NONE:
                        break;
                }
                repaint();
            }
        });
    }

    private Node getNodeFromCoords(int x, int y) {
        int c = x / cellSize;
        int r = y / cellSize;
        return grid.getNode(r, c);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                Node node = grid.getNode(r, c);
                
                if (node.isStart()) {
                    g.setColor(new Color(46, 204, 113)); // Green
                } else if (node.isEnd()) {
                    g.setColor(new Color(231, 76, 60)); // Red
                } else if (node.isWall()) {
                    g.setColor(new Color(52, 73, 94));  // Dark Blue / Wall color
                } else if (node.isPath()) {
                    g.setColor(new Color(241, 196, 15)); // Yellow
                } else if (node.isVisited()) {
                    g.setColor(new Color(52, 152, 219, 150)); // Light Blue
                } else {
                    g.setColor(Color.WHITE); // Default empty
                }

                g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                g.setColor(new Color(189, 195, 199)); // Border color
                g.drawRect(c * cellSize, r * cellSize, cellSize, cellSize);
            }
        }
    }
}
