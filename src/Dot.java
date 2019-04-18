public class Dot {
    double x;
    double y;


    public Dot(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceDotDot(Dot dot) {
        return Math.sqrt((x - dot.x) * (x - dot.x) + (y - dot.y) * (y - dot.y));
    }
}
