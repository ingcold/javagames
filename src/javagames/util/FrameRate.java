package javagames.util;

public class FrameRate {
    private String frameRate;
    private long lastTime;
    private long delta;
    private int frameCount;
    public void initialize(){
        lastTime=System.currentTimeMillis();
        frameRate="FPS 0";
    }
    public void calculate(){
        long current=System.currentTimeMillis();
        delta+=current-lastTime;//时时刻刻都要计算，通过蛮力实现，因为计算机是离散的，实际上完全准确的计时几乎不可能实现
        lastTime=current;
        frameCount++;
        if(delta>1000){
            delta-=1000;
            frameRate=String.format("FPS %s",frameCount);
            frameCount=0;
        }
    }
    public String getFrameRate(){
        return frameRate;
    }
}

/*
currentTimeMills(),String.format之类的也没有要import什么包
 */
