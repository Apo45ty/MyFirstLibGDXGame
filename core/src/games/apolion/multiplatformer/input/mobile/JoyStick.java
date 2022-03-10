package games.apolion.multiplatformer.input.mobile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import games.apolion.multiplatformer.entities.Drawable;
import games.apolion.multiplatformer.screens.GameScreen;

public class JoyStick extends InputAdapter implements Drawable {
    private final int frameWidth,frameHeight,ballWidth,ballHeight;
    private final Camera cam;
    public TextureRegion frameTex;
    public TextureRegion ballTex;
    public int x,y,BallX,BallY;
    private float height,width,camInitX,camInitY;
    private Viewport viewport;
    private boolean dragging = false;


    private String  TAG = JoyStick.class.getName();

    public JoyStick(TextureRegion frameTex, TextureRegion ballTex, int x, int y, float width, float height, Viewport viewport) {
        this.frameTex = frameTex;
        this.ballTex = ballTex;
        this.x = x;
        this.y = y;
        frameWidth = frameTex.getTexture().getWidth();
        frameHeight = frameTex.getTexture().getHeight();
        ballWidth = ballTex.getTexture().getWidth();
        ballHeight = ballTex.getTexture().getHeight();
        this.width = width ;
        this.height = height;
        this.viewport=viewport;
        BallX = (int)Math.ceil(x+width/2-width/8);
        BallY = (int)Math.ceil(y+height/2-height/8);
        cam = viewport.getCamera();
    }
    public float getAxis(String axis){
        if(axis.equalsIgnoreCase("Horizontal")){
            return (BallX-(x+width/2-width/8))/width;
        } else if(axis.equalsIgnoreCase("Vertical")){
            return (BallY-(y+height/2-height/8))/height;
        }
        return 0f;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 position = viewport.unproject(new Vector2(screenX,screenY));
//        Gdx.app.log(TAG,"X:"+ position.x+" Y:"+position.y+" jX:"+x+
//                " jY:"+y+" jW:"+width+" jH:"+height+ " cX:"+cam.position.x+ " cY:"+cam.position.y);

        if(position.x>x&&position.x<x+width
        &&position.y>y&&position.y<y+height){
            Gdx.app.log(TAG,"Lol");
            dragging = true;

        }
        return true;
    }
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        BallX = (int)Math.ceil(x+width/2-width/8);
        BallY = (int)Math.ceil(y+height/2-height/8);
        dragging = false;
        return true;
    }

    @Override
    public void draw(Batch batch, Viewport viewport) {

    }

    @Override
    public void draw(Batch batch) {
        if(dragging)
        {
            Vector3 position = viewport.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0));
            if(position.x>x&&position.x<x+width
                    &&position.y>y&&position.y<y+height){
                BallX = (int)position.x;
                BallY = (int)position.y;
            }
        }
        batch.draw(frameTex,x,y,width,width);
        batch.draw(ballTex,BallX,BallY,width/4,height/4);
    }


}
