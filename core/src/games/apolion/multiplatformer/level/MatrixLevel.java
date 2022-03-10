package games.apolion.multiplatformer.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import games.apolion.multiplatformer.entities.GameObject;
import games.apolion.multiplatformer.entities.Player;
import games.apolion.multiplatformer.input.mobile.JoyStick;
import games.apolion.multiplatformer.utils.Constants;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MatrixLevel extends AbstractLevel {
    private final Player  player;
    private final int tileWidth;
    private final int tileHeigh;
    public Camera camera;
    public HashMap<Integer,Tile> tilesMap = new LinkedHashMap<Integer,Tile>();


    private String  TAG = MatrixLevel.class.getName();

    short[][] tileMatrix = new short[100][100];
    public MatrixLevel(Player player, Camera camera, int x, int y, LevelLayout levelLayout, TextureAtlas atlas,int tileWidth,int tileHeigh) {
        super( x, y);

        this.player = player;
        this.camera = camera;
        this.tileWidth = tileWidth;
        this.tileHeigh = tileHeigh;
        tilesMap.put(1,new StaticTile(0,0,1*tileWidth,1*tileHeigh,atlas.findRegion("tile"),camera,player));
    }

    public short[][] getTileMatrix() {
        return tileMatrix;
    }

    public void setTileMatrix(short[][] tileMatrix) {
        this.tileMatrix = tileMatrix;
    }

    @Override
    public void draw(Batch batch, Viewport viewport) {
        draw(batch);
    }

    @Override
    public void draw(Batch batch) {
        for(int i =0 ;i<tileMatrix.length;i++){
            for(int j=0;j<tileMatrix[i].length;j++){
                if(tileMatrix[i][j]==1){
                    Tile t = tilesMap.get(1);
                    t.setWidth(tileWidth);
                    t.setHeight(tileHeigh);
                    t.setxPos(this.xPos-j*tileWidth);
                    t.setyPos(this.yPos+i*tileHeigh);
                    t.draw(batch);
                }
            }
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
    public Vector2 TryAndMove(int moveAmountX, int moveAmountY) {
        float nextXRight = (player.getX()+moveAmountX-this.xPos+player.getWidth())/(float)tileWidth;
        float nextXLeft = (player.getX()-moveAmountX-this.xPos)/(float)tileWidth;
        float nextYUp = (this.yPos- player.getY()+moveAmountY)/(float)tileHeigh;
        float nextYDown = (this.yPos- (player.getY()+moveAmountY-player.getHeight()))/(float)tileHeigh;
//        if(nextXLe>=tileMatrix.length){
//            Gdx.app.log(TAG,"X:"+nextX+" Y:"+nextY);
//        }
        boolean isOutsideYUp = nextYUp<0&&moveAmountY>0;
        boolean isOutsideYDown = nextYDown>=tileMatrix.length;
        if(isOutsideYUp){
            player.getVelocity().y = 0;
            player.setyPos(player.getY()-Constants.EdgeTeleportDistance);
            moveAmountY = 0;
            nextYUp = 0;
        } else if(isOutsideYDown){
            player.getVelocity().y = 0;
            player.setyPos(player.getY()+Constants.EdgeTeleportDistance);
            moveAmountY = 0;
            nextYDown = 0;
        }
        boolean isOutsideXLeft = nextXLeft<0&&moveAmountX<0;
        boolean isOutsideXRight = nextXRight>=tileMatrix[(int)nextYDown].length&&moveAmountX>0;
        if(isOutsideXLeft){
            //player.setxPos(player.getX()-moveAmountX);
            moveAmountX = 0;
        } else if(isOutsideXRight){
           // player.setxPos(player.getX()-moveAmountX);
            moveAmountX = 0;
        }
        if(!isOutsideXLeft&&!isOutsideXRight&&!isOutsideYUp&&!isOutsideYDown){
            if(tileMatrix[(int)nextYUp][(int)nextXRight]<1&&moveAmountX>0){
                player.getVelocity().x = 0;
//                player.setxPos(player.getX()-moveAmountX);
                moveAmountY = 0;
                moveAmountX = 0;
            }
            if(tileMatrix[(int)nextYUp][(int)nextXRight]<1&&moveAmountY>0){
                player.getVelocity().y = 0;
                player.setyPos(player.getY()-Constants.EdgeTeleportDistance);
                moveAmountY = 0;
                moveAmountX = 0;
            }
            if(tileMatrix[(int)nextYUp][(int)nextXRight]<1&&moveAmountY<0){
                player.getVelocity().y = 0;
                player.setyPos(player.getY()- Constants.EdgeTeleportDistance);
                moveAmountY = 0;
                moveAmountX = 0;
            }

            if(tileMatrix[(int)nextYUp][(int)nextXLeft]<1&&moveAmountX<0){
                player.getVelocity().x = 0;
//                player.setxPos(player.getX()-moveAmountX);
                moveAmountY = 0;
                moveAmountX = 0;
            }
            if(tileMatrix[(int)nextYUp][(int)nextXLeft]<1&&moveAmountY<0){
                player.getVelocity().y = 0;
                player.setyPos(player.getY()-Constants.EdgeTeleportDistance);
                moveAmountY = 0;
                moveAmountX = 0;
            }
            if(tileMatrix[(int)nextYUp][(int)nextXLeft]<1&&moveAmountY>0){
                player.getVelocity().y = 0;
                player.setyPos(player.getY()-Constants.EdgeTeleportDistance);
                moveAmountY = 0;
                moveAmountX = 0;
            }
            ///////////////////////////////////////////////
            if(tileMatrix[(int)nextYDown][(int)nextXRight]<1&&moveAmountX>0){
                player.getVelocity().x = 0;
//                player.setxPos(player.getX()-moveAmountX);
                moveAmountY = 0;
                moveAmountX = 0;
            }
            if(tileMatrix[(int)nextYDown][(int)nextXRight]<1&&moveAmountY>0){
                player.getVelocity().y = 0;
                player.setyPos(player.getY()+Constants.EdgeTeleportDistance);
                moveAmountY = 0;
                moveAmountX = 0;
            }
            if(tileMatrix[(int)nextYDown][(int)nextXRight]<1&&moveAmountY<0){
                player.getVelocity().y = 0;
                player.setyPos(player.getY()+ Constants.EdgeTeleportDistance);
                moveAmountY = 0;
                moveAmountX = 0;
            }

            if(tileMatrix[(int)nextYDown][(int)nextXLeft]<1&&moveAmountX<0){
                player.getVelocity().x = 0;
//                player.setxPos(player.getX()-moveAmountX);
                moveAmountY = 0;
                moveAmountX = 0;
            }
            if(tileMatrix[(int)nextYDown][(int)nextXLeft]<1&&moveAmountY<0){
                player.getVelocity().y = 0;
                player.setyPos(player.getY()+Constants.EdgeTeleportDistance);
                moveAmountY = 0;
                moveAmountX = 0;
            }
            if(tileMatrix[(int)nextYDown][(int)nextXLeft]<1&&moveAmountY>0){
                player.getVelocity().y = 0;
                player.setyPos(player.getY()+Constants.EdgeTeleportDistance);
                moveAmountY = 0;
                moveAmountX = 0;
            }
        }
//        Gdx.app.log(TAG,this.yPos+" X:"+nextXRight+" "+moveAmountX+" Y:"+nextYDown+" "+moveAmountY+" total:"+(this.yPos- player.getY()+moveAmountY-player.getHeight())+" total:"+((this.yPos- player.getY()+moveAmountY-player.getHeight())/tileHeigh));

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
