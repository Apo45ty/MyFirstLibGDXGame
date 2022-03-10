package games.apolion.multiplatformer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import games.apolion.multiplatformer.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//		config.width = 1920;
//		config.height = 1080;
		config.resizable = false;
		new LwjglApplication(new Main(), config);
	}
}
