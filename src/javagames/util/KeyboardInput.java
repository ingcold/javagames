package javagames.util;
import java.awt.event.*;
public class KeyboardInput implements KeyListener{
    private boolean[] keys;
    private int[] polled;
    public KeyboardInput(){
        keys=new boolean[256];
        polled=new int[256];//poll可以理解为通过轮询按下某个按键的次数
    }                      //（实际上计算机的实现如此智障，通过巨大的工作量实现功能）
    public boolean keyDown(int keyCode){return polled[keyCode]>0;}
    public boolean keyDownOnce(int keyCode){return polled[keyCode]==1;}
    //keyDownOnce要求为1，按一次才有效，所以不能连续按
    public synchronized void poll(){//poll计票，轮询
        for(int i=0;i<keys.length;++i){
            if(keys[i])polled[i]++;
            else polled[i]=0;
        }
    }
    public synchronized void keyPressed(KeyEvent e){
        int keyCode=e.getKeyCode();
        if(keyCode>=0&&keyCode<keys.length)keys[keyCode]=true;
    }
    public synchronized void keyReleased(KeyEvent e){
        int keyCode=e.getKeyCode();
        if(keyCode>=0&&keyCode<keys.length)keys[keyCode]=false;
    }
    public void keyTyped(KeyEvent e){}//implements KeyListener,实现“全部”？
}
