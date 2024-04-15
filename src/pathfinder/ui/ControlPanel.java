package pathfinder.ui;

import pathfinder.algo.AStarAlgorithm.HeuristicType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {
    private JComboBox<String> algoCombo;
    private JComboBox<HeuristicType> heuristicCombo;
    private JCheckBox diagonalCheck;
    private JSlider speedSlider;
    private JButton startBtn;
    private JButton resetBtn;
    private JButton clearWallsBtn;

    private JLabel metricsLabel;

    public ControlPanel() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        this.setBackground(Color.LIGHT_GRAY);

        // Algorithm Selector
        algoCombo = new JComboBox<>(new String[]{"A* Algorithm", "Dijkstra", "BFS", "DFS"});
        this.add(new JLabel("Algorithm:"));
        this.add(algoCombo);

        // Heuristic Selector (Only for A*)
        heuristicCombo = new JComboBox<>(HeuristicType.values());
        this.add(new JLabel("Heuristic (A*):"));
        this.add(heuristicCombo);

        // Diagonal
        diagonalCheck = new JCheckBox("Allow Diagonals");
        diagonalCheck.setBackground(Color.LIGHT_GRAY);
        this.add(diagonalCheck);

        // Speed Slider
        speedSlider = new JSlider(1, 100, 50);
        this.add(new JLabel("Speed:"));
        this.add(speedSlider);

        // Buttons
        startBtn = new JButton("Start");
        resetBtn = new JButton("Clear Path");
        clearWallsBtn = new JButton("Clear Board");
        
        this.add(startBtn);
        this.add(resetBtn);
        this.add(clearWallsBtn);

        // Metrics
        metricsLabel = new JLabel("Nodes Explored: 0  |  Path Length: 0  |  Time: 0ms");
        metricsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        this.add(metricsLabel);
    }

    public String getSelectedAlgorithm() {
        return (String) algoCombo.getSelectedItem();
    }

    public HeuristicType getSelectedHeuristic() {
        return (HeuristicType) heuristicCombo.getSelectedItem();
    }

    public boolean isDiagonalEnabled() {
        return diagonalCheck.isSelected();
    }

    public int getSpeedDelay() {
        // slider 1 (fast) to 100 (slow)
        // map 1 -> 100ms, 100 -> 1ms
        return 101 - speedSlider.getValue();
    }

    public void updateMetrics(int explored, int length, long time) {
        metricsLabel.setText(String.format("Nodes Explored: %d  |  Path Length: %d  |  Time: %dms", explored, length, time));
    }

    public void setRunningState(boolean isRunning) {
        algoCombo.setEnabled(!isRunning);
        heuristicCombo.setEnabled(!isRunning);
        diagonalCheck.setEnabled(!isRunning);
        startBtn.setText(isRunning ? "Running..." : "Start");
        startBtn.setEnabled(!isRunning);
    }

    public void addStartListener(ActionListener l) { startBtn.addActionListener(l); }
    public void addResetListener(ActionListener l) { resetBtn.addActionListener(l); }
    public void addClearWallsListener(ActionListener l) { clearWallsBtn.addActionListener(l); }
}
