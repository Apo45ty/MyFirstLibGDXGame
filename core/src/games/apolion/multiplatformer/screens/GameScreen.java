package games.apolion.multiplatformer.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import games.apolion.multiplatformer.Main;
import games.apolion.multiplatformer.entities.Drawable;
import games.apolion.multiplatformer.entities.GameObject;
import games.apolion.multiplatformer.entities.Player;
import games.apolion.multiplatformer.input.mobile.JoyStick;
import games.apolion.multiplatformer.level.AbstractLevel;
import games.apolion.multiplatformer.level.NetworkedMatricLevelActions;
import games.apolion.multiplatformer.utils.LevelLoader;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends InputAdapter implements Screen {

    private final Stage stage;
    private List<GameObject> otherGameObjects = new ArrayList<GameObject>();
    //UI variables
    private final games.apolion.multiplatformer.input.mobile.JoyStick joy;
    private boolean isFocusedOnChat = true;

    //Change Level change what is displayed
    private final AbstractLevel level;

    //Screen transition variables
    public boolean escIsPressed = false;


    //Screen Variables
    private OrthographicCamera camera;

    //Graphics
    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private TextureRegion[] backgrounds ;
    private final BitmapFont font;

    //Timing
    private float[] backgroundOffsets={0,0,0,0,0,0,0,0,0};
    private float backgroundMaScrollingSpeed;
    private long initialTime ;

    //World parameters
    public int WORLD_WIDTH = 1920;
    public int WORLD_HEIGHT = 1080;

    //VIEWPORT
    private final Viewport viewport;

    ///gameobjects
    private Player player;
    private String  TAG = GameScreen.class.getName();
    private TextField chatTextField;
    public TextArea chatTextArea;
    private TextButton send;
    private ScrollPane scrollPane;
    private boolean isShowingChat;
    private TextButton hideshow;


    public GameScreen(String levelName, Main main, Stage stage){
        this.stage=stage;
        WORLD_WIDTH = Gdx.graphics.getWidth();
        WORLD_HEIGHT = Gdx.graphics.getHeight();
        initialTime = TimeUtils.nanoTime();
        camera = new OrthographicCamera();
//        //camera.setToOrtho(false,CAMERA_WIDTH,CAMERA_HEIGHT);
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);

        Gdx.app.log(TAG,"start W:"+WORLD_WIDTH+"H:"+WORLD_HEIGHT);
        //Setup texture atlas
        //TODO change depending on level
        textureAtlas = new TextureAtlas("images.atlas");

        backgroundMaScrollingSpeed= WORLD_WIDTH/4;
        SetupBackground();
//        batch = new SpriteBatcatch();
        batch = (SpriteBatch) stage.getBatch();
        int totalPxls = WORLD_HEIGHT+WORLD_HEIGHT;
        //Setup game objects
        player = new Player(10,-10,
                30,30,textureAtlas);
        //Setup font
        font = getBitmapFont();
        //Setup joystick
        joy = new JoyStick(textureAtlas.findRegion("joystick_frame"),textureAtlas.findRegion("joystick_ball"),
                0,0,totalPxls*(0.2f),totalPxls*(0.2f),(viewport));

        level = LevelLoader.levelLoadByName(levelName,player,camera,textureAtlas,this,main);

    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public void setTextureAtlas(TextureAtlas textureAtlas) {
        this.textureAtlas = textureAtlas;
    }

    public void addGameObject(GameObject d){
        otherGameObjects.add(d);
    }

    public Drawable removeGameObject(GameObject d){
        return otherGameObjects.remove(otherGameObjects.indexOf(d));
    }
    private void SetupBackground() {
        //Setup background
        backgrounds = new TextureRegion[9];
        backgrounds[0] = textureAtlas.findRegion("bg (1)");
        backgrounds[1] = textureAtlas.findRegion("bg (2)");
        backgrounds[2] = textureAtlas.findRegion("bg (3)");
        backgrounds[3] = textureAtlas.findRegion("bg (4)");
        backgrounds[4] = textureAtlas.findRegion("bg (5)");
        backgrounds[5] = textureAtlas.findRegion("bg (6)");
        backgrounds[6] = textureAtlas.findRegion("bg (7)");
        backgrounds[7] = textureAtlas.findRegion("bg (8)");
        backgrounds[8] = textureAtlas.findRegion("bg (9)");
    }

    private BitmapFont getBitmapFont() {
        final BitmapFont font;
        //Setup font to write on screen
        font = new BitmapFont();
        font.getData().setScale(3);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return font;
    }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(this);

        int totalPxls = WORLD_HEIGHT+WORLD_HEIGHT;
        stage.clear();

        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        hideshow = new TextButton("Toogle Chat", skin);
        hideshow.setPosition(WORLD_WIDTH-WORLD_WIDTH*0.234f-WORLD_WIDTH*0.313f,0);
        hideshow.setSize(WORLD_WIDTH*0.234f+WORLD_WIDTH*0.313f,WORLD_HEIGHT*0.104f);
        hideshow.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                isShowingChat = !isShowingChat;
                hideShowChat();

            }
        });
        stage.addActor(hideshow);

        chatTextField = new TextField("",skin);
        chatTextField.setPosition(WORLD_WIDTH-WORLD_WIDTH*0.547f,hideshow.getHeight());
        chatTextField.setSize(WORLD_WIDTH*0.313f,WORLD_HEIGHT*0.104f);


        send = new TextButton("Send", skin);
        send.setPosition(WORLD_WIDTH-WORLD_WIDTH*0.234f,hideshow.getHeight());
        send.setSize(WORLD_WIDTH*0.234f,WORLD_HEIGHT*0.104f);
        send.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String message=chatTextField.getText();
                level.InvokeAction(NetworkedMatricLevelActions.chat.commandStr,message);
                chatTextField.setText("");
            }
        });


        chatTextArea = new TextArea("",skin);
        chatTextArea.setDisabled(true);
