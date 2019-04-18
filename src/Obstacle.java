import java.awt.*;
import java.awt.geom.Path2D;

public class Obstacle {
    boolean rectangle;

    public Obstacle() {
    }

    public void draw(Graphics g) {
        g.setColor(new Color(100, 100, 100));
    }
}

class Rectangle extends Obstacle {

    double x;
    double y;
    double width;
    double height;


    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        rectangle = true;
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.fillRect((int)x, (int)y, (int)width, (int)height);
    }

    public Line getLine(int mode) {
        if (mode == 0) {
            return new Line(x, y, x + width, y, 1, new Color(100, 100, 100)); //upper
        }
        if (mode == 1) {
            return new Line(x, y, x, y + height, 1, new Color(100, 100, 100)); //left
        }
        if (mode == 2) {
            return new Line(x + width, y, x + width, y + height, 1, new Color(100, 100, 100)); //right
        }
        if (mode == 3) {
            return new Line(x, y +height, x + width, y + height, 1, new Color(100, 100, 100)); //lower
        }
        return null;
    }

    public Dot getVertex(int mode0, int mode1) {
        if (mode0 == 0) {
            if (mode1 == 0) {
               return null;
            }
            if (mode1 == 1) {
                return new Dot(x, y);
            }
            if (mode1 == 2) {
                return new Dot(x + width, y);
            }
            if (mode1 == 3) {
                return null;
            }
        }
        if (mode0 == 1) {
            if (mode1 == 1) {
                return null;
            }
            if (mode1 == 0) {
                return new Dot(x, y);
            }
            if (mode1 == 2) {
                return null;
            }
            if (mode1 == 3) {
                return new Dot(x, y + height);
            }
        }
        if (mode0 == 2) {
            if (mode1 == 2) {
                return null;
            }
            if (mode1 == 0) {
                return new Dot(x + width, y);
            }
            if (mode1 == 1) {
                return null;
            }
            if (mode1 == 3) {
                return new Dot(x + width, y + height);
            }
        }
        if (mode0 == 3) {
            if (mode1 == 3) {
                return null;
            }
            if (mode1 == 0) {
                return null;
            }
            if (mode1 == 1) {
                return new Dot(x, y + height);
            }
            if (mode1 == 2) {
                return new Dot(x + width, y + height);
            }
        }
        return null;
    }

    public boolean checkDot(double x, double y) {
        return x > this.x && x < this.x + width && y > this.y && y < this.y + height;
    }
}

class Triangle extends Obstacle {
    double x0;
    double y0;
    double x1;
    double y1;
    double x2;
    double y2;
    Path2D myPath = new Path2D.Double();

    public Triangle(double x0, double y0, double x1, double y1, double x2, double y2) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        myPath.moveTo(x0, y0);
        myPath.lineTo(x1, y1);
        myPath.lineTo(x2, y2);
        myPath.closePath();
        rectangle = false;
    }

    public Triangle(double x0, double y0, double width, double height) { //Right triangle (half of a rectangle); direction: right and down
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x0 + width;
        this.y1 = y0;
        this.x2 = x0 + width;
        this.y2 = y0 + height;
        myPath.moveTo(x0, y0);
        myPath.lineTo(x1, y1);
        myPath.lineTo(x2, y2);
        myPath.closePath();
        rectangle = false;
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.fill(myPath);
    }

    public Line getLine (int mode) {
        if (mode == 0) {
           return new Line(x0, y0, x1, y1, 1, new Color(100, 100, 100));
        }
        if (mode == 1) {
            return new Line(x0, y0, x2, y2, 1, new Color(100, 100, 100));
        }
        if (mode == 2) {
            return new Line(x2, y2, x1, y1, 1, new Color(100, 100, 100));
        }
        return null;
    }

    public boolean checkDot(double x, double y) {
        //http://www.cyberforum.ru/algorithms/thread144722.html
        double a = (x0 - x) * (y1 - y0) - (x1 - x0) * (y0 - y);
        double b = (x1 - x) * (y2 - y1) - (x2 - x1) * (y1 - y);
        double c = (x2 - x) * (y0 - y2) - (x0 - x2) * (y2 - y);
        return (a >= 0 && b >= 0 && c >= 0) || (a <= 0 && b <= 0 && c <= 0);
    }
}
