package games.apolion.multiplatformer;

import android.content.pm.PackageManager;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import games.apolion.multiplatformer.Main;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Main(), config);
		if(getContext().checkCallingOrSelfPermission("android.permission.INTERNET") == PackageManager.PERMISSION_GRANTED) {
			Gdx.app.log("INFO", "PERMISSION GRANTED");
		} else {
			Gdx.app.log("ERROR", "PERMISSION DENIED");
		}
	}
}
