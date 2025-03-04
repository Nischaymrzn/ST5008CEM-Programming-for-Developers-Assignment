package question3b.tetris;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SidePanel extends JPanel {
    private boolean paused = false;
    private boolean started = false;
    private boolean gameOver = false;

    private final JPanel scorePanel;
    private final JLabel levelDisplay;
    private final JLabel scoreDisplay;
    private final ModernButton startButton;
    private final ModernButton pauseButton;
    private final JPanel controlPanel;
    private final ModernButton leftButton;
    private final ModernButton rightButton;
    private final ModernButton rotateButton;
    private final JPanel nextShapePanel;
    
    private Board board;
    private int score = 0;
    private int level = 1;
    private Shapes nextShape;

    public SidePanel() {
        setBackground(ThemeColors.BACKGROUND);
        setPreferredSize(new Dimension(200, 600));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        scorePanel = createPanel("Game Stats");
        scorePanel.setLayout(new GridLayout(2, 2, 10, 10));
        
        levelDisplay = createLCDLabel("1");
        scoreDisplay = createLCDLabel("0");
        
        JLabel levelLabel = createLabel("LEVEL");
        JLabel scoreLabel = createLabel("SCORE");
        
        scorePanel.add(levelLabel);
        scorePanel.add(scoreLabel);
        scorePanel.add(levelDisplay);
        scorePanel.add(scoreDisplay);

        // Create nextShapePanel as an anonymous subclass that handles its own painting.
        nextShapePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (nextShape != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    int blockSize = 20;
                    // Center the drawing within this panel
                    int startX = (getWidth() - blockSize * 4) / 2;
                    int startY = (getHeight() - blockSize * 4) / 2;
                    
                    int[] x = nextShape.getX();
                    int[] y = nextShape.getY();
                    g2.setColor(nextShape.getColor());
                    
                    for (int i = 0; i < 4; i++) {
                        int drawX = startX + x[i] * blockSize;
                        int drawY = startY + y[i] * blockSize;
                        g2.fill(new RoundRectangle2D.Float(drawX, drawY, blockSize - 2, blockSize - 2, 6, 6));
                    }
                    g2.dispose();
                }
            }
        };
        nextShapePanel.setPreferredSize(new Dimension(160, 160));
        
        startButton = new ModernButton("START", ThemeColors.PRIMARY, 10);
        pauseButton = new ModernButton("PAUSE", ThemeColors.SURFACE_LIGHT, 10);
        
        // Increased horizontal gap between buttons (20 instead of 10)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);

        controlPanel = createPanel("Controls");
        controlPanel.setLayout(new GridLayout(2, 2, 10, 10));
        
        leftButton = new ModernButton("←", ThemeColors.SURFACE_LIGHT, 10);
        rightButton = new ModernButton("→", ThemeColors.SURFACE_LIGHT, 10);
        rotateButton = new ModernButton("↻", ThemeColors.SURFACE_LIGHT, 10);
        
        controlPanel.add(leftButton);
        controlPanel.add(rightButton);
        controlPanel.add(rotateButton);

        add(scorePanel);
        add(Box.createVerticalStrut(20));
        add(nextShapePanel);
        add(Box.createVerticalStrut(20));
        add(buttonPanel);
        add(Box.createVerticalStrut(20));
        add(controlPanel);
        
        setupListeners();
    }

    private JPanel createPanel(String title) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ThemeColors.SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                g2.dispose();
            }
        };
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setOpaque(false);
        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(ThemeColors.TEXT_SECONDARY);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JLabel createLCDLabel(String text) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ThemeColors.SURFACE_LIGHT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        label.setForeground(ThemeColors.PRIMARY);
        label.setFont(new Font("Monospaced", Font.BOLD, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        label.setOpaque(false);
        return label;
    }

    private void setupListeners() {
        startButton.addActionListener(e -> startAction());
        pauseButton.addActionListener(e -> pauseAction());
        leftButton.addActionListener(e -> {
            if (board != null && !gameOver) {
                board.moveLeftAction();
                board.requestFocusInWindow();
            }
        });
        rightButton.addActionListener(e -> {
            if (board != null && !gameOver) {
                board.moveRightAction();
                board.requestFocusInWindow();
            }
        });
        rotateButton.addActionListener(e -> {
            if (board != null && !gameOver) {
                board.rotateAction();
                board.requestFocusInWindow();
            }
        });
    }

    public void setScore(int s) {
        score = s;
        scoreDisplay.setText(String.valueOf(s));
        int newLevel = (score / 1000) + 1;
        if (newLevel != level) {
            level = newLevel;
            levelDisplay.setText(String.valueOf(level));
        }
    }
    
    public int getLevel() {
        return level;
    }

    public void setBoard(Board b) {
        board = b;
    }

    public void setGameStarted(boolean state) {
        started = state;
        startButton.setText(state ? "RESET" : "START");
    }

    private void startAction() {
        if (!started) {
            board.startGame();
            startButton.setText("RESET");
            pauseButton.setText("PAUSE");
            paused = false;
            started = true;
        } else {
            board.resetGame();
            startButton.setText("START");
            pauseButton.setText("PAUSE");
            paused = false;
            started = false;
        }
        if(board != null) {
            board.requestFocusInWindow();
        }
    }

    private void pauseAction() {
        if (started && !gameOver) {
            paused = !paused;
            board.setAnimation(!paused);
            pauseButton.setText(paused ? "RESUME" : "PAUSE");
            if(board != null) {
                board.requestFocusInWindow();
            }
        }
    }

    public void updateNextShape(Shapes shape) {
        this.nextShape = shape;
        nextShapePanel.repaint();
    }

    public void setGameOver(boolean state) {
        gameOver = state;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}