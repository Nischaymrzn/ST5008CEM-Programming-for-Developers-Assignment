package question3b.tetris;

import java.awt.*;
import javax.swing.*;

public class MainPanel extends JPanel {
    private CardLayout cardLayout;
    private FrontPagePanel frontPage;
    private JPanel gamePanel;
    private GameOverPanel gameOverPanel;
    private Board board;
    private SidePanel sidePanel;
    
    public MainPanel() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        
        frontPage = new FrontPagePanel();
        frontPage.addStartListener(e -> startGameFromFrontPage());
        add(frontPage, "front");
        
        gamePanel = new JPanel(new BorderLayout());
        sidePanel = new SidePanel();
        board = new Board(sidePanel, this);
        sidePanel.setBoard(board);
        gamePanel.add(board, BorderLayout.CENTER);
        gamePanel.add(sidePanel, BorderLayout.EAST);
        add(gamePanel, "game");
        
        gameOverPanel = new GameOverPanel(0);
        gameOverPanel.addRestartListener(e -> restartGame());
        add(gameOverPanel, "gameOver");
        
        cardLayout.show(this, "front");
    }
    
    private void startGameFromFrontPage() {
        cardLayout.show(this, "game");
        board.startGame();
        sidePanel.setGameStarted(true);
    }
    
    public void showGameOverPanel(int score) {
        remove(gameOverPanel);
        gameOverPanel = new GameOverPanel(score);
        gameOverPanel.addRestartListener(e -> restartGame());
        add(gameOverPanel, "gameOver");
        cardLayout.show(this, "gameOver");
        revalidate();
        repaint();
    }
    
    public void restartGame() {
        cardLayout.show(this, "game");
        board.resetGame();
        board.startGame();
        sidePanel.setGameStarted(true);
    }
}