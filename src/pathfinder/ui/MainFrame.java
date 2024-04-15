package pathfinder.ui;

import pathfinder.algo.*;
import pathfinder.model.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private Grid grid;
    private GridPanel gridPanel;
    private ControlPanel controlPanel;

    private Timer timer;
    private AlgorithmBase currentAlgo;
    private long startTimeMillis;

    public MainFrame() {
        super("A* Pathfinding Visualizer - O(b^d) vs Metrics");

        // Initialize 30x40 grid
        int rows = 30;
        int cols = 40;
        grid = new Grid(rows, cols);

        gridPanel = new GridPanel(grid, grid.getNode(15, 10), grid.getNode(15, 30));
        controlPanel = new ControlPanel();

        this.setLayout(new BorderLayout());
        this.add(controlPanel, BorderLayout.NORTH);
        this.add(gridPanel, BorderLayout.CENTER);

        setupListeners();
        setupTimer();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void setupTimer() {
        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentAlgo != null) {
                    boolean finished = currentAlgo.runStep();
                    gridPanel.repaint();
                    
                    if (finished) {
                        timer.stop();
                        controlPanel.setRunningState(false);
                        long timeTaken = System.currentTimeMillis() - startTimeMillis;
                        controlPanel.updateMetrics(currentAlgo.getNodesExplored(), currentAlgo.getPathLength(), timeTaken);
                    }
                }
            }
        });
    }

    private void setupListeners() {
        controlPanel.addStartListener(e -> {
            grid.resetPaths(); 
            grid.setDiagonalsEnabled(controlPanel.isDiagonalEnabled());
            
            String algoName = controlPanel.getSelectedAlgorithm();
            switch (algoName) {
                case "A* Algorithm":
                    currentAlgo = new AStarAlgorithm(grid, gridPanel.getStartNode(), gridPanel.getEndNode(), controlPanel.getSelectedHeuristic());
                    break;
                case "Dijkstra":
                    currentAlgo = new DijkstraAlgorithm(grid, gridPanel.getStartNode(), gridPanel.getEndNode());
                    break;
                case "BFS":
                    currentAlgo = new BFSAlgorithm(grid, gridPanel.getStartNode(), gridPanel.getEndNode());
                    break;
                case "DFS":
                    currentAlgo = new DFSAlgorithm(grid, gridPanel.getStartNode(), gridPanel.getEndNode());
                    break;
            }

            timer.setDelay(controlPanel.getSpeedDelay());
            
            controlPanel.setRunningState(true);
            controlPanel.updateMetrics(0, 0, 0);
            startTimeMillis = System.currentTimeMillis();
            timer.start();
        });

        controlPanel.addResetListener(e -> {
            if (timer.isRunning()) timer.stop();
            currentAlgo = null;
            grid.resetPaths();
            controlPanel.setRunningState(false);
            controlPanel.updateMetrics(0, 0, 0);
            gridPanel.repaint();
        });

        controlPanel.addClearWallsListener(e -> {
            if (timer.isRunning()) timer.stop();
            currentAlgo = null;
            grid.fullReset(); // clears walls too
            // Re-assert start / end node status
            gridPanel.getStartNode().setStart(true);
            gridPanel.getEndNode().setEnd(true);
            
            controlPanel.setRunningState(false);
            controlPanel.updateMetrics(0, 0, 0);
            gridPanel.repaint();
        });
    }
}
