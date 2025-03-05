package question3b.tetris;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class GameOverPanel extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color BUTTON_COLOR = new Color(34, 197, 94); // green-500
    private static final Color TEXT_COLOR = new Color(33, 37, 41);
    
    private JButton restartButton;
    
    public GameOverPanel(int score) {
        setBackground(BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));
        
        JLabel gameOverLabel = new JLabel("GAME OVER");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 48));
        gameOverLabel.setForeground(TEXT_COLOR);
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel scoreLabel = new JLabel(String.format("Score: %d", score));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 32));
        scoreLabel.setForeground(TEXT_COLOR);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        restartButton = createStyledButton("Play Again");
        
        add(Box.createVerticalGlue());
        add(gameOverLabel);
        add(Box.createVerticalStrut(30));
        add(scoreLabel);
        add(Box.createVerticalStrut(50));
        add(restartButton);
        add(Box.createVerticalGlue());
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        
        return button;
    }
    
    public void addRestartListener(java.awt.event.ActionListener listener) {
        restartButton.addActionListener(listener);
    }
}