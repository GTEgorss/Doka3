import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyMouseListener implements MouseListener {

    GamePanel gamePanel;

    public MyMouseListener(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) { //creating a new line in the drawings array
        gamePanel.mousePressed = true;
        gamePanel.drawings.add(new Drawing(gamePanel.stroke, gamePanel.color));

        gamePanel.allowed = !gamePanel.ball[0].checkDot(e.getX(), e.getY()) &&
                !gamePanel.ball[1].checkDot(e.getX(), e.getY());

        if (gamePanel.allowed) {
            for (Lattice lattice : gamePanel.lattices) {
                gamePanel.allowed = gamePanel.allowed && !lattice.checkDot(e.getX(), e.getY());
            }
        }

        if (gamePanel.allowed) {
            for (Obstacle obstacle : gamePanel.obstacles) {
                if (obstacle.rectangle) {
                    gamePanel.allowed = gamePanel.allowed && !((Rectangle) obstacle).checkDot(e.getX(), e.getY());
                } else {
                    gamePanel.allowed = gamePanel.allowed && !((Triangle) obstacle).checkDot(e.getX(), e.getY());
                }
            }
        }

        if (gamePanel.allowed) {
            gamePanel.drawings.get(gamePanel.drawings.size() - 1).lastX = e.getX();
            gamePanel.drawings.get(gamePanel.drawings.size() - 1).lastY = e.getY();
        }

        gamePanel.allowed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        gamePanel.mousePressed = false;
        gamePanel.drawings.get(gamePanel.drawings.size() - 1).finished = true;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
