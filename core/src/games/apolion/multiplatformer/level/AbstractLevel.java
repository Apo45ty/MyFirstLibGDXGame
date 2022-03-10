package games.apolion.multiplatformer.level;

import com.badlogic.gdx.math.Vector2;
import games.apolion.multiplatformer.entities.GameObject;
import games.apolion.multiplatformer.entities.Player;

public abstract class AbstractLevel  extends GameObject {
    public AbstractLevel(int x,int y){
        super(x,y);
    }

    public abstract Vector2 TryAndMove( int moveAmountX, int moveAmountY);
    public abstract void show();
    public abstract void update(float delta);
    public abstract void dispose();

    public enum LevelLayout{
        Test
    }

    public abstract boolean InvokeAction(String action,String data);
}