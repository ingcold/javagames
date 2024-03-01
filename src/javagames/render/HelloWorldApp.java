package javagames.render;

import java.awt.*;
import javax.swing.*;
import javagames.util.*;

public class HelloWorldApp extends JFrame{
    private FrameRate frameRate;
    public HelloWorldApp(){frameRate=new FrameRate();}
    protected void createAndShowGUI(){
        GamePanel gamePanel=new GamePanel();
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setPreferredSize(new Dimension(320,240));
        getContentPane().add(gamePanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Hello World");
        pack();
        frameRate.initialize();
        setVisible(true);
    }
    private class GamePanel extends JPanel{
        public void paint(Graphics g){
            super.paint(g);
            onPaint(g);
        }
    }
    protected void onPaint(Graphics g){
        frameRate.calculate();
        g.setColor(Color.WHITE);
        g.drawString(frameRate.getFrameRate(),30,30);
        repaint();
    }
    public static void main(String[] args){
        final HelloWorldApp app=new HelloWorldApp();
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                app.createAndShowGUI();
            }
        });
    }

}
/*2023-10-22
依赖包javagames.rener是自己手动写的
拼写错误JPanel
import java.~.*;
import java.~.~;
 */
//2023-11-19不放入脑子里，确实就是跟没见过一样