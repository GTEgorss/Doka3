import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GamePanel extends JPanel {
    boolean opened = false;
    boolean allowed = true;

    boolean passed = false;
    boolean lastLevel = false;

    int levelPermanent;

    //Balls
    int x1, y1, x2, y2;
    Ball[] ball = new Ball[2];

    //Curves
    int stroke;
    Color color;
    ArrayList<Drawing> drawings = new ArrayList<>();
    double mu = 0.1; //The coefficient of friction of curve
    boolean mousePressed = false;
    boolean started = false;

    //Obstacles
    ArrayList<Obstacle> obstacles = new ArrayList<>();

    //Lattices
    ArrayList<Lattice> lattices = new ArrayList<>();

    //Listeners
    MyMouseListener listener;
    MyMouseMotionListener motionListener;
    MyKeyEventDispatcher dispatcher;

    boolean enterPressed = false;

    VictoryPanel victoryPanel;
    boolean victoryPanelOpen = false;
    boolean victoryPanelDelay = false;
    DefeatPanel defeatPanel;
    boolean defeatPanelDelay = false;
    boolean defeatPanelOpen = false;

    public GamePanel(int level, boolean lastLevel) throws IOException {
        this.lastLevel = lastLevel;

        setLayout(null);

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

        ImageIcon restartIcon = new ImageIcon(getClass().getResource("media/restart.png"));
        JButton restart = new JButton(restartIcon);
        restart.setBorder(BorderFactory.createLineBorder(Color.black, 0));
        restart.setBounds(1200, 15, restartIcon.getIconWidth(), restartIcon.getIconHeight());
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    restart(levelPermanent);
                    passed = false;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        add(restart);

        Scanner scanner = new Scanner(getClass().getResourceAsStream("media/skin.txt"));
        int skin = scanner.nextInt();
        if (skin == 0) {
            this.stroke = 8;
            this.color = Color.black;
        } else {
            if (skin == 1) {
                this.stroke = 14;
                this.color = Color.blue;
            } else {
                this.stroke = 4;
                this.color = Color.green;
            }
        }

        levelPermanent = level;
        if (level == 0) {
            obstacles.add(new Rectangle(0, 500, 1280, 300));
            x1 = 1000;
            y1 = 300;
            x2 = 200;
            y2 = 300;
            ball[0] = new Ball(x1, y1, Color.pink);
            ball[1] = new Ball(x2, y2, Color.cyan);
        } else {
            if (level == 1) {
                obstacles.add(new Rectangle(0, 500, 400, 400));
                obstacles.add(new Triangle(700, 600, -300, -100));
                obstacles.add(new Rectangle(400, 600, 1000, 150));
                x1 = 1000;
                y1 = 300;
                x2 = 200;
                y2 = 300;
                ball[0] = new Ball(x1, y1, Color.pink);
                ball[1] = new Ball(x2, y2, Color.cyan);
            } else {
                if (level == 2) {
                    obstacles.add(new Rectangle(0, 500, 1280, 300));
                    lattices.add(new Lattice(200, 300, 400, 400));
                    x1 = 850;
                    y1 = 400;
                    x2 = 350;
                    y2 = 400;
                    ball[0] = new Ball(x1, y1, Color.pink);
                    ball[1] = new Ball(x2, y2, Color.cyan);
                } else {
                    if (level == 3) {
                        obstacles.add(new Rectangle(350, 200, 500, 300));
                        x1 = 1000;
                        y1 = 300;
                        x2 = 200;
                        y2 = 300;
                        ball[0] = new Ball(x1, y1, Color.pink);
                        ball[1] = new Ball(x2, y2, Color.cyan);
                    } else {
                        obstacles.add(new Rectangle(0, 600, 1280, 200));
                        x1 = 400;
                        y1 = 300;
                        x2 = 350;
                        y2 = 300;
                        ball[0] = new Ball(x1, y1, Color.pink);
                        ball[1] = new Ball(x2, y2, Color.cyan);
                    }
                }
            }
        }
        drawings.add(new Drawing(1, Color.white));

        listener = new MyMouseListener(this);
        motionListener = new MyMouseMotionListener(this);
        dispatcher = new MyKeyEventDispatcher(this);

        victoryPanel = new VictoryPanel(lastLevel);
        victoryPanel.setBounds(640 - 400, 360 - 250, 800, 500);
        add(victoryPanel);
        victoryPanel.setVisible(false);
        defeatPanel = new DefeatPanel();
        victoryPanel.setBounds(640 - 400, 360 - 250, 800, 500);
        add(defeatPanel);
        defeatPanel.setVisible(false);
    }

    @Override
    public void paintComponent(Graphics g) {

        //System.out.print(" panel begin");

        //Gravity
        if (started && !ball[0].collision) {
            ball[0].gravity();
            ball[1].gravity();
        }


        //Collisions
        if (started && !ball[0].collision) {
            for (Drawing drawing : drawings) {
                if (drawing.finished) {
                    drawing.gravity();
                }
            }

            passed = ball[0].collisionBallBall(ball[1]);

            for (Drawing drawing : drawings) {
                if (drawing.finished) {
                    ball[0].collisionBallDrawing(drawing);
                    ball[1].collisionBallDrawing(drawing);
                }
                for (Obstacle obstacle : obstacles) {
                    if (drawing.collisionDrawingObstacle(obstacle)) {
                        drawing.doCollisionDrawingObstacle();
                        while (drawing.collisionDrawingObstacle(obstacle)) {
                            drawing.moveDrawing(drawing.movingVector);
                        }
                        drawing.moveDrawing(drawing.movingVector.normalize().multByNumber(drawing.lines.get(0).stroke / 2));
                    }
                }

                /*
                for (Drawing drawing0 : drawings) {
                    if (drawing0 != drawing) {
                        if (drawing.collisionDrawingDrawing(drawing0)) {
                            drawing.doCollisionDrawingDrawing(drawing0);
                            while (drawing.collisionDrawingDrawing(drawing0)) {
                                drawing.moveDrawing(drawing.movingVector);
                            }
                            drawing.moveDrawing(drawing.movingVector.normalize().multByNumber(drawing.lines.get(0).stroke / 2));
                        }
                    }
                }
                */
            }

            for (Obstacle obstacle : obstacles) {
                ball[0].collisionBallObstacle(obstacle);
                ball[1].collisionBallObstacle(obstacle);
            }

            //Gravity update
            ball[0].updateGravity(mu);
            ball[1].updateGravity(mu);
        }

        //Drawing
        //Background
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        //balls
        ball[0].draw(g);
        ball[1].draw(g);
        //System.out.print(" " + 1);
        //curve
        for (int i = 1; i < drawings.size(); ++i) {
            drawings.get(i).draw(g);
        }

        //System.out.print(" " + 2);

        //obstacle
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(g);
        }
        //lattice
        for (Lattice lattice : lattices) {
            lattice.draw(g);
        }


        //Start detection
        if (!mousePressed && drawings.size() > 1) {
            started = true;
        }

        //System.out.print(" panel end.");

        if (victoryPanelOpen) {
            try {
                if (!victoryPanelDelay) {
                    Thread.sleep(1000);
                    victoryPanelDelay = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            victoryPanel.setVisible(true);
        }

        if (passed && !victoryPanelOpen) {
            victoryPanelOpen = true;
        }
    }

    public void restart(int level) throws IOException {
        drawings.clear();
        drawings.add(new Drawing(1, Color.white));
        started = false;
        victoryPanel.setVisible(false);
        defeatPanel.setVisible(false);
        victoryPanelDelay = false;
        defeatPanelDelay = false;
        victoryPanelOpen = false;
        defeatPanelOpen = false;
        if (level == 0) {
            x1 = 1000;
            y1 = 300;
            x2 = 200;
            y2 = 300;
            ball[0] = new Ball(x1, y1, Color.pink);
            ball[1] = new Ball(x2, y2, Color.cyan);
        } else {
            if (level == 1) {
                x1 = 1000;
                y1 = 300;
                x2 = 200;
                y2 = 300;
                ball[0] = new Ball(x1, y1, Color.pink);
                ball[1] = new Ball(x2, y2, Color.cyan);
            } else {
                if (level == 2) {
                    x1 = 850;
                    y1 = 400;
                    x2 = 350;
                    y2 = 400;
                    ball[0] = new Ball(x1, y1, Color.pink);
                    ball[1] = new Ball(x2, y2, Color.cyan);
                } else {
                    if (level == 3) {
                        x1 = 1000;
                        y1 = 300;
                        x2 = 200;
                        y2 = 300;
                        ball[0] = new Ball(x1, y1, Color.pink);
                        ball[1] = new Ball(x2, y2, Color.cyan);
                    } else {
                        x1 = 400;
                        y1 = 300;
                        x2 = 350;
                        y2 = 300;
                        ball[0] = new Ball(x1, y1, Color.pink);
                        ball[1] = new Ball(x2, y2, Color.cyan);
                    }
                }
            }
        }
    }
}
