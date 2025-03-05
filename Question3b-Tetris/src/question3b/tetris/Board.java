package question3b.tetris;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

public class Board extends JPanel {
    private static final int ROWS = 20;
    private static final int COLS = 10;
    private static final int CELL_SIZE = 30;
    
    // Stack representing the game board rows (index 0 is the top row)
    private final Stack<Color[]> boardStack;
    
    // Queue for storing the falling blocks
    private final Queue<Shapes> shapeQueue;
    
    private Shapes currentShape;
    private final Timer gameTimer;
    private final SidePanel sidePanel;
    private final MainPanel mainPanel;
    private int score = 0;
    private boolean isGameOver = false;
    
    public Board(SidePanel sidePanel, MainPanel mainPanel) {
        this.sidePanel = sidePanel;
        this.mainPanel = mainPanel;
        
        // Initialize the boardStack with empty rows
        boardStack = new Stack<>();
        for (int i = 0; i < ROWS; i++) {
            boardStack.add(new Color[COLS]);
        }
        
        // Initialize the shape queue and enqueue one random shape
        shapeQueue = new LinkedList<>();
        shapeQueue.add(Shapes.createRandomShape());
        
        setBackground(ThemeColors.GRID_BACKGROUND);
        setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE));
        setBorder(BorderFactory.createLineBorder(ThemeColors.SURFACE_LIGHT, 2, true));
        setFocusable(true);
        
        gameTimer = new Timer(300, e -> update());
        
        setupControls();
    }
    
    private void setupControls() {
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (currentShape != null && !isGameOver) {
                    switch (e.getKeyCode()) {
                        case java.awt.event.KeyEvent.VK_LEFT -> moveLeft();
                        case java.awt.event.KeyEvent.VK_RIGHT -> moveRight();
                        case java.awt.event.KeyEvent.VK_UP -> rotate();
                        case java.awt.event.KeyEvent.VK_DOWN -> moveDown();
                        case java.awt.event.KeyEvent.VK_SPACE -> dropToBottom();
                    }
                    repaint();
                }
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2d);
        drawPlacedBlocks(g2d);
        
        if (currentShape != null) {
            drawShape(g2d, currentShape);
        }
    }
    
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(ThemeColors.GRID_LINE);
        for (int i = 0; i <= COLS; i++) {
            int x = i * CELL_SIZE;
            g2d.drawLine(x, 0, x, ROWS * CELL_SIZE);
        }
        for (int i = 0; i <= ROWS; i++) {
            int y = i * CELL_SIZE;
            g2d.drawLine(0, y, COLS * CELL_SIZE, y);
        }
    }
    
    private void drawPlacedBlocks(Graphics2D g2d) {
        // Iterate through boardStack rows
        for (int row = 0; row < ROWS; row++) {
            Color[] rowData = boardStack.get(row);
            for (int col = 0; col < COLS; col++) {
                if (rowData[col] != null) {
                    drawBlock(g2d, col, row, rowData[col]);
                }
            }
        }
    }
    
    private void drawBlock(Graphics2D g2d, int x, int y, Color color) {
        int xPos = x * CELL_SIZE;
        int yPos = y * CELL_SIZE;
        g2d.setColor(color);
        g2d.fill(new RoundRectangle2D.Float(
            xPos + 1, yPos + 1,
            CELL_SIZE - 2, CELL_SIZE - 2,
            8, 8
        ));
        g2d.setColor(new Color(255, 255, 255, 30));
        g2d.fill(new RoundRectangle2D.Float(
            xPos + 3, yPos + 3,
            CELL_SIZE - 6, CELL_SIZE - 6,
            4, 4
        ));
    }
    
    private void drawShape(Graphics2D g2d, Shapes shape) {
        int[] pos = shape.getPosition();
        int[] xOffsets = shape.getX();
        int[] yOffsets = shape.getY();
        
        for (int i = 0; i < 4; i++) {
            int x = pos[0] + xOffsets[i];
            int y = pos[1] + yOffsets[i];
            if (y >= 0) {
                drawBlock(g2d, x, y, shape.getColor());
            }
        }
    }
    
    private void update() {
        if (currentShape == null) {
            spawnNewShape();
        } else {
            moveDown();
        }
        repaint();
    }
    
    private void spawnNewShape() {
        // Ensure the queue has at least one shape
        if (shapeQueue.isEmpty()) {
            shapeQueue.add(Shapes.createRandomShape());
        }
        currentShape = shapeQueue.poll();
        if (!isValidMove(currentShape)) {
            gameOver();
            return;
        }
        // Enqueue a new random shape for the future
        shapeQueue.add(Shapes.createRandomShape());
        // Update the preview in the side panel (next shape in queue)
        sidePanel.updateNextShape(shapeQueue.peek());
    }
    
    private boolean isValidMove(Shapes shape) {
        int[] pos = shape.getPosition();
        int[] xOffsets = shape.getX();
        int[] yOffsets = shape.getY();
        
        for (int i = 0; i < 4; i++) {
            int x = pos[0] + xOffsets[i];
            int y = pos[1] + yOffsets[i];
            
            if (x < 0 || x >= COLS || y >= ROWS) {
                return false;
            }
            if (y >= 0 && boardStack.get(y)[x] != null) {
                return false;
            }
        }
        return true;
    }
    
    private void moveLeft() {
        currentShape.moveLeft();
        if (!isValidMove(currentShape)) {
            currentShape.moveRight();
        }
    }
    
    private void moveRight() {
        currentShape.moveRight();
        if (!isValidMove(currentShape)) {
            currentShape.moveLeft();
        }
    }
    
    private void moveDown() {
        currentShape.moveDown();
        if (!isValidMove(currentShape)) {
            currentShape.moveUp();
            lockShape();
            clearLines();
            spawnNewShape();
        }
    }
    
    private void rotate() {
        currentShape.rotate();
        if (!isValidMove(currentShape)) {
            currentShape.rotateBack();
        }
    }
    
    private void dropToBottom() {
        while (isValidMove(currentShape)) {
            currentShape.moveDown();
        }
        currentShape.moveUp();
        lockShape();
        clearLines();
        spawnNewShape();
    }
    
    private void lockShape() {
        int[] pos = currentShape.getPosition();
        int[] xOffsets = currentShape.getX();
        int[] yOffsets = currentShape.getY();
        
        for (int i = 0; i < 4; i++) {
            int x = pos[0] + xOffsets[i];
            int y = pos[1] + yOffsets[i];
            if (y >= 0) {
                boardStack.get(y)[x] = currentShape.getColor();
            }
        }
    }
    
    private void clearLines() {
        // Iterate through each row; if a row is complete, remove it from the stack
        // and add a new empty row at the top.
        for (int row = 0; row < ROWS; row++) {
            if (isLineComplete(row)) {
                boardStack.remove(row);
                boardStack.add(0, new Color[COLS]);
                score += calculateScore(1);
                sidePanel.setScore(score);
                row--; // adjust index after removal
            }
        }
    }
    
    private boolean isLineComplete(int row) {
        Color[] rowData = boardStack.get(row);
        for (int col = 0; col < COLS; col++) {
            if (rowData[col] == null) {
                return false;
            }
        }
        return true;
    }
    
    private int calculateScore(int linesCleared) {
        return switch (linesCleared) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 500;
            case 4 -> 800;
            default -> 0;
        };
    }
    
    private void gameOver() {
        isGameOver = true;
        gameTimer.stop();
        sidePanel.setGameOver(true);
        mainPanel.showGameOverPanel(score);
    }
    
    public void startGame() {
        isGameOver = false;
        score = 0;
        // Clear and reinitialize the boardStack with empty rows
        boardStack.clear();
        for (int i = 0; i < ROWS; i++) {
            boardStack.add(new Color[COLS]);
        }
        // Also clear and reinitialize the shapeQueue
        shapeQueue.clear();
        shapeQueue.add(Shapes.createRandomShape());
        
        currentShape = null;
        spawnNewShape();
        gameTimer.start();
        sidePanel.setScore(score);
        requestFocusInWindow();
    }
    
    public void resetGame() {
        gameTimer.stop();
        isGameOver = false;
        score = 0;
        boardStack.clear();
        for (int i = 0; i < ROWS; i++) {
            boardStack.add(new Color[COLS]);
        }
        shapeQueue.clear();
        shapeQueue.add(Shapes.createRandomShape());
        
        currentShape = null;
        sidePanel.setScore(score);
        repaint();
        requestFocusInWindow();
    }
    
    public void setAnimation(boolean isAnimating) {
        if (isAnimating) {
            gameTimer.start();
        } else {
            gameTimer.stop();
        }
    }
    
    public void moveLeftAction() {
        moveLeft();
        repaint();
        requestFocusInWindow();
    }
    
    public void moveRightAction() {
        moveRight();
        repaint();
        requestFocusInWindow();
    }
    
    public void rotateAction() {
        rotate();
        repaint();
        requestFocusInWindow();
    }
}
