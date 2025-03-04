package question3b.tetris;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FrontPagePanel extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color BUTTON_COLOR = new Color(52, 58, 64);
    private static final Color EXIT_BUTTON_COLOR = new Color(220, 53, 69);
    private static final Color TEXT_COLOR = new Color(33, 37, 41);
    
    private JButton startButton;
    private JButton exitButton;
    private BufferedImage logoImage;
    
    public FrontPagePanel() {
        setBackground(BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(50, 40, 50, 40));
        
        try {
            logoImage = ImageIO.read(getClass().getResource("./tetris.png"));
        } catch (IOException e) {
            System.out.println("Could not load logo image, using fallback");
        }
        
        JPanel logoPanel = createLogoPanel();
        
        JLabel titleLabel = new JLabel("TETRIS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        startButton = createStyledButton("PLAY", BUTTON_COLOR);
        exitButton = createStyledButton("EXIT", EXIT_BUTTON_COLOR);
        
        exitButton.addActionListener(e -> System.exit(0));
        
        add(Box.createVerticalGlue());
        add(logoPanel);
        add(Box.createVerticalStrut(30));
        add(titleLabel);
        add(Box.createVerticalStrut(50));
        add(startButton);
        add(Box.createVerticalStrut(15));
        add(exitButton);
        add(Box.createVerticalGlue());
    }
    
    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (logoImage != null) {
                    int x = (getWidth() - logoImage.getWidth()) / 2;
                    int y = (getHeight() - logoImage.getHeight()) / 2;
                    g2d.drawImage(logoImage, x, y, null);
                } else {
                    drawTetrisBlocks(g2d);
                }
            }
        };
        logoPanel.setPreferredSize(new Dimension(200, 120));
        logoPanel.setBackground(BACKGROUND_COLOR);
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return logoPanel;
    }
    
    private void drawTetrisBlocks(Graphics2D g2d) {
        int blockSize = 20;
        int centerX = getWidth() / 2 - (blockSize * 2);
        int centerY = getHeight() / 2 - (blockSize * 2);
        
        g2d.setColor(new Color(255, 193, 7));
        drawBlock(g2d, centerX, centerY, blockSize);
        drawBlock(g2d, centerX, centerY + blockSize, blockSize);
        drawBlock(g2d, centerX + blockSize, centerY + blockSize, blockSize);
        
        g2d.setColor(new Color(220, 53, 69));
        drawBlock(g2d, centerX + blockSize * 2, centerY, blockSize);
        drawBlock(g2d, centerX + blockSize * 3, centerY, blockSize);
        drawBlock(g2d, centerX + blockSize * 3, centerY + blockSize, blockSize);
    }
    
    private void drawBlock(Graphics2D g2d, int x, int y, int size) {
        g2d.fill(new RoundRectangle2D.Float(x, y, size - 2, size - 2, 4, 4));
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
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
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        
        return button;
    }
    
    public void addStartListener(java.awt.event.ActionListener listener) {
        startButton.addActionListener(listener);
    }
}