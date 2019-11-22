import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class Screen {
    protected MainPanel mainPanel;

    abstract void update();
    abstract void draw(Graphics g);
    void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_1:
            case KeyEvent.VK_2:
            case KeyEvent.VK_3:
                mainPanel.switchScreen(new GameScreen(mainPanel, e.getKeyCode() - KeyEvent.VK_1));
                break;
            case KeyEvent.VK_R:
                mainPanel.switchScreen(new StartScreen(mainPanel));
                break;
            default:
                break;
        }
    }
}