//        stage.addActor(chatTextArea);
        scrollPane = new ScrollPane(chatTextArea);
        scrollPane.setPosition(WORLD_WIDTH-chatTextField.getWidth()-send.getWidth(),chatTextField.getHeight()+hideshow.getHeight());
        scrollPane.setSize(chatTextField.getWidth()+send.getWidth(),WORLD_HEIGHT*0.208f);
        resizeScrollview();
        scrollPane.setOverscroll(false, true);
        addChatActors();

        level.show();
    }

    public void resizeScrollview() {
        int numberOfScrollLines = chatTextArea.getText().length()%60;
        chatTextArea.setPrefRows(numberOfScrollLines);
        scrollPane.layout();
    }

    private void addChatActors() {
        stage.addActor(send);
        stage.addActor(scrollPane);
        stage.addActor(chatTextField);
    }
    private void removeChatActors() {
        send.remove();
        scrollPane.remove();
        chatTextField.remove();
    }

    @Override
    public void render(float delta) {
        //camera.update();
        viewport.apply();

        //batch.setProjectionMatrix(camera.combined);
        //batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        Gdx.gl.glClearColor( 0.973f, 0.573f, 0.373f, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        renderBackground(delta);
        level.update(delta);
        level.draw(batch);
        readInputsAndDetectCollion(delta);

        //Draw all other objects
        for(Drawable d: otherGameObjects){
            d.draw(batch);
        }

        //Draw player
        player.draw(batch,viewport);

        ///DrawUI
        joy.draw(batch);
        font.draw(batch,"X:"+player.getX()+" Y:"+player.getY(),0,WORLD_HEIGHT);
        if(otherGameObjects.size()>0)
            font.draw(batch,"X:"+otherGameObjects.get(0).getX()+" Y:"+otherGameObjects.get(0).getY(),300,WORLD_HEIGHT);

        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }


    private void readInputsAndDetectCollion(float delta) {
//        if(chatTextField.isTouchFocusTarget())
//            Gdx.app.log(TAG,"Text Focus");
        Vector2 movement = Vector2.Zero;
        if(stage.getKeyboardFocus() != chatTextField)
            movement = player.readInputProcessCollision(level,joy,delta);

        float moveAmountX = movement.x;
        float moveAmountY = movement.y;
//        camera.position.x += moveAmountX;
//        camera.position.y += moveAmountY;
//      move background
        backgroundOffsets[0] += (float)moveAmountX * Math.ceil(backgroundMaScrollingSpeed * delta/ 256f);
        backgroundOffsets[1] += (float)moveAmountX * Math.ceil(backgroundMaScrollingSpeed * delta/ 128f);
        backgroundOffsets[2] += (float)moveAmountX * Math.ceil(backgroundMaScrollingSpeed * delta/ 64f);
        backgroundOffsets[3] += (float)moveAmountX * Math.ceil(backgroundMaScrollingSpeed * delta/ 32f);
        backgroundOffsets[4] += (float)moveAmountX * Math.ceil(backgroundMaScrollingSpeed * delta/ 16f);
        backgroundOffsets[5] += (float)moveAmountX * Math.ceil(backgroundMaScrollingSpeed * delta/ 8f);
        backgroundOffsets[6] += (float)moveAmountX * Math.ceil(backgroundMaScrollingSpeed * delta/ 4f);
        backgroundOffsets[7] += (float)moveAmountX * Math.ceil(backgroundMaScrollingSpeed * delta/ 2f);
        backgroundOffsets[8] += (float)moveAmountX * backgroundMaScrollingSpeed * delta ;

    }



    private void renderBackground(float delta) {

        for(int layer = 0;layer < backgrounds.length;layer++){
            if(backgroundOffsets[layer]  > WORLD_WIDTH ){
                backgroundOffsets[layer] = 0;
            } else
            if(backgroundOffsets[layer]  < 0 ){
                backgroundOffsets[layer] = WORLD_WIDTH;
            }
            int ypos = 0;
            if(layer > 5 ||layer == 0)
                ypos = WORLD_HEIGHT/2;
            batch.draw(backgrounds[layer], -backgroundOffsets[layer],ypos,
                    WORLD_WIDTH,WORLD_HEIGHT);
            batch.draw(backgrounds[layer], -backgroundOffsets[layer]+WORLD_WIDTH,ypos,
                    WORLD_WIDTH,WORLD_HEIGHT);
            batch.draw(backgrounds[layer], -backgroundOffsets[layer]-WORLD_WIDTH,ypos,
                    WORLD_WIDTH,WORLD_HEIGHT);
        }
    }

    @Override
    public void resize(int width, int height) {
        //camera.setToOrtho(false,width,height);
        //Gdx.app.log(TAG,"W:"+WORLD_WIDTH+"H:"+WORLD_HEIGHT);
        WORLD_WIDTH = Gdx.graphics.getWidth();
        WORLD_HEIGHT = Gdx.graphics.getHeight();
        viewport.update(WORLD_WIDTH,WORLD_HEIGHT,true);
        //batch.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        stage.clear();
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }
    @Override
    public boolean touchDown(int screenX,int screenY,int point, int button){
        stage.unfocus(chatTextField);
        stage.touchDown(screenX,screenY,point,button);
        return joy.touchDown(screenX,screenY,point,button);
    }
    @Override
    public boolean touchUp(int screenX,int screenY,int point, int button){
        stage.touchUp(screenX,screenY,point,button);
        return joy.touchUp(screenX,screenY,point,button);
    }
    @Override
    public boolean keyUp(int keycode) {
        stage.keyUp(keycode);
        if(keycode == Input.Keys.ESCAPE){
            escIsPressed = true;
        }
        return super.keyUp(keycode);
    }
    public boolean keyDown (int keycode) {
        if (keycode == Input.Keys.T&&stage.getKeyboardFocus() != chatTextField){
            isShowingChat = !isShowingChat;
            hideShowChat();
        }
        return stage.keyDown(keycode);
    }

    private void hideShowChat() {
        if(isShowingChat){
            removeChatActors();
        } else{
            addChatActors();
        }
    }

    public boolean keyTyped (char character) {
        return stage.keyTyped(character);
    }


    public boolean touchDragged (int screenX, int screenY, int pointer) {
        return stage.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved (int screenX, int screenY) {
        return stage.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled (float amountX, float amountY) {
        return stage.scrolled(amountX, amountY);
    }
}
