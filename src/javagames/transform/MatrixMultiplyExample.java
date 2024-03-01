package javagames.transform;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Random;
import javagames.util.*;
import javax.swing.*;
public class MatrixMultiplyExample extends JFrame implements Runnable{
    private static final int SCREEN_W=640;
    private static final int SCREEN_H=480;
    private FrameRate frameRate;
    private BufferStrategy bs;
    private volatile boolean running;
    private Thread gameThread;
    private RelativeMouseInput mouse;
    private KeyboardInput keyboard;
    private float earthRot,earthDelta;
    private float moonRot,moonDelta;
    private boolean showStars;
    private int[] stars;
    private Random rand=new Random();
    public MatrixMultiplyExample(){}
    protected void createAndShowGUI(){//1.create 2.show GUI 3.Thread.start()
        Canvas canvas=new Canvas();
        canvas.setSize(640,480);
        canvas.setBackground(Color.BLACK);
        canvas.setIgnoreRepaint(true);
        getContentPane().add(canvas);
        setTitle("Vector Graphics Example");
        setIgnoreRepaint(true);
        pack();
        //
        keyboard=new KeyboardInput();
        canvas.addKeyListener(keyboard);
        //
        mouse=new RelativeMouseInput(canvas);
        canvas.addMouseListener(mouse);
        canvas.addMouseMotionListener(mouse);
        canvas.addMouseWheelListener(mouse);

        setVisible(true);
        canvas.createBufferStrategy(2);
        bs=canvas.getBufferStrategy();
        canvas.requestFocus();

        gameThread=new Thread(this);
        gameThread.start();
    }
    public void run(){
        running=true;
        initialize();
        while (running)gameLoop();
    }
    private void gameLoop(){
        processInput();
        renderFrame();
        sleep(10L);
    }
    private void renderFrame(){
        do{
            do{
                Graphics g=null;
                try{
                    g=bs.getDrawGraphics();
                    g.clearRect(0,0,getWidth(),getHeight());
                    render(g);
                }finally {
                    if(g!=null)g.dispose();
                }
            }while (bs.contentsRestored());
            bs.show();
        }while (bs.contentsLost());
    }
    private void sleep(long sleep){
        try {
            Thread.sleep(sleep);
        }catch (InterruptedException ex){}
    }
    private void initialize(){
        frameRate=new FrameRate();
        frameRate.initialize();
        earthDelta=(float) Math.toRadians(0.5);
        moonDelta=(float) Math.toRadians(2.5);
        showStars=true;
        stars=new int[1000];
        for(int i=0;i<stars.length-1;i+=2){
            stars[i]=rand.nextInt(SCREEN_W);
            stars[i+1]=rand.nextInt(SCREEN_H);
        }
    }
    private void processInput(){
        keyboard.poll();
        mouse.poll();
        if(keyboard.keyDownOnce(KeyEvent.VK_SPACE))showStars=!showStars;
    }
    private void render(Graphics g){
        g.setColor(Color.GREEN);
        frameRate.calculate();
        g.drawString(frameRate.getFrameRate(),20,20);
        g.drawString("Press [SPACE] to toggle stars",20,35);
        if(showStars){
            g.setColor(Color.WHITE);
            for(int i=0;i<stars.length-1;i+=2)g.fillRect(stars[i],stars[i+1],1,1);
        }//实际上是小正方形，因为是fillRect
        //draw the sun...
        Matrix3x3f sunMat=Matrix3x3f.identity();
        sunMat=sunMat.mul(Matrix3x3f.translate(SCREEN_W/2,SCREEN_H/2));
        Vector2f sun=sunMat.mul(new Vector2f());
        g.setColor(Color.YELLOW);//oval椭圆，fillOval决定为圆形，与fillRect对比
        g.fillOval((int)sun.x-50,(int)sun.y-50,100,100);
        //draw Earth's Orbit
        g.setColor(Color.WHITE);
        g.drawOval((int)sun.x-SCREEN_W/4,(int)sun.y-SCREEN_W/4,SCREEN_W/2,SCREEN_W/2);
        //draw the Earth
        Matrix3x3f earthMat=Matrix3x3f.translate(SCREEN_W/4,0);//earthMat<---Matrix3x3f
        earthMat=earthMat.mul(Matrix3x3f.rotate(earthRot));
        earthMat=earthMat.mul(sunMat);//<<------------------
        earthRot+=earthDelta;
        Vector2f earth=earthMat.mul(new Vector2f());//earth<---Vector2f
        g.setColor(Color.BLUE);
        g.fillOval((int)earth.x-10,(int)earth.y-10,20,20);
        //draw the moon
        Matrix3x3f moonMat=Matrix3x3f.translate(30,0);
        moonMat=moonMat.mul(Matrix3x3f.rotate(moonRot));
        moonMat=moonMat.mul(earthMat);//<<----------------
        moonRot+=moonDelta;
        Vector2f moon=moonMat.mul(new Vector2f());
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval((int)moon.x-5,(int)moon.y-5,10,10);
    }
    private void onWindowClosing(){
        try{
            running=false;
            gameThread.join();
        }catch (InterruptedException e){e.printStackTrace();}
        System.exit(0);
    }

    public static void main(String[] args) {
        final MatrixMultiplyExample app=new MatrixMultiplyExample();
        app.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                app.onWindowClosing();
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                app.createAndShowGUI();
            }
        });
    }

}
