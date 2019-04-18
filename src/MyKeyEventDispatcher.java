import java.awt.*;
import java.awt.event.KeyEvent;

public class MyKeyEventDispatcher implements KeyEventDispatcher {

    GamePanel gamePanel;

    public MyKeyEventDispatcher(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                gamePanel.enterPressed = true;
            }
        }

        if (e.getID() == KeyEvent.KEY_RELEASED) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                gamePanel.enterPressed = false;
            }
        }

        if (e.getID() == KeyEvent.KEY_RELEASED) {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                gamePanel.ball[0] = new Ball(gamePanel.x1, gamePanel.y1, Color.pink);
                gamePanel.ball[1] = new Ball(gamePanel.x2, gamePanel.y2, Color.cyan);
                gamePanel.drawings.clear();
                gamePanel.drawings.add(new Drawing(gamePanel.stroke, Color.white));
                gamePanel.started = false;
            }
        }

        return false;
    }
}
