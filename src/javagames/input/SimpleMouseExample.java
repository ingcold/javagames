package javagames.input;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javagames.util.*;
import javax.swing.*;

public class SimpleMouseExample extends JFrame implements Runnable{
    private FrameRate frameRate;
    private BufferStrategy bs;
    private volatile boolean running;
    private Thread gameThread;
    private SimpleMouseInput mouse;
    private KeyboardInput keyboard;
    private ArrayList<Point> lines=new ArrayList<Point>();
    private boolean drawingLine;
    private Color[] COLORS={
            Color.RED,
            Color.GREEN,
            Color.YELLOW,
            Color.BLUE
    };
    private int colorIndex;
    public SimpleMouseExample(){frameRate=new FrameRate();}
    protected void createAndShowGUI(){
        Canvas canvas=new Canvas();
        canvas.setSize(640,480);
        canvas.setBackground(Color.BLACK);
        canvas.setIgnoreRepaint(true);
        getContentPane().add(canvas);
        setTitle("Simple Mouse Example");
        setIgnoreRepaint(true);
        pack();
        //
        keyboard=new KeyboardInput();
        canvas.addKeyListener(keyboard);
        //
        mouse=new SimpleMouseInput();
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
        while(running)gameLoop();
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
            }while(bs.contentsRestored());
            bs.show();
        }while(bs.contentsLost());
    }
    private void sleep(long sleep){
        try {
            Thread.sleep(sleep);
        }catch (InterruptedException ex){}
    }
    private void processInput(){
        keyboard.poll();
        mouse.poll();
        if(keyboard.keyDownOnce(KeyEvent.VK_SPACE)) System.out.println("VK_SPACE");
        if(mouse.buttonDownOnce(MouseEvent.BUTTON1))drawingLine=true;
        if(mouse.buttonDownOnce(MouseEvent.BUTTON1)){
            lines.add(mouse.getPosition());
        }else if(drawingLine){
            lines.add(null);
            drawingLine=false;
        }
        if(keyboard.keyDownOnce(KeyEvent.VK_C))lines.clear();
    }
    private void render(Graphics g){
        colorIndex+=mouse.getNotches();
        Color color=COLORS[Math.abs(colorIndex%COLORS.length)];
        g.setColor(color);
        frameRate.calculate();
        g.drawString(frameRate.getFrameRate(),30,30);
        g.drawString("Use mouse to draw lines",30,45);
        g.drawString("Press C to clear lines",30,60);
        g.drawString("Mouse Wheel cycles colors",30,75);
        g.drawString(mouse.getPosition().toString(),30,90);
        for(int i=0;i<lines.size()-1;++i){
            Point p1=lines.get(i);
            Point p2=lines.get(i+1);
            if(!(p1==null||p2==null))g.drawLine(p1.x,p1.y,p2.x,p2.y);
        }
    }
    protected void onWindowClosing(){
        try{
            running=false;
            gameThread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        final SimpleMouseExample app=new SimpleMouseExample();
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
