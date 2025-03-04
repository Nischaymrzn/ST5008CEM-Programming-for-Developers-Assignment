package question3b.tetris;

import java.awt.Color;

public class ThemeColors {
    // Main theme colors
    public static final Color BACKGROUND = new Color(18, 18, 18);
    public static final Color SURFACE = new Color(30, 30, 30);
    public static final Color SURFACE_LIGHT = new Color(45, 45, 45);
    public static final Color PRIMARY = new Color(0, 200, 83);
    public static final Color PRIMARY_DARK = new Color(0, 170, 70);
    public static final Color ERROR = new Color(255, 69, 58);
    public static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    public static final Color TEXT_SECONDARY = new Color(170, 170, 170);
    
    // Game colors
    public static final Color[] BLOCK_COLORS = {
        new Color(102, 187, 106),  // Muted green
        new Color(144, 202, 249),  // Muted blue
        new Color(255, 183, 77),   // Muted orange
        new Color(240, 98, 146),   // Muted pink
        new Color(171, 71, 188),   // Muted purple
        new Color(229, 115, 115),  // Muted red
        new Color(149, 117, 205)   // Muted violet
    };
    
    // Grid colors
    public static final Color GRID_LINE = new Color(45, 45, 45);
    public static final Color GRID_BACKGROUND = new Color(25, 25, 25);
}