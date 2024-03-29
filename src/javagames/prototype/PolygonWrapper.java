package javagames.prototype;

import java.util.List;
import javagames.util.Matrix3x3f;
import javagames.util.Vector2f;
public class PolygonWrapper {
    private float worldWidth;
    private float worldHeight;
    private Vector2f worldMin;
    private Vector2f worldMax;
    public PolygonWrapper(float worldWidth,float worldHeight){
        this.worldWidth=worldWidth;
        this.worldHeight=worldHeight;
        worldMax=new Vector2f(worldWidth/2.0f,worldHeight/2.0f);
        worldMin=worldMax.inv();
    }
    public boolean hasLeftWorld(Vector2f position){
        return position.x<worldMin.x||position.x>worldMax.x||
                position.y<worldMin.y||position.y>worldMax.y;
    }
    public Vector2f wrapPosition(Vector2f position){
        Vector2f wrapped=new Vector2f(position);
        if(position.x<worldMin.x){
            wrapped.x=position.x+worldWidth;
        }else if (position.x>worldMax.x){wrapped.x=position.x-worldWidth;}
        if(position.y<worldMin.y){
            wrapped.y=position.y+worldHeight;
        }else if (position.y>worldMax.y){wrapped.y=position.y-worldHeight;}
        return wrapped;//position-->wrap()-->return wrapped
    }
    public void wrapPolygon(Vector2f[] poly,List<Vector2f[]> renderList){
        Vector2f min=getMin(poly);//对于wrapPolygon，设想一个超越边界的多边形
        Vector2f max=getMax(poly);//并不需要减去原来超越边界的点，屏幕本身会不显示
        boolean north=max.y>worldMax.y;//要做的是显示新增加的，即renderList.add()
        boolean south=min.y<worldMin.y;
        boolean west=min.x<worldMin.x;
        boolean east=max.x>worldMax.x;
        if(west)renderList.add(wrapEast(poly));
        if(east)renderList.add(wrapWest(poly));
        if(north)renderList.add(wrapSouth(poly));
        if(south)renderList.add(wrapNorth(poly));
        if(north&&west)renderList.add(wrapSouthEast(poly));
        if(north&&east)renderList.add(wrapSouthWest(poly));
        if(south&&west)renderList.add(wrapNorthEast(poly));
        if(south&&east)renderList.add(wrapNorthWest(poly));
    }
    private Vector2f getMin(Vector2f[] poly){
        Vector2f min=new Vector2f(Float.MAX_VALUE,Float.MAX_VALUE);
        for(Vector2f v:poly){
            min.x=Math.min(v.x,min.x);
            min.y=Math.min(v.y,min.y);
        }
        return min;
    }
    private Vector2f getMax(Vector2f[] poly){
        Vector2f max=new Vector2f(-Float.MAX_VALUE,-Float.MAX_VALUE);
        for(Vector2f v:poly){
            max.x=Math.max(v.x,max.x);
            max.y=Math.max(v.y,max.y);
        }
        return max;
    }
    private Vector2f[] wrapNorth(Vector2f[] poly){return transform(poly,Matrix3x3f.translate(0.0f,worldHeight));}
    private Vector2f[] wrapSouth(Vector2f[] poly){return transform(poly,Matrix3x3f.translate(0.0f,-worldHeight));}
    private Vector2f[] wrapEast(Vector2f[] poly){return transform(poly,Matrix3x3f.translate(worldWidth,0.0f));}
    private Vector2f[] wrapWest(Vector2f[] poly){return transform(poly,Matrix3x3f.translate(-worldWidth,0.0f));}
    private Vector2f[] wrapNorthWest(Vector2f[] poly){
        return transform(poly,Matrix3x3f.translate(-worldWidth,worldHeight));
    }
    private Vector2f[] wrapNorthEast(Vector2f[] poly){
        return transform(poly,Matrix3x3f.translate(worldWidth,worldHeight));
    }
    private Vector2f[] wrapSouthEast(Vector2f[] poly){
        return transform(poly,Matrix3x3f.translate(worldWidth,-worldHeight));
    }
    private Vector2f[] wrapSouthWest(Vector2f[] poly){
        return transform(poly,Matrix3x3f.translate(-worldWidth,-worldHeight));
    }
    private Vector2f[] transform(Vector2f[] poly,Matrix3x3f mat){
        Vector2f[] copy=new Vector2f[poly.length];
        for(int i=0;i< poly.length;++i)copy[i]=mat.mul(poly[i]);
        return copy;
    }
}
