import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DefeatPanel extends JPanel {
    boolean opened = false;
    boolean nextClicked = false;

    BufferedImage failed;

    public DefeatPanel() throws IOException { //800x500
        setLayout(null);
        setBackground(Color.white);

        failed = ImageIO.read(getClass().getResourceAsStream("media/fail.png"));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setStroke(new BasicStroke(10));
        g.drawRect(0,0, getWidth(), getHeight());
        g.drawImage(failed, 175, 75, null);
    }
}