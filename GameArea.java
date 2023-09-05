import javax.net.ssl.SNIHostName;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.Random;
import java.util.Scanner;

public class GameArea extends JFrame {
    public static BufferStrategy bs;
    public static JFrame frame;
    public static Canvas canvas;

    public static int direction = 1;
    public static int indexOfLastMovedPart = 0;
    public static Snake snake;
    public static Graphics2D g;

    public static boolean gameOver = true;
    public static boolean gameStarted = false;
    public static boolean borderMode = false;
    public static boolean pause = false;
    public static boolean grid = false;

    public static int DEFAULT_FONT_SIZE = 90;
    public static int fontSize;

    public static Point goal;


    public static JComboBox<myDimension> dimensionJComboBox;
    public static JCheckBox fullscreenJCheckBox;
    public static JComboBox<String> mapSizeJComboBox;
    public static JCheckBox bordersModeJCheckBox;
    public static JComboBox<String> snakeSpeedJComboBox;  //-1 slow, 0 normal, 1 fast


    public GameArea() {
        super("Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setVisible(true);

        canvas = new Canvas();
        add(canvas);
        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
        g = (Graphics2D) bs.getDrawGraphics();

        int x = getWidth()/2;
        int y = getHeight()/2;
        snake = new Snake(x, y);

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                canvas.createBufferStrategy(2);
                bs = canvas.getBufferStrategy();
                g = (Graphics2D) bs.getDrawGraphics();

    //                //prepare background for canvas
    //                g.setColor(Color.DARK_GRAY);
    //                g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

    //                if(!gameStarted){
    //                    pressKeyMessage();
    //                    bs.show();
    //                }else if(gameOver){
    //                   gameOverMessage();
    //                    bs.show();
    //                }

            }
            public void componentMoved(ComponentEvent e) {

            }
            public void componentShown(ComponentEvent e) {

            }
            public void componentHidden(ComponentEvent e) {

            }
        });

        canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == 'w')
                    direction = 0;
                if(e.getKeyChar() == 'd')
                    direction = 1;
                if(e.getKeyChar() == 's')
                    direction = 2;
                if(e.getKeyChar() == 'a')
                    direction = 3;
