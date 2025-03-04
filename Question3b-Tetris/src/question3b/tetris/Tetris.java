package question3b.tetris;

import javax.swing.*;

public class Tetris {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            JFrame frame = new JFrame("Tetris");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            MainPanel mainPanel = new MainPanel();
            frame.setContentPane(mainPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setResizable(false);
        });
    }
}
