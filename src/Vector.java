import java.util.ArrayList;

public class Vector {
    double x;
    double y;
    double mass;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
        this.mass = 0;
    }

    public Vector(double x, double y, double mass) {
        this.x = x;
        this.y = y;
        this.mass = mass;
    }

    public Vector(Line line) {
        this(line.x1 - line.x0, line.y1 - line.y0);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                ", mass=" + mass +
                '}';
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public double scalarProduct(Vector vector0) {
        double x0 = x;
        double y0 = y;
        double x1 = vector0.x;
        double y1 = vector0.y;
        return x0 * x1 + y0 * y1;
    }

    public double cosOfAngleBetweenVectors(Vector vector0) {
        if (vector0.x == 0 && vector0.y == 0) vector0.y = 1; //TODO just skip where usage
        double scalarProduct = this.scalarProduct(vector0);
        double length0 = this.getLength();
        double length1 = vector0.getLength();
        double cos = scalarProduct / (length0 * length1);
        return cos;
    }

    public Vector sumOfTwoVectors(Vector vector) {
        if (vector.mass == 0 && mass == 0) {
            return new Vector(x + vector.x, y + vector.y, 0);
        } else {
            return new Vector((x * mass + vector.x * vector.mass) / (mass + vector.mass), (y * mass + vector.y * vector.mass) / (mass + vector.mass), mass + vector.mass);
        }
    }

    public static Vector sumOfManyVectors(ArrayList<Vector> vectors) {
        Vector sumVector = new Vector(0, 0, 0);
        for (Vector vector : vectors) {
            sumVector = sumVector.sumOfTwoVectors(vector);
        }
        return sumVector;
    }

    public Vector normalize() {
        return new Vector(x / getLength(), y / getLength());
    }

    public Vector multByNumber(double num) {
        return new Vector(x * num, y * num);
    }
}
