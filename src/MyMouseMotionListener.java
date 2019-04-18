import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MyMouseMotionListener implements MouseMotionListener {

    GamePanel gamePanel;

    public MyMouseMotionListener(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseDragged(MouseEvent e) { //drawing curve by chain of lines
        Drawing bufDrawing = gamePanel.drawings.get(gamePanel.drawings.size() - 1);
        boolean firstVertex = false;
        if (bufDrawing.lastX < 0) {
            firstVertex = true;
        }

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

        if (gamePanel.allowed && !firstVertex) {
            for (Lattice lattice : gamePanel.lattices) {
                Line bufLine = new Line(bufDrawing.lastX, bufDrawing.lastY, e.getX(), e.getY(), gamePanel.stroke, gamePanel.color);
                for (int i = 0; i < 4; ++i) {
                    gamePanel.allowed = !bufLine.intersectionLineLine(lattice.getLine(i));
                    if (!gamePanel.allowed) break;
                }
            }
        }

        if (gamePanel.allowed && !firstVertex) {
            for (Obstacle obstacle : gamePanel.obstacles) {
                if (obstacle.rectangle) {
                    Line bufLine = new Line(bufDrawing.lastX, bufDrawing.lastY, e.getX(), e.getY(), gamePanel.stroke, gamePanel.color);
                    for (int i = 0; i < 4; ++i) {
                        gamePanel.allowed = !bufLine.intersectionLineLine(((Rectangle) obstacle).getLine(i));
                        if (!gamePanel.allowed) break;
                    }
                } else {
                    Line bufLine = new Line(bufDrawing.lastX, bufDrawing.lastY, e.getX(), e.getY(), gamePanel.stroke, gamePanel.color);
                    for (int i = 0; i < 3; ++i) {
                        gamePanel.allowed = !bufLine.intersectionLineLine(((Triangle) obstacle).getLine(i));
                        if (!gamePanel.allowed) break;
                    }
                }
                if (!gamePanel.allowed) break;
            }
        }

        if (gamePanel.allowed) {
            if (firstVertex) {
                bufDrawing.lastX = e.getX();
                bufDrawing.lastY = e.getY();
            } else {
                bufDrawing.lines.add(new Line(bufDrawing.lastX, bufDrawing.lastY, e.getX(), e.getY(), gamePanel.stroke, gamePanel.color));
                bufDrawing.lastX = e.getX();
                bufDrawing.lastY = e.getY();
                gamePanel.drawings.get(gamePanel.drawings.size() - 1).evaluateTheMass();
                gamePanel.drawings.get(gamePanel.drawings.size() - 1).centerOfTheMass();
            }
        }

        gamePanel.allowed = true;
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
