import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VictoryPanel extends JPanel {
    boolean opened = false;
    boolean nextClicked = false;

    boolean last;

    BufferedImage passed;

    public VictoryPanel(boolean last) throws IOException { //800x500
        this.last = last;

        setLayout(null);
        setBackground(Color.white);
        passed = ImageIO.read(getClass().getResourceAsStream("media/passed.png"));

        if (!last) {
            ImageIcon nextIcon = new ImageIcon(getClass().getResource("media/next.png"));
            JButton next = new JButton(nextIcon);
            next.setBounds(640, 360, nextIcon.getIconWidth(), nextIcon.getIconHeight());
            next.setBorder(BorderFactory.createLineBorder(Color.white, 0));
            next.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    opened = false;
                    nextClicked = true;
                }
            });
            add(next);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setStroke(new BasicStroke(10));
        g.drawRect(0, 0, getWidth(), getHeight());
        g.drawImage(passed, 175, 75, null);
    }
}