package pathfinder;

import pathfinder.ui.MainFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Run UI safely on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}
