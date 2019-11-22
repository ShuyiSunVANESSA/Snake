import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

public class MainPanel extends JPanel {
    private Screen curScreen;

    public MainPanel() {
        setMinimumSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        setMaximumSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        Timer timer = new Timer(1000 / Constants.FPS, e -> {
            curScreen.update();
            repaint();
        });
        timer.start();
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                curScreen.keyPressed(e);
            }
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        curScreen.draw(g);
    }

	public void switchScreen(Screen toScreen) {
        curScreen = toScreen;
    }
}