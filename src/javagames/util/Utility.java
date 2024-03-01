package javagames.util;
//javagames.util内部，不需要import javagames.util
import java.awt.*;
import java.util.List;
//Utility这个类，全都是static的函数，就是说，把很多有用的杂类工具放到了这个Utility里面
//要使用的话，就直接用Utility这个类了，而不会生成Utility的instance对象
public class Utility {
    public static Matrix3x3f createViewport(
            float worldWidth,float worldHeight,
            float screenWidth,float screenHeight){
        float sx=(screenWidth-1)/worldWidth;
        float sy=(screenHeight-1)/worldHeight;
        float tx=(screenWidth-1)/2.0f;
        float ty=(screenHeight-1)/2.0f;
        Matrix3x3f viewport=Matrix3x3f.scale(sx,-sy);
        viewport=viewport.mul(Matrix3x3f.translate(tx,ty));
        return viewport;
    }
    public static Matrix3x3f createReverseViewport(
            float worldWidth,float worldHeight,
            float screenWidth,float screenHeight){
        float sx=worldWidth/(screenWidth-1);
        float sy=worldHeight/(screenHeight-1);
        float tx=(screenWidth-1)/2.0f;
        float ty=(screenHeight-1)/2.0f;
        Matrix3x3f viewport=Matrix3x3f.translate(-tx,-ty);
        viewport=viewport.mul(Matrix3x3f.scale(sx,-sy));
        return viewport;
    }
    public static void drawPolygon(Graphics g,Vector2f[] polygon){
        Vector2f P;
        Vector2f S=polygon[polygon.length-1];//P先行，跟随polygon[i],S跟随P,从S向P逐个画线
        for(int i=0;i<polygon.length;++i){
            P=polygon[i];
            g.drawLine((int)S.x,(int)S.y,(int)P.x,(int)P.y);
            S=P;
        }
    }
    public static void drawPolygon(Graphics g,List<Vector2f> polygon){
        Vector2f S=polygon.get(polygon.size()-1);
        for(Vector2f P:polygon){
            g.drawLine((int)S.x,(int)S.y,(int)P.x,(int)P.y);
            S=P;
        }
    }
    public static void fillPolygon(Graphics2D g,Vector2f[] polygon){
        Polygon p=new Polygon();
        for(Vector2f v:polygon)p.addPoint((int)v.x,(int)v.y);
        g.fill(p);//External Library->java.desktop.java.awt.Graphics2D
    }
    public static void fillPolygon(Graphics2D g,List<Vector2f>polygon){
        Polygon p=new Polygon();
        for (Vector2f v:polygon)p.addPoint((int)v.x,(int)v.y);
        g.fill(p);
    }
    public static int drawString(Graphics g,int x,int y,String str){
        return drawString(g,x,y,new String[]{str});
    }
    public static int drawString(Graphics g,int x,int y,List<String> str){
        return drawString(g,x,y,str.toArray(new String[0]));
    }
    public static int drawString(Graphics g,int x,int y,String... str){//... 是什么？
        FontMetrics fm=g.getFontMetrics();
        int height=fm.getAscent()+fm.getDescent()+fm.getLeading();
        for(String s:str){
            g.drawString(s,x,y+fm.getAscent());
            y+=height;
        }
        return y;
    }
}
