import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Doka3 {

    public static void main(String[] args) throws InterruptedException, IOException {
        JFrame frame = new JFrame();
        frame.setSize(1280, 719); //The size of my Macbook window area
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);



        MenuPanel menuPanel = new MenuPanel();
        frame.add(menuPanel);

        GamePanel gamePanel = null;
        try {
            gamePanel = new GamePanel(0, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<GamePanel> gamePanels = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            try {
                if (i == 4) {
                    gamePanels.add(new GamePanel(i, true));
                } else {
                    gamePanels.add(new GamePanel(i, false));
                }
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

        JustForFileWriter writer = new JustForFileWriter();

        while (true) {
            if (menuPanel.exit) {
                System.exit(0);
            }
            ///////Last level; memorizing
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
                writer.write(menuPanel, menuPanel.lastLevel + "", "media/lastLevel.txt");
            }

            ///////Cooperation with gamePanels
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
                gamePanel.passed = false;
                gamePanel.opened = false;
                frame.add(menuPanel);
                gamePanel.restart(gamePanel.levelPermanent);
                menuPanel.opened = true;
                frame.setVisible(true);
                menuPanel.mode = -1;
            }


            if (gamePanel.victoryPanel.nextClicked) {
                frame.remove(gamePanel);
                gamePanel.opened = false;
                gamePanel.passed = false;
                gamePanel.restart(gamePanel.levelPermanent);
                gamePanel.victoryPanel.nextClicked = false;

                gamePanel = gamePanels.get(menuPanel.lastLevel);
                frame.add(gamePanel);
                gamePanel.opened = true;
                frame.setVisible(true);
                gamePanel.victoryPanel.nextClicked = false;
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
                writer.write(menuPanel, "0", "media/lastLevel.txt");
                writer.write(menuPanel, "0", "media/skin.txt");
                for (GamePanel gamePanel1 : gamePanels) {
                    gamePanel1.passed = false;
                }
                settings.correct = false;
                menuPanel.lastLevel = 0;
            }
            /////

            if (!gamePanel.enterPressed) frame.repaint();
            Thread.sleep(20); //Standard sleep
        }
    }
}
