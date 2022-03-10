package games.apolion.multiplatformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import games.apolion.multiplatformer.http.DTO.UsersDTO;
import games.apolion.multiplatformer.utils.HttpRequestSender;

public class LoginScreen implements Screen{

    private String  TAG = LoginScreen.class.getName();
    public boolean loginSucessful = false;
    private Stage stage;
    public TextField email,password;
    private boolean loginClicked=false;

    public LoginScreen(Stage stage){

        /// create stage and set it as input processor
        this.stage = stage;
    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(this.stage);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);
        stage.addActor(table);

        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

        //create buttons
        Label labelEmail = new Label("Email",skin);
        email = new TextField("",skin);
        Label labelPassword = new Label("Password",skin);
        password =  new TextField("",skin);
        password.setPasswordMode(true);
        password.setPasswordCharacter('*');
        TextButton login = new TextButton("Login", skin);
        TextButton exit = new TextButton("Exit", skin);

        //add buttons to table
        table.add(labelEmail).fillX().uniformX();
        table.row();
        table.add(email).fillX().uniformX();
        table.row();
        table.add(labelPassword).fillX().uniformX();
        table.row();
        table.add(password).fillX().uniformX();
        table.row();
        table.add(login).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();
        // create button listeners
        login.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                Gdx.app.log(TAG,"p:"+password.getText()+" e:"+email.getText());
                try{
                    HttpRequestSender.me.sendLoginRequest(new UsersDTO("",password.getText(),email.getText()));
                    loginClicked = true;
                } catch(NoClassDefFoundError e){
                    e.printStackTrace();
                }
            }
        });
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

    }

    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        if(loginClicked&&HttpRequestSender.me.tokenDTO !=null){
            loginSucessful=true;
        }
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // change the stage's viewport when teh screen size is changed
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        stage.clear();
    }

    @Override
    public void dispose() {
        // dispose of assets when not needed anymore
        stage.dispose();
    }

}