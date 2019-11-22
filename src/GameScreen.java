import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public class GameScreen extends Screen {
    List<List<Cell>> grid = new ArrayList<>();
    Deque<Point> snake = new ArrayDeque<>();
    Random rand = new Random(1);
    // direction:
    //  0: Up
    //  1: Right
    //  2: Down
    //  3: Left
    int direction = 0;
    int previousDirection = 0;
    int[] DELTA_X = {0, 1, 0, -1};
    int[] DELTA_Y = {-1, 0, 1, 0};
    int counter = 0;
    static final Level[] levels = {
        new Level(5, 5, 5, 30),
        new Level(10, 15, 4, 30),
        new Level(15, 20, 3, -1),
    };
    int curLevel;
    int score = 0;
    boolean isPaused = false;

    GameScreen(MainPanel mp, int levelNum) {
        mainPanel = mp;
        curLevel = levelNum;
        for (int i = 0; i < Constants.GRID_HEIGHT; i++) {
            List<Cell> row = new ArrayList<>(Collections.nCopies(Constants.GRID_WIDTH, Cell.GROUND));
            grid.add(row);
        }
        Level level = levels[curLevel];
        int fruitCount = rand.nextInt(level.fruitCountMax - level.fruitCountMin + 1) + level.fruitCountMin;
        for (int i = 0; i < fruitCount; i++) {
            generateProp(Cell.FRUIT);
        }
        for (int i = 0; i < Constants.OBSTACLE_COUNT; i++) {
            generateProp(Cell.OBSTACLE);
        }
        for (int i = 0; i < Constants.BORDER_WIDTH; i++) {
            grid.get(0).set(i, Cell.OBSTACLE);
            grid.get(0).set(Constants.GRID_WIDTH - i - 1, Cell.OBSTACLE);
            grid.get(Constants.GRID_HEIGHT - 1).set(i, Cell.OBSTACLE);
            grid.get(Constants.GRID_HEIGHT - 1).set(Constants.GRID_WIDTH - i - 1, Cell.OBSTACLE);
        }
        for (int i = 0; i < Constants.BORDER_HEIGHT; i++) {
            grid.get(i).set(0, Cell.OBSTACLE);
            grid.get(Constants.GRID_HEIGHT - i -1).set(0, Cell.OBSTACLE);
            grid.get(i).set(Constants.GRID_WIDTH - 1, Cell.OBSTACLE);
            grid.get(Constants.GRID_HEIGHT - i - 1).set(Constants.GRID_WIDTH - 1, Cell.OBSTACLE);
        }
        snake.add(new Point(Constants.GRID_WIDTH / 2, Constants.GRID_HEIGHT / 2));
    }

    private void generateProp(Cell c) {
        Point p = new Point(rand.nextInt(Constants.GRID_WIDTH), rand.nextInt(Constants.GRID_HEIGHT));
        for (Point s : snake) {
            if (p.equals(s)) {
                generateProp(c);
                return;
            }
        }
        if (grid.get(p.y).get(p.x) != Cell.GROUND) {
            generateProp(c);
            return;
        }
        grid.get(p.y).set(p.x, c);
    }

    @Override
    void update() {
        if (isPaused) {
            return;
        }
        counter += 1;
        if (counter % levels[curLevel].speed == 0) {
            Point p = snake.getFirst();
            Point newHead = new Point((p.x + DELTA_X[direction] + Constants.GRID_WIDTH) % Constants.GRID_WIDTH,
                    (p.y + DELTA_Y[direction] + Constants.GRID_HEIGHT) % Constants.GRID_HEIGHT);
            switch (grid.get(newHead.y).get(newHead.x)) {
            case FRUIT:
                grid.get(newHead.y).set(newHead.x, Cell.GROUND);
                generateProp(Cell.FRUIT);
                score += 1;
                break;
            case OBSTACLE:
                gameOver();
                break;
            case GROUND:
                snake.removeLast();
                break;
            default:
                break;
            }
            // snake die if hit itself
            for (Point s : snake) {
                if (s.equals(newHead)) {
                    gameOver();
                    break;
                }
            }
            snake.addFirst(newHead);
            previousDirection = direction;
        } 
        if (counter % Constants.OBSTACLE_PERIOD == 0) {
            generateProp(Cell.OBSTACLE);
        }
        if (levels[curLevel].timer != -1 && getCurTime() <= 0) {
            mainPanel.switchScreen(new GameScreen(mainPanel, curLevel + 1));
        }
    }

    private void gameOver() {
        mainPanel.switchScreen(new EndScreen(mainPanel, score));
    }

    @Override
    void draw(Graphics g) {
        g.setColor(new Color(58, 83, 138));
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        for (int y = 0; y < Constants.GRID_HEIGHT; y++) {
            for (int x = 0; x < Constants.GRID_WIDTH; x++) {
                Point pixel = indexToPixel(new Point(x, y));
                Point nextPixel = indexToPixel(new Point(x + 1, y + 1));
                switch (grid.get(y).get(x)) {
                case GROUND:
                    break;
                case FRUIT:
                    g.setColor(new Color(235, 213, 75));
                    g.fillRoundRect(pixel.x, pixel.y, nextPixel.x - pixel.x, nextPixel.y - pixel.y, 20,20);
                    break;
                case OBSTACLE:
                    g.setColor(new Color(2, 18, 41));
                    g.fillRect(pixel.x, pixel.y, nextPixel.x - pixel.x, nextPixel.y - pixel.y);
                    break;
                default:
                    assert false;
                }
            }
        }
        for (Point p : snake) {
            Point pixel = indexToPixel(p);
            Point nextPixel = indexToPixel(new Point(p.x + 1, p.y + 1));
            g.setColor(new Color(23, 227, 142));
            g.fillOval(pixel.x, pixel.y, nextPixel.x - pixel.x, nextPixel.y - pixel.y);
        }
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 30f));
        g.drawString("Level: " + (curLevel + 1), 20, 40);
        g.drawString("Score: " + score, 20, 70);
        if (levels[curLevel].timer != -1) {
            String time = Integer.toString(getCurTime());
            g.setFont(g.getFont().deriveFont(Font.BOLD, 50f));
            g.drawString(time, 20, 130);
        }
    }

    private int getCurTime() {
        return levels[curLevel].timer - (counter / Constants.FPS);
    }
    private static Point indexToPixel(Point p) {
        int px = p.x * Constants.SCREEN_WIDTH / Constants.GRID_WIDTH;
        int py = p.y * Constants.SCREEN_HEIGHT / Constants.GRID_HEIGHT;
        return new Point(px, py);
    }

    @Override
    void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        int keyCode = e.getKeyCode();
        int newDir = direction;
        switch (keyCode) {
            case KeyEvent.VK_RIGHT:
                newDir = (direction + 1) % 4;
                break;
            case KeyEvent.VK_LEFT:
                newDir = (direction + 3) % 4;
                break;
            case KeyEvent.VK_P:
                isPaused = !isPaused;
                break;
            case KeyEvent.VK_Q:
                gameOver();
                break;
            default:
                return;
        }
        if (newDir % 2 != previousDirection % 2) {
            direction = newDir;
        }
    }
}

enum Cell {
    GROUND,
    FRUIT,
    OBSTACLE,
}

class Level {
    public int fruitCountMin;
    public int fruitCountMax;
    public int speed;
    public int timer;

    public Level(int fMin, int fMax, int s, int t) {
        fruitCountMin = fMin;
        fruitCountMax = fMax;
        speed = s;
        timer = t;
    }
}