import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener{
    int height = 400;
    int width = 400;
    int maxDots = 1600;
    int dotsize = 10;
    int initialdots;
    int []x = new int[maxDots];
    int []y = new int[maxDots];
    int apple_x;
    int apple_y;

    Image body, head, apple;
    Timer timer;
    int DELAY = 150;
    boolean leftDirection =  true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;
    boolean inGame = true;

    Board(){
        TAdapter tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
        setPreferredSize(new Dimension(width,height));
        setBackground(Color.black);
        initializeGame();
        loadImages();
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void initializeGame(){
        initialdots = 3;

        x[0] = 50;
        y[0] = 50;

        //initialize snake's position
        for(int i = 0; i < initialdots; i++){
            x[i] = x[0] + dotsize*i;
            y[i] = y[0];
        }

        //initialize apple;s position
        locateApple();

    }

    //load images to Images class
    public void loadImages(){
        ImageIcon bodyIcon = new ImageIcon("src/resources/dot.png");
        body = bodyIcon.getImage();
        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();
        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();
    }
    //draw images at particular coordintees
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Drawing(g);
    }

    public void Drawing(Graphics g){
        if(inGame){
            g.drawImage(apple, apple_x, apple_y, this);
            for(int i = 0; i < initialdots; i++){
                if(i == 0){
                    g.drawImage(head, x[0], y[0], this);
                }else{
                    g.drawImage(body, x[i], y[i], this);
                }
            }
        }else{
            GameOver(g);
            timer.stop();
        }

    }

    //Random position of Apple
    public void locateApple(){
        apple_x = ((int)(Math.random()*39))* dotsize;
        apple_y =  ((int)(Math.random()*39))* dotsize;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame){
            checkCollison();
            SnakeEat();
            move();
        }
        repaint();
    }

    public void GameOver(Graphics g){
        String msg = "Game Over";
        int scores = (initialdots - 3)*100;
        String scoremsg = "Score:"+Integer.toString(scores);
        Font small = new Font("Arial", Font.BOLD, 14);
        FontMetrics fontMetrics = getFontMetrics(small);
        g.setColor(Color.RED);
        g.setFont(small);
        g.drawString(msg,(width - fontMetrics.stringWidth(msg))/2 , height/4);
        g.drawString(scoremsg,(width - fontMetrics.stringWidth(scoremsg))/2 , (3*height)/4);
    }

    //check collison with body and border
    public void checkCollison(){
        for(int i = 1; i < initialdots; i++) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
            if(x[0] < 0 || x[0] >= width || y[0] < 0 || y[0] >= height) {
                inGame = false;
            }
    }

    public void move(){
        for(int i = initialdots - 1; i > 0 ; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(leftDirection){
            x[0] -= dotsize;
        }
        if(rightDirection){
            x[0] += dotsize;
        }
        if(upDirection){
            y[0] -= dotsize;
        }
        if(downDirection){
            y[0] += dotsize;
        }
    }

    public void SnakeEat(){
        if(apple_x == x[0] && apple_y == y[0]){
            initialdots++;
            locateApple();
        }
    }
    private class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key = keyEvent.getKeyCode();

            if(key==keyEvent.VK_LEFT&&!rightDirection){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if(key==keyEvent.VK_RIGHT&&!leftDirection){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key==keyEvent.VK_UP&&!downDirection){
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }

            if(key==keyEvent.VK_DOWN&&!upDirection){
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
}
