package question3b.tetris;

import java.awt.Color;
import java.util.Random;

public class Shapes {

    private static final int[][][] LShapeCoords = {
        {{1, 0, 0, 0}, {2, 2, 1, 0}},
        {{0, 1, 2, 2}, {1, 1, 1, 0}},
        {{1, 1, 1, 0}, {2, 1, 0, 0}},
        {{0, 0, 1, 2}, {1, 0, 0, 0}}
    };

    private static final int[][][] SShapeCoords = {
        {{0, 1, 1, 2}, {1, 1, 0, 0}},
        {{1, 1, 0, 0}, {2, 1, 1, 0}},
        {{0, 1, 1, 2}, {1, 1, 0, 0}},
        {{1, 1, 0, 0}, {2, 1, 1, 0}}
    };

    private static final int[][][] ZShapeCoords = {
        {{2, 1, 1, 0}, {1, 1, 0, 0}},
        {{1, 1, 0, 0}, {0, 1, 1, 2}},
        {{2, 1, 1, 0}, {1, 1, 0, 0}},
        {{1, 1, 0, 0}, {0, 1, 1, 2}}
    };

    private static final int[][][] RevLShapeCoords = {
        {{0, 1, 1, 1}, {2, 2, 1, 0}},
        {{2, 2, 1, 0}, {1, 0, 0, 0}},
        {{0, 0, 0, 1}, {2, 1, 0, 0}},
        {{2, 1, 0, 0}, {1, 1, 1, 0}}
    };

    private static final int[][][] TShapeCoords = {
        {{1, 2, 1, 0}, {1, 0, 0, 0}},
        {{0, 0, 0, 1}, {2, 1, 0, 1}},
        {{2, 1, 0, 1}, {1, 1, 1, 0}},
        {{1, 1, 1, 0}, {2, 1, 0, 1}}
    };

    private static final int[][][] SquareShapeCoords = {
        {{1, 0, 1, 0}, {1, 1, 0, 0}},
        {{1, 0, 1, 0}, {1, 1, 0, 0}},
        {{1, 0, 1, 0}, {1, 1, 0, 0}},
        {{1, 0, 1, 0}, {1, 1, 0, 0}}
    };

    private static final int[][][] IShapeCoords = {
        {{0, 0, 0, 0}, {3, 2, 1, 0}},
        {{0, 1, 2, 3}, {0, 0, 0, 0}},
        {{0, 0, 0, 0}, {3, 2, 1, 0}},
        {{0, 1, 2, 3}, {0, 0, 0, 0}}
    };

    // Starting position (column, row)
    private final int[] shapePosition = {4, 0};

    private final String shape;
    private int angle;
    private final Color color;

    public Shapes(String shape, int angle) {
        this.shape = shape;
        this.angle = angle;
        this.color = getRandomColor();
    }

    private static Color getRandomColor() {
        Color[] colors = {
            new Color(102, 187, 106),  
            new Color(144, 202, 249),  
            new Color(255, 183, 77),   
            new Color(240, 98, 146),   
            new Color(171, 71, 188),   
            new Color(229, 115, 115),  
            new Color(149, 117, 205)   
        };
        return colors[new Random().nextInt(colors.length)];
    }

    public static Shapes createRandomShape() {
        Random rand = new Random();
        String[] shapeTypes = {"L", "RL", "S", "Z", "T", "SQ", "I"};
        int idx = rand.nextInt(shapeTypes.length);
        String randomShape = shapeTypes[idx];
        int randomAngle = rand.nextInt(4);
        return new Shapes(randomShape, randomAngle);
    }

    public Color getColor() {
        return color;
    }

    public int[] getPosition() {
        return shapePosition;
    }

    // Movement methods (collision detection is handled in Board)
    public void moveDown() {
        shapePosition[1]++;
    }
    
    public void moveUp() {
        shapePosition[1]--;
    }
    
    public void moveLeft() {
        shapePosition[0]--;
    }
    
    public void moveRight() {
        shapePosition[0]++;
    }
    
    public void rotate() {
        angle = (angle + 1) % 4;
    }
    
    public void rotateBack() {
        angle = (angle + 3) % 4;
    }

    public int getAngle() {
        return angle;
    }

    public int[] getX() {
        return switch (shape) {
            case "L" -> LShapeCoords[angle][0];
            case "RL" -> RevLShapeCoords[angle][0];
            case "S" -> SShapeCoords[angle][0];
            case "Z" -> ZShapeCoords[angle][0];
            case "T" -> TShapeCoords[angle][0];
            case "SQ" -> SquareShapeCoords[angle][0];
            case "I" -> IShapeCoords[angle][0];
            default -> null;
        };
    }

    public int[] getY() {
        return switch (shape) {
            case "L" -> LShapeCoords[angle][1];
            case "RL" -> RevLShapeCoords[angle][1];
            case "S" -> SShapeCoords[angle][1];
            case "Z" -> ZShapeCoords[angle][1];
            case "T" -> TShapeCoords[angle][1];
            case "SQ" -> SquareShapeCoords[angle][1];
            case "I" -> IShapeCoords[angle][1];
            default -> null;
        };
    }
}
