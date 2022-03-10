package games.apolion.multiplatformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import games.apolion.multiplatformer.screens.GameScreen;
import games.apolion.multiplatformer.screens.LoginScreen;
import games.apolion.multiplatformer.screens.PauseScreen;

public class Main extends Game {
	public GameScreen gameScreen;
	public PauseScreen pauseScreen;
	public LoginScreen login;

	@Override
	public void create () {
		Stage stage = new Stage(new ScreenViewport());
		login = new LoginScreen(stage);
		setScreen(gameScreen);
		gameScreen = new GameScreen("Test",this,stage);
		pauseScreen = new PauseScreen(stage);
		setScreen(gameScreen);
//		setScreen(gameScreen);
	}

	@Override
	public void render () {
		super.render();
		if(gameScreen.escIsPressed){
			pauseScreen.escIsPressed = false;
			gameScreen.escIsPressed = false;
			setScreen(pauseScreen);
		} else if(pauseScreen.escIsPressed){
			pauseScreen.escIsPressed = false;
			gameScreen.escIsPressed = false;
			setScreen(gameScreen);
		}
		if(login.loginSucessful){
			login.loginSucessful =false;
			setScreen(gameScreen);
		}
	}

}
