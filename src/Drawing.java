import java.awt.*;
import java.util.ArrayList;

public class Drawing {
    ArrayList<Line> lines = new ArrayList<>();
    ArrayList<Double> speedX = new ArrayList<>();
    ArrayList<Double> speedY = new ArrayList<>();
    Color color;
    int stroke;

    double centerOfMassX;
    double centerOfMassY;
    double mass;

    double gravity = 0.3;

    double lastX = -600;
    double lastY = -600;

    boolean finished = false;

    Vector movingVector;

    public Drawing(int stroke, Color color) {
        this.color = color;
        this.stroke = stroke;
    }

    public void draw(Graphics g) {
        for (Line line : lines) {
            g.setColor(color);
            line.draw(g);
        }
        //g.drawString(mass + "", (int) centerOfMassX + 15, (int) centerOfMassY + 15);
        g.setColor(Color.green);
        //g.drawOval((int) centerOfMassX - 5, (int) centerOfMassY - 5, 10, 10);
    }

    public void gravity() {
        for (Line line : lines) {
            line.gravity(0, gravity);
        }
        this.centerOfTheMass();
    }

    public void evaluateTheMass() {
        for (Line line : lines) {
            mass += line.mass;
        }
    }

    public void centerOfTheMass() {
        ArrayList<Vector> vectors = new ArrayList<>();
        for (Line line : lines) {
            vectors.add(new Vector(line.centerOfMassX, line.centerOfMassY, line.mass));
        }
        Vector centerOfMassVector = Vector.sumOfManyVectors(vectors);
        centerOfMassX = centerOfMassVector.x;
        centerOfMassY = centerOfMassVector.y;
    }

    public boolean collisionDrawingObstacle(Obstacle obstacle) {
        boolean collision = false;
        if (!obstacle.rectangle) {
            for (Line line : lines) {
                for (int i = 0; i < 3; ++i) {
                    collision = line.intersectionLineLine(((Triangle) obstacle).getLine(i));
                    if (collision) break;
                }
                if (collision) break;
            }

            if (!collision) {
                for (Line line : lines) {
                    collision = ((Triangle) obstacle).checkDot(line.x0, line.y0);
                    if (collision) break;
                }
            }
            if (!collision) {
                if (lines.size() > 0 && ((Triangle) obstacle).checkDot(lines.get(lines.size() - 1).x1, lines.get(lines.size() - 1).y1)) {
                    collision = true;
                }
            }
        }

        if (obstacle.rectangle) {
            for (Line line : lines) {
                for (int i = 0; i < 4; ++i) {
                    collision = line.intersectionLineLine(((Rectangle) obstacle).getLine(i));
                    if (collision) break;
                }
                if (collision) break;
            }

            if (!collision) {
                for (Line line : lines) {
                    if (((Rectangle) obstacle).checkDot(line.x0, line.y0)) {
                        collision = true;
                    }
                }
            }
            if (!collision) {
                if (lines.size() > 0 && ((Rectangle) obstacle).checkDot(lines.get(lines.size() - 1).x1, lines.get(lines.size() - 1).y1)) {
                    collision = true;
                }
            }
        }
        return collision;
    }

    public void doCollisionDrawingObstacle() {
        movingVector = new Vector(-lines.get(0).speedX / 4, -lines.get(0).speedY / 4); //TODO speed of the line of the collision
        for (Line line1 : lines) {
            line1.speedX = 0;
            line1.speedY = 0;
            gravity = 0;
        }
    }

    public void moveDrawing(Vector vector) {
        for (Line line : lines) {
            line.x0 += vector.x;
            line.x1 += vector.x;
            line.y0 += vector.y;
            line.y1 += vector.y;

            if (line.stroke > 1) {
                for (int i = 0; i < 4; ++i) {
                    line.vertexes[i].x += vector.x;
                    line.vertexes[i].y += vector.y;
                }
            }
        }
    }

    public boolean collisionDrawingDrawing(Drawing drawing) {
        boolean intersection = false;
        for (Line line0 : lines) {
            for (Line line1 : drawing.lines) {
                intersection = line0.intersectionLineLine(line1);
                if (intersection) break;
            }
            if (intersection) break;
        }
        return intersection;
    }

    public void doCollisionDrawingDrawing(Drawing drawing) {
        if (lines.get(0).speedX != 0 || lines.get(0).speedY != 0)
            movingVector = new Vector(-lines.get(0).speedX / 4, -lines.get(0).speedY / 4); //TODO speed of the line of the collision
        else
            movingVector = new Vector(-drawing.lines.get(0).speedX / 4, -drawing.lines.get(0).speedY / 4); //TODO speed of the line of the collision
    }
}
