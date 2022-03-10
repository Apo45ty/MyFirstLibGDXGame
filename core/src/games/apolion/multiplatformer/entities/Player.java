package games.apolion.multiplatformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import games.apolion.multiplatformer.input.mobile.JoyStick;
import games.apolion.multiplatformer.level.AbstractLevel;

public class Player  extends GameObject {

    //statistics
    private static final float ACCELERATION = 300;
    private final long initialTime;
    private final Vector2 velocity;
    private float MAX_SPEED=3000;
    private float DRAG=0.9f;
    private final float MINVEL = 0.1f;


    //position & dimensions
    int width,height;
    private Rectangle boundingBox;

    //Graphix
    public Array<TextureRegion> playerIdle,playerRunLeft,playerRunUp,playerRunDown,playerRunRight;
    private Animation<TextureRegion> idleLoop,runLeftLoop,runUpLoop,runDownLoop,runRightLoop;
    private PlayerStates state = PlayerStates.Idle;
    private final float IDLE_LOOP_FRAME_DURATION=0.3f;
    public TextureRegion region;

    public Player(int xCenter, int yCenter,
                  int width, int height, TextureAtlas atlas) {
        super(xCenter,yCenter);
        velocity = new Vector2(0,0);
        this.width = width;
        this.height = height;
        initialTime = TimeUtils.nanoTime();

        boundingBox = new Rectangle(xPos,yPos,width,height);

        playerIdle = new Array<TextureRegion>();
        playerIdle.add(atlas.findRegion("playeridle1"));
        playerIdle.add(atlas.findRegion("playeridle2"));
        playerIdle.add(atlas.findRegion("playeridle3"));
        playerIdle.add(atlas.findRegion("playeridle4"));
        idleLoop = new Animation<TextureRegion>(IDLE_LOOP_FRAME_DURATION,playerIdle, Animation.PlayMode.LOOP);


        playerRunLeft = new Array<TextureRegion>();
        playerRunLeft.add(atlas.findRegion("playerrunleft1"));
        playerRunLeft.add(atlas.findRegion("playerrunleft2"));
        playerRunLeft.add(atlas.findRegion("playerrunleft3"));
        playerRunLeft.add(atlas.findRegion("playerrunleft4"));
        playerRunLeft.add(atlas.findRegion("playerrunleft5"));
        runLeftLoop = new Animation<TextureRegion>(IDLE_LOOP_FRAME_DURATION,playerRunLeft, Animation.PlayMode.LOOP);

        playerRunUp = new Array<TextureRegion>();
        playerRunUp.add(atlas.findRegion("playerrunup1"));
        playerRunUp.add(atlas.findRegion("playerrunup2"));
        playerRunUp.add(atlas.findRegion("playerrunup3"));
        runUpLoop = new Animation<TextureRegion>(IDLE_LOOP_FRAME_DURATION,playerRunUp, Animation.PlayMode.LOOP);

        playerRunDown = new Array<TextureRegion>();
        playerRunDown.add(atlas.findRegion("playerrundown1"));
        playerRunDown.add(atlas.findRegion("playerrundown2"));
        playerRunDown.add(atlas.findRegion("playerrundown3"));
        playerRunDown.add(atlas.findRegion("playerrundown4"));
        runDownLoop = new Animation<TextureRegion>(IDLE_LOOP_FRAME_DURATION,playerRunDown, Animation.PlayMode.LOOP);

        playerRunRight = new Array<TextureRegion>();
        playerRunRight.add(atlas.findRegion("playerrunright"));
        playerRunRight.add(atlas.findRegion("playerrunright2"));
        playerRunRight.add(atlas.findRegion("playerrunright3"));
        playerRunRight.add(atlas.findRegion("playerrunright4"));
        runRightLoop = new Animation<TextureRegion>(IDLE_LOOP_FRAME_DURATION,playerRunRight, Animation.PlayMode.LOOP);

    }

    public void draw(Batch batch, Viewport viewport){
        float elapsedTime = MathUtils.nanoToSec * (TimeUtils.nanoTime() - initialTime);
        region = idleLoop.getKeyFrame(elapsedTime);
        switch (state){
            case RunningRight:
                region = runRightLoop.getKeyFrame(elapsedTime);
                break;
            case RunningLeft:
                region = runLeftLoop.getKeyFrame(elapsedTime);
                break;
            case RunningDown:
                region = runDownLoop.getKeyFrame(elapsedTime);
                break;
            case RunningUp:
                region = runUpLoop.getKeyFrame(elapsedTime);
                break;
            case Idle:
            default:
                break;
        }

        batch.draw(region,
                viewport.getWorldWidth() / 2 - region.getRegionWidth() / 2,
                viewport.getWorldHeight() / 2 - region.getRegionHeight() / 2,width,height);
//        Gdx.app.log(Player.class.getName(),"State:"+state);
//        Gdx.app.log(Player.class.getName(),region.toString());

    }

    @Override
    public void draw(Batch batch) {
        batch.draw(playerIdle.get(0),xPos,yPos,width,height);
    }

    @Override
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    @Override
    public boolean intersects(Rectangle rect) {
        return this.getBoundingBox().overlaps(rect);
    }

    public void move(int deltaX,int deltaY){
        xPos+=deltaX;
        yPos+=deltaY;
        boundingBox.set(xPos,yPos,width,height);
    }

    public Vector2 readInputProcessCollision(AbstractLevel level, JoyStick joy, float delta) {
        //Handle player movement
        int moveAmountX = 0;
        int moveAmountY = 0;
        float horizontalAxis = joy.getAxis("Horizontal");
        float verticalAxis = joy.getAxis("Vertical");
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            horizontalAxis = -1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)){
            horizontalAxis = 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            verticalAxis = 1;
        }else if (Gdx.input.isKeyPressed(Input.Keys.S)){
            verticalAxis = -1;
        }
        velocity.x += delta * ACCELERATION * horizontalAxis;
        velocity.y += delta * ACCELERATION * verticalAxis;
        // Use velocity.clamp() to limit the total speed to MAX_SPEED
        velocity.clamp(0, MAX_SPEED);

        velocity.x -= delta * DRAG * velocity.x;
        velocity.y -= delta * DRAG * velocity.y;

        //Gdx.app.log(Player.class.getName(),"Vel:"+velocity);

        if(horizontalAxis>MINVEL){
            state = PlayerStates.RunningRight;
        } else if(horizontalAxis<-MINVEL){
            state=PlayerStates.RunningLeft;
        }else if(verticalAxis>MINVEL){
            state=PlayerStates.RunningUp;
        }else if(verticalAxis<-MINVEL){
            state=PlayerStates.RunningDown;
        } else {
            state = PlayerStates.Idle;
        }


        moveAmountX += delta * velocity.x;
        moveAmountY += delta * velocity.y;

        Vector2 allowedMoveInLevel = level.TryAndMove(moveAmountX,moveAmountY);
        moveAmountX = (int)allowedMoveInLevel.x;
        moveAmountY = (int)allowedMoveInLevel.y;

        move(moveAmountX,moveAmountY);
        return new Vector2(moveAmountX,moveAmountY);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Vector2 getVelocity() {
        return velocity;
    }
}
