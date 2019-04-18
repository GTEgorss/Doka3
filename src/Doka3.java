import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Doka3 {

    public static void main(String[] args) throws InterruptedException, IOException {
        JFrame frame = new JFrame();
        frame.setSize(1280, 719); //The size of my Macbook window area
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        MenuPanel menuPanel = null;
        try {
            menuPanel = new MenuPanel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        frame.add(menuPanel);

        GamePanel gamePanel = null;
        try {
            gamePanel = new GamePanel(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<GamePanel> gamePanels = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            try {
                gamePanels.add(new GamePanel(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PenPanel penPanel = new PenPanel(gamePanels);
        LevelPanel levelPanel = new LevelPanel(menuPanel);
        Settings settings = new Settings(gamePanels, frame);

        for (GamePanel gamePanel1 : gamePanels) {
            gamePanel1.addMouseMotionListener(gamePanel1.motionListener);

            gamePanel1.addMouseListener(gamePanel1.listener);

            FocusManager.getCurrentManager().addKeyEventDispatcher(gamePanel1.dispatcher);
        }

        frame.setSize(1280, 719); //The size of my MacBook window area
        frame.setVisible(true);

        while (true) {
            ///////Cooperation with gamePanels
            assert menuPanel != null;
            if (menuPanel.mode == 0 && menuPanel.opened) {
                gamePanel = gamePanels.get(menuPanel.lastLevel);
                frame.remove(menuPanel);
                menuPanel.opened = false;
                frame.add(gamePanel);
                gamePanel.opened = true;
                frame.setVisible(true);
            }

            assert gamePanel != null;
            if (!menuPanel.opened && !gamePanel.opened && menuPanel.mode == 0) {
                frame.remove(gamePanel);
                gamePanel.opened = false;
                frame.add(menuPanel);
                gamePanel.restart(gamePanel.levelPermanent);
                menuPanel.opened = true;
                frame.setVisible(true);
                menuPanel.mode = -1;
            }
            ////////


            ///////Cooperation with levelPanel
            if (menuPanel.mode == 1 && menuPanel.opened) {
                frame.remove(menuPanel);
                menuPanel.opened = false;
                frame.add(levelPanel);
                levelPanel.opened = true;
                frame.setVisible(true);
            }

            if (!menuPanel.opened && !levelPanel.opened && menuPanel.mode == 1) {
                frame.remove(levelPanel);
                levelPanel.opened = false;
                frame.add(menuPanel);
                menuPanel.opened = true;
                frame.setVisible(true);
                menuPanel.mode = -1;
            }

            if (levelPanel.chosen) {
                gamePanel = gamePanels.get(levelPanel.level);
                frame.remove(levelPanel);
                levelPanel.opened = false;
                frame.add(gamePanel);
                gamePanel.opened = true;
                frame.setVisible(true);
                levelPanel.level = -1;
                levelPanel.chosen = false;
                menuPanel.mode = 0;
            }
            ///////


            //////Cooperation with penPanel
            if (menuPanel.mode == 2 && menuPanel.opened) {
                frame.remove(menuPanel);
                menuPanel.opened = false;
                frame.add(penPanel);
                penPanel.opened = true;
                frame.setVisible(true);
            }

            if (!menuPanel.opened && !penPanel.opened && menuPanel.mode == 2) {
                frame.remove(penPanel);
                penPanel.opened = false;
                frame.add(menuPanel);
                menuPanel.opened = true;
                frame.setVisible(true);
                menuPanel.mode = -1;
            }
            //////

            /////Cooperation with settings
            if (menuPanel.mode == 3 && menuPanel.opened) {
                frame.remove(menuPanel);
                menuPanel.opened = false;
                frame.add(settings);
                settings.opened = true;
                frame.setVisible(true);
            }

            if (!menuPanel.opened && !settings.opened && menuPanel.mode == 3) {
                frame.remove(settings);
                settings.opened = false;
                settings.attention.setVisible(false);
                frame.add(menuPanel);
                menuPanel.opened = true;
                frame.setVisible(true);
                menuPanel.mode = -1;
            }

            if (settings.correct) {
                try (FileWriter writer = new FileWriter("/Users/egorsergeev/IdeaProjects/Doka3/src/lastLevel.txt", false)) {
                    String string = "0";
                    writer.write(string);
                    writer.flush();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                try (FileWriter writer = new FileWriter("/Users/egorsergeev/IdeaProjects/Doka3/src/skin.txt", false)) {
                    String string = "0";
                    writer.write(string);
                    writer.flush();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                for (GamePanel gamePanel1 : gamePanels) {
                    gamePanel1.passed = false;
                }
                settings.correct = false;
                menuPanel.lastLevel = 0;
            }
            /////

            int bufLastLevel = menuPanel.lastLevel;
            for (int i = 0; i < 5; ++i) {
                if (gamePanels.get(i).passed) {
                    if (i < 4) {
                        menuPanel.lastLevel = i + 1;
                    } else {
                        menuPanel.lastLevel = 4;
                    }
                }
            }
            if (menuPanel.lastLevel != bufLastLevel) {
                try (FileWriter writer = new FileWriter("/Users/egorsergeev/IdeaProjects/Doka3/src/lastLevel.txt", false)) {
                    String string = menuPanel.lastLevel + "";
                    writer.write(string);
                    writer.flush();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            if (!gamePanel.enterPressed) frame.repaint();
            Thread.sleep(20); //Standard sleep
        }
    }
}
