import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class LevelPanel extends JPanel {
    boolean opened = false;
    boolean chosen = false;
    int level = -1;
    MenuPanel menuPanel;

    public LevelPanel(MenuPanel menuPanel1) {
        this.menuPanel = menuPanel1;

        setLayout(null);
        setBackground(Color.white);

        ImageIcon backIcon = new ImageIcon("/Users/egorsergeev/IdeaProjects/Doka3/src/fuckGoBack.png");
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

        ImageIcon level0Icon = new ImageIcon("");
        JButton level0 = new JButton("level0");
        //level0.setBorder(BorderFactory.createLineBorder(Color.white, 0));
        level0.setBounds(50, 250, 200, 100);
        level0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosen = true;
                level = 0;
            }
        });
        add(level0);

        ImageIcon level1Icon = new ImageIcon("");
        JButton level1 = new JButton("level1");
        //level1.setBorder(BorderFactory.createLineBorder(Color.white, 0));
        level1.setBounds(300, 250, 200, 100);
        level1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (menuPanel1.lastLevel >= 1) {
                    chosen = true;
                    level = 1;
                }
            }
        });
        add(level1);

        ImageIcon level2Icon = new ImageIcon("");
        JButton level2 = new JButton("level2");
        //level2.setBorder(BorderFactory.createLineBorder(Color.white, 0));
        level2.setBounds(550, 250, 200, 100);
        level2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (menuPanel1.lastLevel >= 2) {
                    chosen = true;
                    level = 2;
                }
            }
        });
        add(level2);

        ImageIcon level3Icon = new ImageIcon("");
        JButton level3 = new JButton("level3");
        //level3.setBorder(BorderFactory.createLineBorder(Color.white, 0));
        level3.setBounds(800, 250, 200, 100);
        level3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (menuPanel1.lastLevel >= 3) {
                    chosen = true;
                    level = 3;
                }
            }
        });
        add(level3);

        ImageIcon level4Icon = new ImageIcon("");
        JButton level4 = new JButton("level4");
        //level4.setBorder(BorderFactory.createLineBorder(Color.white, 0));
        level4.setBounds(1050, 250, 200, 100);
        level4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (menuPanel1.lastLevel >= 4) {
                    chosen = true;
                    level = 4;
                }
            }
        });
        add(level4);

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
