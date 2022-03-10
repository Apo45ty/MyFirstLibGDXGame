package games.apolion.multiplatformer.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class GameObject implements Drawable {
    public GameObject(int x,int y){
        this.yPos=y;
        this.xPos=x;
    }
    protected int xPos,yPos;
    public int getX() {return xPos;}
    public int getY(){ return yPos;}

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public abstract void draw(Batch batch, Viewport viewport);
    public abstract void draw(Batch batch);
    public abstract com.badlogic.gdx.math.Rectangle getBoundingBox();
    public abstract boolean intersects(Rectangle rect);


}
