import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Line {
    double x0;
    double y0;
    double x1;
    double y1;
    double stroke;
    double prevStroke; //for BallLine collision
    double prevX0;
    Angle angle = new Angle(Math.PI / 2);
    double density; //TODO to choose

    Color color;

    double centerOfMassX;
    double centerOfMassY;
    double mass;

    double speedX = 0;
    double speedY = 0;
    double gravityX;
    double gravityY;
    double speedRotation;
    double accelerationRotation;

    Dot[] vertexes = new Dot[4];

    public Line(@NotNull Dot dot0, @NotNull Dot dot1, double stroke, Color color) {
        this(dot0.x, dot0.y, dot1.x, dot1.y, stroke, color);
    }

    public Line(double x0, double y0, double x1, double y1, double stroke, Color color) {
        this(x0, y0, x1, y1, stroke, color, 0, 0);
    }

    public Line(double x0, double y0, double x1, double y1, double stroke, Color color, double speedX, double speedY) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.stroke = stroke;
        prevStroke = stroke;
        this.color = color;
        prevX0 = x0;
        angle.angle = Math.atan2(y1 - y0, x1 - x0); //y = k * x + b => angle = atan(k);  //TODO I should get rid of atan and replace it with evaluating the angle via scalar product
        centerOfMassX = Math.abs(x1 + x0) / 2.0;
        centerOfMassY = Math.abs(y1 + y0) / 2.0;
        this.speedX = speedX;
        this.speedY = speedY;
        density = stroke;
        mass = this.getLength() * density;

        if (stroke > 1) {
            if (x0 == x1) {
                double a0 = x0;
                double b0 = Math.max(y0, y1); //ниже
                double a1 = x0;
                double b1 = Math.min(y0, y1); //выше
                vertexes[0] = new Dot(a0 - stroke / 2, b0); //Upper outline
                vertexes[1] = new Dot(a1 - stroke / 2, b1); //Upper outline
                vertexes[2] = new Dot(a1 + stroke / 2, b1); //Lower outline
                vertexes[3] = new Dot(a0 + stroke / 2, b0); //Lower outline

            } else {
                if (y0 == y1) {
                    double a0 = x0;
                    double b0 = y0; //ниже
                    double a1 = x1;
                    double b1 = y1; //выше

                    vertexes[0] = new Dot(a0, b0 - stroke / 2); //Upper outline
                    vertexes[1] = new Dot(a1, b1 - stroke / 2); //Upper outline
                    vertexes[2] = new Dot(a1, b1 + stroke / 2); //Lower outline
                    vertexes[3] = new Dot(a0, b0 + stroke / 2); //Lower outline

                } else {
                    double a0 = (Math.max(y0, y1) == y0) ? x0 : x1;
                    double b0 = Math.max(y0, y1); //ниже
                    double a1 = (Math.min(y0, y1) == y0) ? x0 : x1;
                    double b1 = Math.min(y0, y1); //выше

                    vertexes[0] = new Dot(a0 - stroke / 2 * Math.abs(Math.sin(angle.angle)) * Math.signum(a1 - a0), b0 - stroke / 2 * Math.abs(Math.cos(angle.angle))); //Upper outline
                    vertexes[1] = new Dot(a1 - stroke / 2 * Math.abs(Math.sin(angle.angle)) * Math.signum(a1 - a0), b1 - stroke / 2 * Math.abs(Math.cos(angle.angle))); //Upper outline
                    vertexes[2] = new Dot(a1 + stroke / 2 * Math.abs(Math.sin(angle.angle)) * Math.signum(a1 - a0), b1 + stroke / 2 * Math.abs(Math.cos(angle.angle))); //Lower outline
                    vertexes[3] = new Dot(a0 + stroke / 2 * Math.abs(Math.sin(angle.angle)) * Math.signum(a1 - a0), b0 + stroke / 2 * Math.abs(Math.cos(angle.angle))); //Lower outline
                }
            }
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.green);
        g2.setStroke(new BasicStroke(1));
        /*
        g.drawLine((int)vertexes[0].x, (int)vertexes[0].y, (int)vertexes[1].x, (int)vertexes[1].y);
        g.drawLine((int)vertexes[1].x, (int)vertexes[1].y, (int)vertexes[2].x, (int)vertexes[2].y);
        g.drawLine((int)vertexes[2].x, (int)vertexes[2].y, (int)vertexes[3].x, (int)vertexes[3].y);
        g.drawLine((int)vertexes[0].x, (int)vertexes[0].y, (int)vertexes[3].x, (int)vertexes[3].y);
        */

        g.setColor(color);
        g2.setStroke(new BasicStroke((int) stroke));
        g.drawLine((int) x0, (int) y0, (int) x1, (int) y1);
    }

    public void gravity(double gravityX, double gravityY) {
        speedX += gravityX;
        speedY += gravityY;

        x0 += speedX;
        x1 += speedX;
        y0 += speedY;
        y1 += speedY;

        if (stroke > 1) {
            for (int i = 0; i < 4; ++i) {
                vertexes[i].x += speedX;
                vertexes[i].y += speedY;
            }
        }

        angle.angle = Math.atan2(y1 - y0, x1 - x0); //y = k * x + b => angle = atan(k);  //TODO I should get rid of atan and replace it with evaluating the angle via scalar product
        centerOfTheMass();
    }

    public double getLength() {
        return Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));
    }

    public void centerOfTheMass() {
        centerOfMassX = (x0 + x1) / 2.0;
        centerOfMassY = (y0 + y1) / 2.0;
    }

    public double distanceLineDot(int x, int y) {
        //A, B - endpoints of the line; C - center of the circle
        double a = Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1)); //Pythagorean theorem for AB
        double b = Math.sqrt((x0 - x) * (x0 - x) + (y0 - y) * (y0 - y)); //Pythagorean theorem for AC
        double c = Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1)); //Pythagorean theorem for BC

        double cosAB = (a * a + b * b - c * c) / (2 * a * b); //Law of cosines for a^b
        double cosAC = (a * a - b * b + c * c) / (2 * a * c); //Law of cosines for a^c

        double distance;
        double halfPerimeter = (a + b + c) / 2;
        double area = Math.sqrt(halfPerimeter * (halfPerimeter - a) * (halfPerimeter - b) * (halfPerimeter - c)); //Heron's theorem for the triangle ABC

        /*
        if (cosAB >= 0 && cosAC >= 0) { //Acute triangle or right triangle
            distance = (2 * area / a); //Formula of the area of the triangle
        } else {  //Obtuse triangle
            if (cosAB < 0) { //b^a > 90º
                distance = b;
            } else { //b^c > 90º
                distance = c;
            }
        }
        */

        return distance = (2 * area / a); //That is real distance, but I need the length of the perpendicular, or height
    }

    public boolean intersectionLineLine(Line line) {
        //http://www.cyberforum.ru/delphi-beginners/thread652199.html
//        Определяется так. Предположим, у нас есть 3 точки: А(х1,у1), Б(х2,у2), С(х3,у3). Через точки А и Б проведена прямая. И нам надо определить, как расположена точка С относительно прямой АБ. Для этого вычисляем значение:
//        D = (х3 - х1) * (у2 - у1) - (у3 - у1) * (х2 - х1)
//                - Если D = 0 - значит, точка С лежит на прямой АБ.
//                - Если D < 0 - значит, точка С лежит слева от прямой.
//                - Если D > 0 - значит, точка С лежит справа от прямой.
        boolean intersection = false;
        if (((line.x0 - x0) * (y1 - y0) - (line.y0 - y0) * (x1 - x0)) * ((line.x1 - x0) * (y1 - y0) - (line.y1 - y0) * (x1 - x0)) <= 0
                && ((x0 - line.x0) * (line.y1 - line.y0) - (y0 - line.y0) * (line.x1 - line.x0)) * ((x1 - line.x0) * (line.y1 - line.y0) - (y1 - line.y0) * (line.x1 - line.x0)) <= 0)
            intersection = true;
        return intersection;
    }

    public Line getOutline(int mode) {
        if (mode == 0) {
            Line bufLine = new Line(vertexes[0], vertexes[1], 1, color);
            bufLine.prevStroke = stroke;
            //bufLine.angle = angle;
            return bufLine;
        }
        if (mode == 1) {
            Line bufLine = new Line(vertexes[1], vertexes[2], 1, color);
            bufLine.prevStroke = stroke;
            return bufLine;
        }
        if (mode == 2) {
            Line bufLine = new Line(vertexes[2], vertexes[3], 1, color);
            bufLine.prevStroke = stroke;
            return bufLine;
        }
        /*
        if (mode == 3) {
            Line bufLine = new Line(vertexes[0], vertexes[3], 1, color);
            bufLine.prevStroke = stroke;
            return bufLine;
        }
        */
        return null;
    }
}
