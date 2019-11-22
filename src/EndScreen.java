import java.awt.*;
import java.awt.event.KeyEvent;

public class EndScreen extends Screen {
    int score;

    public EndScreen(MainPanel mp, int s) {
        mainPanel = mp;
        score = s;
	}

	@Override
    void update() {

    }

    @Override
    void draw(Graphics g) {
        g.setColor(new Color(15, 44, 64));
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        String endInfo = "Game is Over!\n" + "Your score: " + score;
        String[] lines = endInfo.split("\n");
        int y = 40;
        g.setColor(new Color(127, 170, 199));
        g.setFont(g.getFont().deriveFont(Font.BOLD, 30f));
        for (String line : lines) {
            g.drawString(line, 10, y);
            y += g.getFontMetrics().getHeight();
        }
    }

    @Override
    void keyPressed(KeyEvent e) {
        super.keyPressed(e);
    }
}