//                if(e.getKeyChar() == 'e')
//                    snake.addBodyPart();
//                if(e.getKeyChar() == 'r')
//                    goal = null;
                if(e.getKeyChar() == '`') {
                    pause = true;
                    showSettings();
                }
                if(e.getKeyChar() == 'g')
                    grid = !grid;

                if(gameOver)
                    gameOver = false;
            }
            public void keyPressed (KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_F1){
                    JOptionPane.showMessageDialog(null, "Press '`' for menu.");
                }
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    pause = !pause;
                }
            }
            public void keyReleased (KeyEvent e){}
        });

        //prepare background for canvas
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        //Frames
        while(true) {
            if (!pause) {
                g = (Graphics2D) bs.getDrawGraphics();
                if (!gameOver) {
                    gameStarted = true;

                    //make sure there is goal to collect
                    if (goal == null) {
                        int weidthOfArea = getWidth();
                        int numberOfSectorsAtX = weidthOfArea / Snake.partSize;

                        int heightOfArea = getHeight();
                        int numberOfSectorsAtY = heightOfArea / Snake.partSize;

                        Random random = new Random();

                        int Xsector = random.nextInt(numberOfSectorsAtX - 10) + 5;
                        int Ysector = random.nextInt(numberOfSectorsAtY - 10) + 5;

                        goal = new Point(Xsector * Snake.partSize, Ysector * Snake.partSize);

                    }


                    //prepare background for canvas
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                    if(borderMode) {
                        g.setColor(new Color(188, 63, 60));
                        g.drawRect(1, 1, getWidth() - 1, getHeight() - 1);
                    }

                    int XpositionModifyer = 0;
                    int YpositionModifyer = 0;

                    //move last part of the snake to the top of the head
                    if (direction == 0) {                       //set YpositionModifyer to remove one 'bodysize' from y coordinate so new head facing north
                        YpositionModifyer = -Snake.partSize;
                        XpositionModifyer = 0;
                    } else if (direction == 1) {                 //set XpositionModifyer to add one 'bodysize' to x coordinate so new head facing east
                        XpositionModifyer = Snake.partSize;
                        YpositionModifyer = 0;
                    } else if (direction == 2) {                 //set YpositionModifyer to add one 'bodysize' to y coordinate so new head facing south
                        YpositionModifyer = Snake.partSize;
                        XpositionModifyer = 0;
                    } else if (direction == 3) {                //set XpositionModifyer to remove one 'bodysize' from x coordinate so new head facing west
                        XpositionModifyer = -Snake.partSize;
                        YpositionModifyer = 0;
                    }


                    //move last part to the front and add XorY modifications to make snake facing in selected direction
                    int indextToMove;
                    if (indexOfLastMovedPart == 0)
                        indextToMove = snake.getSize() - 1;
                    else
                        indextToMove = indexOfLastMovedPart - 1;
                    Point placeToMove = new Point(snake.getBodyParts().get(indexOfLastMovedPart).x + XpositionModifyer, snake.getBodyParts().get(indexOfLastMovedPart).y + YpositionModifyer);

                    if (placeToMove.x >= getWidth()) {
                        placeToMove.x -= getWidth();
                        if (borderMode)
                            gameOver(getWidth(), getHeight());
                    } else if (placeToMove.x < 0) {
                        placeToMove.x += getWidth();
                        if (borderMode)
                            gameOver(getWidth(), getHeight());
                    }

                    if (placeToMove.y >= getHeight()) {
                        placeToMove.y -= getHeight();
                        if (borderMode)
                            gameOver(getWidth(), getHeight());
                    } else if (placeToMove.y < 0) {
                        placeToMove.y += getHeight();
                        if (borderMode)
                            gameOver(getWidth(), getHeight());
                    }

                    //check if placeToGo is free or have a goal
                    if (snake.getBodyParts().contains(placeToMove)) {
                        gameOver(getWidth(), getHeight());
                    } else {

                        if (goal != null && new Rectangle(goal.x, goal.y, Snake.partSize, Snake.partSize).contains(placeToMove)) {
                            snake.addBodyPart();
                            goal = null;
                        }

                        snake.getBodyParts().get(indextToMove).x = placeToMove.x;
                        snake.getBodyParts().get(indextToMove).y = placeToMove.y;
                        indexOfLastMovedPart = indextToMove;
                    }

                    //display snake, goal, score
                    //snake
                    g.setColor(Color.yellow);
                    for (Point p : snake.bodyParts) {
                        g.drawRect(p.x, p.y, Snake.partSize, Snake.partSize);
                    }
                    //goal
                    if (goal != null) {
                        g.setColor(new Color(50, 167, 80));
                        g.drawOval(goal.x, goal.y, Snake.partSize, Snake.partSize);

                        if(grid) {
                            //show grid lines for goal
                            g.setColor(Color.red);
                            g.drawLine(goal.x+Snake.partSize/2, 0, goal.x+Snake.partSize/2, getHeight()); //x axis
                            g.drawLine(0, goal.y+Snake.partSize/2, getWidth(), goal.y+Snake.partSize/2); //y axis
                        }
                    }
                    //score
                    g.setFont(new Font(Font.SERIF , Font.PLAIN, DEFAULT_FONT_SIZE / 4 * (getWidth() - 200) / 800));
                    g.setColor(new Color(50, 167, 80));
                    g.drawString("Scores: " + snake.score, 10, g.getFontMetrics().getHeight());


                    try {
                        Thread.sleep((long) (1000 / ((int) ((snake.score / 2) + 23)*Snake.speedMultiplier)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //            g.dispose();
                } else if (!gameStarted) {
                    pressKeyMessage(g, getWidth(), getHeight());
                } else if (gameOver) {
                    gameOverMessage(g, getWidth(), getHeight());
                }

            }else{
                g.setFont(new Font(Font.SERIF, Font.PLAIN, DEFAULT_FONT_SIZE / 4 * (getWidth() - 200) / 800));
                g.setColor(Color.yellow);
                g.drawString("Pause", this.getWidth()-g.getFontMetrics().stringWidth("Pause")-50, g.getFontMetrics().getHeight());
            }
            bs.show();
        }
    }

    public void showSettings(){
        JFrame settingsFrame = new JFrame("Game Settings");
        settingsFrame.setResizable(false);
//        settingsFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        settingsFrame.setSize(250, 100);
        JPanel panSettings = new JPanel();
        settingsFrame.add(panSettings);
        panSettings.setLayout(new BoxLayout(panSettings, BoxLayout.Y_AXIS));
        JPanel panResolution = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panSettings.add(panResolution);
        panResolution.add(new JLabel("Window size:"));

        myDimension localDimension = new myDimension(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth(),
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight());

        myDimension[] dimensionsList = {new myDimension(640, 480), new myDimension(800, 600),
                new myDimension(1024, 768), new myDimension(1280, 960), new myDimension(1600, 1200), localDimension};

        dimensionJComboBox = new JComboBox<>(dimensionsList);
        dimensionJComboBox.setSelectedItem(this.getSize());
        panResolution.add(dimensionJComboBox);
//        panResolution.add(new JLabel("Full Screen:"));
//        panResolution.add(fullscreenJCheckBox = new JCheckBox());
//        fullscreenJCheckBox.set

//        JPanel panFullScreen = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        panSettings.add(panFullScreen);
//        panFullScreen.add(new JLabel("Full Screen:"));
//        panFullScreen.add(fullscreenJCheckBox = new JCheckBox());

        JPanel panMapSize = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panSettings.add(panMapSize);
        panMapSize.add(new JLabel("Map size:"));
        panMapSize.add(mapSizeJComboBox = new JComboBox<>(new String[]{"Big", "Normal", "Small"}));
        mapSizeJComboBox.setSelectedItem(snake.getMapSize());


        JPanel panSnakeSpeed = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panSettings.add(panSnakeSpeed);
        panSnakeSpeed.add(new JLabel("Difficulty: "));
        panSnakeSpeed.add(snakeSpeedJComboBox = new JComboBox<>(new String[]{"Hard", "Normal", "Easy"}));
        snakeSpeedJComboBox.setSelectedItem(snake.getDifficulty());
        panSnakeSpeed.add(new JLabel("Border Mode:"));
        panSnakeSpeed.add(bordersModeJCheckBox = new JCheckBox());
        bordersModeJCheckBox.setSelected(borderMode);

        JPanel panRun = new JPanel();
        JButton btnStart = new JButton("Apply");

        panRun.add(btnStart);
        panSettings.add(panRun);
        settingsFrame.pack();
        settingsFrame.setVisible(true);

        btnStart.addActionListener(e-> {
            settingsFrame.setVisible(false);
            pause = false;
            this.setSize((Dimension) dimensionJComboBox.getSelectedItem());
            if(mapSizeJComboBox.getSelectedItem().equals("Big"))
                Snake.setPartSize(5);
            else if(mapSizeJComboBox.getSelectedItem().equals("Normal"))
                Snake.setPartSize(10);
            else
                Snake.setPartSize(20);

            borderMode = bordersModeJCheckBox.isSelected();

            if(!snakeSpeedJComboBox.getSelectedItem().equals(snake.getDifficulty())) {
                restartGame(getWidth(), getHeight());
                if (snakeSpeedJComboBox.getSelectedItem().equals("Easy"))
                    Snake.speedMultiplier = .5;
                else if(snakeSpeedJComboBox.getSelectedItem().equals("Normal"))
                    Snake.speedMultiplier = 1;
                else
                    Snake.speedMultiplier = 2;
            }
        });
    }

//    public void resetDiminsion(int newWidth, int newHeight){
//        this.setSize();
//    }

    public static void gameOver(int w, int h){

        gameOver = true;
        snake = new Snake(w/2, h/2);
        indexOfLastMovedPart = 0;
        goal = null;

//        System.out.println(unCrypt(crypt("Hello my name is Daniil")));
    }

    public static void restartGame(int w, int h){
        snake = new Snake(w/2, h/2);
        indexOfLastMovedPart = 0;
        goal = null;
    }

    public static void gameOverMessage(Graphics2D g, int w, int h){
        //prepare background for canvas
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, w, h);

        g.setFont(new Font(Font.SERIF, Font.PLAIN, DEFAULT_FONT_SIZE*(w-200)/800));
        g.setColor(new Color(188, 63, 60));
        int messageWidth = g.getFontMetrics().stringWidth("Game Over");
        int xofMessage = (w-messageWidth)/2;
        g.drawString("Game Over", xofMessage, h/2);
    }

    public static void pressKeyMessage(Graphics2D g, int w, int h){
        //prepare background for canvas
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, w, h);

        //press any key to start
        g.setFont(new Font(Font.SERIF, Font.PLAIN, DEFAULT_FONT_SIZE/2*(w-200)/800));
        g.setColor(new Color(50, 167, 80));
        int messageWidth = g.getFontMetrics().stringWidth("Press any key to start");
        int xofMessage = (w-messageWidth)/2;
        g.drawString("Press any key to start", xofMessage, h/2);
    }

//    public static String crypt(String toCrypt){
//        char[] letters = toCrypt.toCharArray();
//        String cryptedString = "";
//
//        for(int i = 0; i < letters.length; i++){
//            cryptedString += 1/(int)letters[i] + ",";
//            System.out.println((int)letters[i]);
//        }
//        return cryptedString;
//
//    }
//
//    public static String unCrypt(String toUnCrypt){
//        String originalString = "";
//        Scanner s = new Scanner(toUnCrypt);
//        s.useDelimiter(",");
//        while (s.hasNext()){
//            char c = (char) (1/Double.parseDouble(s.next()));
//            System.out.println((int)c);
//            originalString += c;
//        }
//
//        return originalString;
//    }

//    public getBestScoreFromDataBase()
}