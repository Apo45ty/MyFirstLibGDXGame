package games.apolion.multiplatformer.level;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import games.apolion.multiplatformer.entities.GameObject;
import games.apolion.multiplatformer.entities.Player;

public class StaticTile extends Tile{

    private final TextureRegion textureRegion;

    public StaticTile(int x, int y, int width, int height, TextureRegion textureRegion, Camera cam, Player player){
        super(x,y,width,height,cam,player);
        this.textureRegion = textureRegion;
    }
    @Override
    public Rectangle getBoundingBox() {
        return null;
    }

    @Override
    public boolean intersects(Rectangle rect) {
        return false;
    }

    @Override
    public TextureRegion getTextureRegion(){
        return textureRegion;
    }

}
