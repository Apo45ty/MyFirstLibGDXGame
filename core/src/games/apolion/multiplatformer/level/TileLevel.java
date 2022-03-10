package games.apolion.multiplatformer.level;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import games.apolion.multiplatformer.entities.GameObject;
import games.apolion.multiplatformer.entities.Player;

import java.util.ArrayList;
import java.util.List;

public class TileLevel extends AbstractLevel {
    List<Tile> tiles=new ArrayList<Tile>();
    GameObject player;
    Camera camera;

    public TileLevel(Player player, Camera camera, int x, int y, LevelLayout levelLayout,  TextureAtlas atlas) {
        super(x,y);
        this.player = player;
        this.camera = camera;

        tiles.add(new StaticTile(0,0,50,50,atlas.findRegion("tile"),camera,player));
    }

    public void setPlayerToTiles(GameObject player) {
    }
    public void setCameraToTiles(Camera player) {
    }
    @Override
    public void draw(Batch batch, Viewport viewport) {
        draw(batch);
    }

    @Override
    public void draw(Batch batch) {
        for(Tile t: tiles){
            t.draw(batch);
        }
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
    public Vector2 TryAndMove( int moveAmountX, int moveAmountY) {
        return new Vector2(moveAmountX,moveAmountY);
    }

    @Override
    public void show() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean InvokeAction(String action, String data) {
        return false;
    }
}
