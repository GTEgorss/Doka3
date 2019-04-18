import java.awt.*;

public class Ball {
    double x;
    double y;
    double radius = 10;
    Color color;
    double speedX = 0;
    double speedY = 0;

    double gravityAcceleration = 0.3;
    double gravityX = 0.0;
    double gravityY = 0.3;

    Angle angle = new Angle(Math.PI / 2);

    boolean collision = false;

    int count = 0;

    public Ball(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));
    }

    public void gravity() {
        speedY += gravityY;
        speedX += gravityX;
        y += speedY;
        x += speedX;
    }

    public void updateGravity(double µ) {
        double acceleration = gravityAcceleration * (Angle.format(Math.sin(angle.angle)) - µ * Angle.format(Math.cos(angle.angle))); //Follows from Newton's second law
        if (angle.angle == 0 || angle.angle == Math.PI)
            acceleration = 0; //Not to let it be negative only because of friction force
        double cos = Angle.format(Math.cos(angle.angle));
        double sin = Angle.format(Math.sin(angle.angle));
        gravityX = acceleration * cos; //Projection on the x-axis
        gravityY = acceleration * sin; //Projection on the y-axis
        angle.angle = Math.PI / 2;
    }

    public boolean collisionBallBall(Ball ball) {
        double distance = (this.x - ball.x) * (this.x - ball.x) + (this.y - ball.y) * (this.y - ball.y);  //Pythagorean Theorem
        if (distance < (this.radius + ball.radius) * (this.radius + ball.radius)) {
            this.speedX = 0;
            ball.speedX = 0;
            this.speedY = 0;
            ball.speedY = 0;
            this.gravityAcceleration = 0;
            ball.gravityAcceleration = 0;
            collision = true;
        }
        return collision;
    }

    public double distanceBallLine(Line line) {
        double x0 = line.x0;
        double y0 = line.y0;
        double x1 = line.x1;
        double y1 = line.y1;
        //A, B - endpoints of the line; C - center of the circle
        double a = Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1)); //Pythagorean theorem for AB
        double b = Math.sqrt((x0 - x) * (x0 - x) + (y0 - y) * (y0 - y)); //Pythagorean theorem for AC
        double c = Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1)); //Pythagorean theorem for BC

        double cosAB = (a * a + b * b - c * c) / (2 * a * b); //Law of cosines for a^b
        double cosAC = (a * a - b * b + c * c) / (2 * a * c); //Law of cosines for a^c

        double distance;
        double halfPerimeter = (a + b + c) / 2;
        double area = Math.sqrt(halfPerimeter * (halfPerimeter - a) * (halfPerimeter - b) * (halfPerimeter - c)); //Heron's theorem for the triangle ABC

        if (cosAB >= 0 && cosAC >= 0) { //Acute triangle or right triangle
            distance = (2 * area / a); //Formula of the area of the triangle
        } else {  //Obtuse triangle
            if (cosAB < 0) { //b^a > 90º
                distance = b;
            } else { //b^c > 90º
                distance = c;
            }
        }
        return distance; //That is real distance, but I need the length of the perpendicular, or height
    }

    public void collisionBallLine(Line line, int mode) { //TODO TODO TODO
        double x0 = line.x0;
        double y0 = line.y0;
        double x1 = line.x1;
        double y1 = line.y1;
        int stroke = (int) line.stroke;

        if (((x - speedX - x0) * (y1 - y0) - (y - speedY - y0) * (x1 - x0)) * (x1 - x0) >= 0) {
            mode = 1;
        } else {
            mode = 2;
        }

        // ((x - x0) * (y1 - y0) - (y - y0) * (x1 - x0)) * (x1 - x0)

        if (stroke == 1) {
            if (mode == 1) {
                if (this.distanceBallLine(line) < radius) {
                    angle.angle = line.angle.angle;
                    if (x0 == x1) {
                        if (speedX >= 0) {
                            double deltaDistance = (x + radius) - line.x0 + line.prevStroke / 2 + 1;
                            x -= deltaDistance;
                        } else {
                            double deltaDistance = line.x0 - (x - radius) + line.prevStroke / 2 + 1;
                            x += deltaDistance;
                        }
                        speedX = 0;
                    } else {
                        Vector vectorSpeed = new Vector(speedX, speedY);
                        Vector vectorLine = new Vector(line);
                        double cos = vectorSpeed.cosOfAngleBetweenVectors(vectorLine);
                        Angle bufAngle = new Angle(Math.acos(cos >= -1 && cos <= 1 ? cos : Math.signum(cos)));

                        double speedSum = Math.sqrt(speedX * speedX + speedY * speedY) * Math.cos(bufAngle.angle); //Pythagorean theorem for triangle of speeds
                        speedX = speedSum * Angle.format(Math.cos(angle.angle)); //Projection on the x-axis
                        speedY = speedSum * Angle.format(Math.sin(angle.angle)); //Projection on the y-axis

                        //The distance for which the ball was able to dive in the line
                        double deltaDistance = radius - this.distanceBallLine(new Line(x0, y0, x1, y1, 1, Color.white)) + 1;
                        y -= Math.abs(deltaDistance * Math.cos(angle.angle));
                        x += deltaDistance * Math.sin(angle.angle) * Math.signum(x1 - x0);
                    }
                }
            }
            if (mode == 2) {
                if (this.distanceBallLine(line) < radius) {
                    angle.angle = line.angle.angle;
                    Vector vectorSpeed = new Vector(speedX, speedY);
                    Vector vectorLine = new Vector(x1 - x0, Math.max(line.y0, line.y1) - Math.min(line.y0, line.y1));
                    double cos = vectorSpeed.cosOfAngleBetweenVectors(vectorLine);
                    Angle bufAngle = new Angle(Math.acos(cos >= -1 && cos <= 1 ? cos : Math.signum(cos)));

                    if (bufAngle.angle <= Math.PI / 2) {
                        double speedSum = speedY; //Pythagorean theorem for triangle of speeds
                        speedX = speedSum * Angle.format(Math.cos(angle.angle + bufAngle.angle)); //Projection on the x-axis
                        speedY = speedSum * Angle.format(Math.sin(angle.angle + bufAngle.angle)); //Projection on the y-axis
                    } else {
                        double speedSum = speedY; //Math.sqrt(speedX * speedX + speedY * speedY); //Pythagorean theorem for triangle of speeds
                        speedX = speedSum * Angle.format(Math.cos(bufAngle.angle - angle.angle)); //Projection on the x-axis
                        speedY = speedSum * Angle.format(Math.sin(bufAngle.angle - angle.angle)); //Projection on the y-axis
                    }

                    double deltaDistance = radius - this.distanceBallLine(line) + 1;
                    y += Math.abs(deltaDistance * Math.cos(angle.angle));
                    x -= deltaDistance * Math.sin(angle.angle) * Math.signum(x1 - x0);
                }
            }
        } else {
            for (int i = 0; i < 4; ++i) {
                collisionBallLine(line.getOutline(i), 0);
            }
        }
    }

    public void collisionBallObstacle(Obstacle obstacle) { //TODO
        if (obstacle.rectangle) {
            for (int i = 0; i < 4; ++i) {
                collisionBallLine(((Rectangle) obstacle).getLine(i), 0);
            }
        }
        if (!obstacle.rectangle) {
            for (int i = 0; i < 3; ++i) {
                collisionBallLine(((Triangle) obstacle).getLine(i), 0);
            }
        }
    }

    public void collisionBallDrawing(Drawing drawing) {
        int stroke = drawing.stroke;
        for (int i = 0; i < drawing.lines.size(); ++i) { //Getting each curve segment as a line
            this.collisionBallLine(drawing.lines.get(i), 0);
        }
    }

    public boolean checkDot(double x, double y) {
        double squaredDistanceCenterDot = (this.x - x) * (this.x - x) + (this.y - y) * (this.y - y);
        return squaredDistanceCenterDot < radius * radius;
    }
}
