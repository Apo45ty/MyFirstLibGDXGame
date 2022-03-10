package games.apolion.multiplatformer.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import games.apolion.multiplatformer.entities.GameObject;
import games.apolion.multiplatformer.entities.Player;

public abstract class Tile extends GameObject {
    //Location info
    float minRenderDistanceFactor = 1000f;
    float width,height;
    private static Player player;
    private static Camera camera;

    public Tile(int x, int y, int width, int height, Camera cam, Player player){
        super(x,y);
        this.width=width;
        this.height=height;
        setCameraObject(cam);
        setPlayer(player);
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setCameraObject(Camera cam){
        this.camera = cam;
    }
    public void setPlayer(Player player){
        this.player = player;
    }
    public abstract TextureRegion getTextureRegion();

    @Override
    public void draw(Batch batch) {
        if(camera!=null){
            float camWidth = camera.viewportWidth;
            float camHeight = camera.viewportHeight;
//            Gdx.app.log(""," w"+camWidth+" h"+camHeight);
            float camx = player.getX();
            float camy = player.getY();
            if((camHeight+camy+minRenderDistanceFactor)>yPos&&(camy-minRenderDistanceFactor)<yPos){
                if(camWidth+camx+minRenderDistanceFactor>xPos&&camx-minRenderDistanceFactor<xPos){
                    if(player.region!=null)
                        batch.draw(getTextureRegion(),camWidth / 2 -xPos-player.getX()-player.getWidth() / 2,camHeight/2-yPos-player.getY()-player.getHeight() ,this.width,this.height);
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, Viewport viewport) {
        draw(batch);
    }
}
