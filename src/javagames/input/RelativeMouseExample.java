package javagames.input;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javagames.util.*;
import javax.swing.*;

public class RelativeMouseExample extends JFrame implements Runnable {
    private FrameRate frameRate;
    private BufferStrategy bs;
    private volatile boolean running;
    private Thread gameThread;
    private Canvas canvas;
    private RelativeMouseInput mouse;
    private KeyboardInput keyboard;
    private Point point=new Point(0,0);
    private boolean disableCursor=false;
    public RelativeMouseExample(){frameRate=new FrameRate();}
    protected void createAndShowGUI(){
        canvas=new Canvas();
        canvas.setSize(640,480);
        canvas.setBackground(Color.BLACK);
        canvas.setIgnoreRepaint(true);
        getContentPane().add(canvas);
        setTitle("Relative Mouse Example");
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
        frameRate.initialize();
        while (running)gameLoop();
    }
    public void gameLoop(){
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
    private void processInput(){
        keyboard.poll();
        mouse.poll();
        Point p=mouse.getPosition();
        if(mouse.isRelative()){
            point.x+=p.x;
            point.y+=p.y;
        }else{
            point.x=p.x;
            point.y=p.y;
        }
        if(point.x+25<0)point.x=canvas.getWidth()-1;
        else if(point.x>canvas.getWidth()-1)point.x=-25;
        if(point.y+25<0)point.y=canvas.getHeight()-1;
        else if(point.y>canvas.getHeight()-1)point.y=-25;
        //
        if(keyboard.keyDownOnce(KeyEvent.VK_SPACE))mouse.setRelative(!mouse.isRelative());
        //
        if(keyboard.keyDownOnce(KeyEvent.VK_C)){
            disableCursor=!disableCursor;
            if(disableCursor)disableCursor();
            else setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    private void render(Graphics g){
        g.setColor(Color.GREEN);
        frameRate.calculate();
        g.drawString(mouse.getPosition().toString(),20,20);
        g.drawString("Relative: "+mouse.isRelative(),20,35);
        g.drawString("Press space to switch mouse modes",20,50);
        g.drawString("Press C to toggle cursor",20,65);
        g.setColor(Color.WHITE);
        g.drawRect(point.x,point.y,25,25);
    }
    private void disableCursor(){
        Toolkit tk=Toolkit.getDefaultToolkit();
        Image image=tk.createImage("");
        Point point=new Point(0,0);
        String name="CanBeAnything";
        Cursor cursor=tk.createCustomCursor(image,point,name);
        setCursor(cursor);
    }
    protected void onWindowClosing(){
        try {
            running=false;
            gameThread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        final RelativeMouseExample app=new RelativeMouseExample();
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
