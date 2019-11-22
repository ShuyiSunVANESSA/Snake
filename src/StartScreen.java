import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.*;

public class StartScreen extends Screen {

    public StartScreen(MainPanel mainPanel2) {
        mainPanel = mainPanel2;
	}

	@Override
    void update() {
    }

    @Override
    void draw(Graphics g) {
        g.setColor(new Color(104, 161, 120));
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        String startUpInfo = Constants.NAME + '\n'
            + Constants.USER_ID + '\n'
            + Constants.DESCRIPTION;
        String[] lines = startUpInfo.split("\n");
        int y = 40;
        g.setColor(new Color(25, 74, 39));
        g.setFont(g.getFont().deriveFont(Font.BOLD, 30f));
        for (String line : lines) {
            g.drawString(line, 10, y);
            y += g.getFontMetrics().getHeight();
        }
    }

    @Override
    void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if (e.getKeyChar() == 's') {
            mainPanel.switchScreen(new GameScreen(mainPanel, 0));
        }
    }
}