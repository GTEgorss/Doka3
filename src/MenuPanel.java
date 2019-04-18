import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuPanel extends JPanel {

    boolean opened = true;
    boolean exit = false;

    int lastLevel;

    int mode = -1;

    MyFont myFont;

    public MenuPanel() {
        setLayout(null);
        setBackground(Color.white);

        myFont = new MyFont();

        Scanner scanner = new Scanner(getClass().getResourceAsStream("media/lastLevel.txt"));
        lastLevel = scanner.nextInt();

        ImageIcon startIcon = new ImageIcon(getClass().getResource("media/start.png"));
        JButton start = new JButton(startIcon);
        start.setBorder(BorderFactory.createLineBorder(Color.white, 0));
        start.setBounds(520, 250, startIcon.getIconWidth(), startIcon.getIconHeight());
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 0;
            }
        });
        add(start);

        ImageIcon penIcon = new ImageIcon(getClass().getResource("media/pen.png"));
        JButton pen = new JButton(penIcon);
        pen.setBorder(BorderFactory.createLineBorder(Color.white, 0));
        pen.setBounds(260, 240, penIcon.getIconWidth(), penIcon.getIconHeight());
        pen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 2;
            }
        });
        add(pen);

        ImageIcon levelsIcon = new ImageIcon(getClass().getResource("media/levels.png"));
        JButton levels = new JButton(levelsIcon);
        levels.setBorder(BorderFactory.createLineBorder(Color.white, 0));
        levels.setBounds(820, 250, levelsIcon.getIconWidth(), levelsIcon.getIconHeight());
        levels.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 1;
            }
        });
        add(levels);

        ImageIcon settingsIcon = new ImageIcon(getClass().getResource("media/settings.png"));
        JButton settings = new JButton(settingsIcon);
        settings.setBorder(BorderFactory.createLineBorder(Color.white, 0));
        settings.setBounds(1180, 600, settingsIcon.getIconWidth(), settingsIcon.getIconHeight());
        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 3;
            }
        });
        add(settings);

        JButton quit = new JButton("Quit");
        quit.setBorder(BorderFactory.createLineBorder(Color.white, 0));
        quit.setForeground(new Color(100, 100, 100));
        quit.setFont(myFont.helvetica);
        //quit.setFont(new Font("helvetica", Font.BOLD, 40));
        quit.setBounds(20, 630, 150, 50);
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit = true;
            }
        });
        add(quit);
    }

    public void upgrade(ArrayList<GamePanel> gamePanels) {
        ///////Last level; memorizing
        int bufLastLevel = lastLevel;
        for (int i = 0; i < 5; ++i) {
            if (gamePanels.get(i).passed) {
                if (i < 4) {
                    lastLevel = i + 1;
                } else {
                    lastLevel = 4;
                }
            }
        }
        try (FileWriter writer = new FileWriter(getClass().getResource("media/lastLevel.txt").getPath(), false)) {
            writer.write(lastLevel + "");
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
