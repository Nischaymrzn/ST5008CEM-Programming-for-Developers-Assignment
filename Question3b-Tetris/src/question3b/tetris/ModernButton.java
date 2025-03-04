package question3b.tetris;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class ModernButton extends JButton {
    private Color backgroundColor;
    private final Color defaultColor;
    private Color hoverColor;
    private Color pressedColor;
    private final int radius;

    public ModernButton(String text, Color bgColor, int radius) {
        super(text);
        this.defaultColor = bgColor;
        this.backgroundColor = bgColor;
        this.hoverColor = bgColor.brighter();
        this.pressedColor = bgColor.darker();
        this.radius = radius;
        
        setupButton();
    }

    private void setupButton() {
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setForeground(ThemeColors.TEXT_PRIMARY);
        setFont(new Font("Arial", Font.BOLD, 14));
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backgroundColor = hoverColor;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backgroundColor = defaultColor;
                repaint();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                backgroundColor = pressedColor;
                repaint();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                backgroundColor = hoverColor;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(backgroundColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
        FontMetrics metrics = g2.getFontMetrics(getFont());
        Rectangle stringBounds = metrics.getStringBounds(getText(), g2).getBounds();
        int x = (getWidth() - stringBounds.width) / 2;
        int y = (getHeight() - stringBounds.height) / 2 + metrics.getAscent();
        g2.setColor(getForeground());
        g2.setFont(getFont());
        g2.drawString(getText(), x, y);
        g2.dispose();
    }
}
