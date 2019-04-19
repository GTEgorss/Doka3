import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PenPanel extends JPanel {
    boolean opened = false;

    ArrayList<GamePanel> gamePanels;

    public PenPanel(ArrayList<GamePanel> gamePanels) {
        this.gamePanels = gamePanels;

        setLayout(null);
        setBackground(Color.white);

        ImageIcon backIcon = new ImageIcon(getClass().getResource("media/fuckGoBack.png"));
        JButton back = new JButton(backIcon);
        back.setBorder(BorderFactory.createLineBorder(Color.black, 0));
        back.setBounds(15, 15, backIcon.getIconWidth(), backIcon.getIconHeight());
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opened = false;
            }
        });
        add(back);

        ImageIcon pen0Icon = new ImageIcon(getClass().getResource("media/pen0.png"));
        JButton pen0 = new JButton(pen0Icon);
        pen0.setBorder(BorderFactory.createLineBorder(Color.gray, 1, true));
        pen0.setBounds(200, 200, 256, 256);
        pen0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (GamePanel gamePanel : gamePanels) {
                    gamePanel.stroke = 8;
                    gamePanel.color = Color.black;

                    /*
                    try (FileWriter writer = new FileWriter(getClass().getResource("media/skin.txt").getFile(), false)) {
                        String string = "0";
                        writer.write(string);
                        writer.flush();
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    */
                }
            }
        });
        add(pen0);

        ImageIcon pen1Icon = new ImageIcon(getClass().getResource("media/pen1.png"));
        JButton pen1 = new JButton(pen1Icon);
        pen1.setBorder(BorderFactory.createLineBorder(Color.gray, 1, true));
        pen1.setBounds(500, 200, 256, 256);
        pen1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (GamePanel gamePanel : gamePanels) {
                    gamePanel.stroke = 14;
                    gamePanel.color = Color.blue;

                    /*
                    try (FileWriter writer = new FileWriter(getClass().getResource("media/skin.txt").getFile(), false)) {
                        String string = "1";
                        writer.write(string);
                        writer.flush();
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    */
                }
            }
        });
        add(pen1);

        ImageIcon pen2Icon = new ImageIcon(getClass().getResource("media/pen2.png"));
        JButton pen2 = new JButton(pen2Icon);
        pen2.setBorder(BorderFactory.createLineBorder(Color.gray, 1, true));
        pen2.setBounds(800, 200, 256, 256);
        pen2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (GamePanel gamePanel : gamePanels) {
                    gamePanel.stroke = 4;
                    gamePanel.color = Color.green;

                    /*
                    try (FileWriter writer = new FileWriter(getClass().getResource("media/skin.txt").getPath().replaceAll("!", ""), false)) {
                        String string = "2";
                        writer.write(string);
                        writer.flush();
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    */
                }
            }
        });
        add(pen2);

        FocusManager.getCurrentManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                        opened = false;
                    }
                }
                return false;
            }
        });
    }

}
