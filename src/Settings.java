import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Settings extends JPanel {
    boolean opened = false;
    ArrayList<GamePanel> gamePanels;
    JFrame frame;
    boolean correct = false;
    JDialog attention;

    public Settings(ArrayList<GamePanel> gamePanels, JFrame frame) {
        this.gamePanels = gamePanels;
        this.frame = frame;
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

        JButton clearProgress = new JButton("Clear progress");
        clearProgress.setForeground(new Color(100, 100, 100));
        clearProgress.setFont(new Font("PHOSPHATE", Font.PLAIN, 40));
        clearProgress.setBounds(440, 250, 400, 60);
        clearProgress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attention.setVisible(true);
            }
        });
        add(clearProgress);

        attention = new JDialog(frame, "Attention");
        attention.setBounds(470, 250, 310, 180);
        attention.setFocusable(false); //TODO to check
        JPanel panel = new JPanel(null);
        //panel.setBackground(new Color(70,70,70));
        panel.setBackground(new Color(230, 230, 230));
        JLabel label0 = new JLabel("Are you sure to clear progress?");
        JLabel label1 = new JLabel("The previous results will be deleted.");
        label0.setBounds(20, 20, 330, 30);
        label1.setBounds(20, 60, 330, 30);
        label0.setFont(new Font(label0.getName(), Font.PLAIN, 16));
        label1.setFont(new Font(label1.getName(), Font.PLAIN, 16));
        //label0.setForeground(Color.white);
        label0.setForeground(Color.black);
        //label1.setForeground(Color.white);
        label1.setForeground(Color.black);
        panel.add(label0);
        panel.add(label1);
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        ok.setBorder(BorderFactory.createLineBorder(new Color(50,50,50), 3, true));
        cancel.setBorder(BorderFactory.createLineBorder(new Color(50,50,50), 3, true));
        ok.setOpaque(true);
        cancel.setOpaque(true);
        ok.setBackground(new Color(81,125,255));
        //cancel.setBackground(new Color(80,80,80));
        cancel.setBackground(new Color(215, 215, 215));
        ok.setForeground(Color.white);
        //cancel.setForeground(Color.white);
        cancel.setForeground(Color.black);
        ok.addActionListener(e -> {
            correct = true;
            attention.setVisible(false);
        });
        cancel.addActionListener(e -> {
            correct = false;
            attention.setVisible(false);
        });
        ok.setBounds(215, 110, 80, 30);
        cancel.setBounds(17, 110, 80, 30);
        panel.add(ok);
        panel.add(cancel);
        attention.add(panel);

        attention.setVisible(false);
    }
}
