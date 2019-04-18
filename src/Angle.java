public class Angle {
    double angle;

    public Angle(double angle) {
        this.angle = angle;
    }

    public static double format(double number) { //sin or cos
        return number > 0.0000000000005 || number < -0.0000000000005 ? number : 0;
    }
}
