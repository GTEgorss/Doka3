import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Lattice {

    BufferedImage lattice;
    int x, y, width, height;

    public Lattice(int x, int y) throws IOException {
        this(x, y, 300, 300);
    }

    public Lattice(int x, int y, int width, int height) throws IOException {
        lattice = ImageIO.read(getClass().getResourceAsStream("media/lattice.png"));
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        g.drawImage(lattice, x, y, width, height, null);
    }

    public Line getLine(int mode) {
        if (mode == 0) {
            return new Line(x, y, x + width, y, 1, Color.white); //upper
        }
        if (mode == 1) {
            return new Line(x, y, x, y + height, 1, Color.white); //left
        }
        if (mode == 2) {
            return new Line(x + width, y, x + width, y + height, 1, Color.white); //right
        }
        if (mode == 3) {
            return new Line(x, y +height, x + width, y + height, 1, Color.white); //lower
        }
        return null;
    }

    public boolean checkDot(double x, double y) {
        return x > this.x && x < this.x + width && y > this.y && y < this.y + height;
    }
}
