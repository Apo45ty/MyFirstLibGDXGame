package games.apolion.multiplatformer.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Json;
import games.apolion.multiplatformer.Main;
import games.apolion.multiplatformer.entities.NetworkManager;
import games.apolion.multiplatformer.entities.Player;
import games.apolion.multiplatformer.http.DTO.UsersDTO;
import games.apolion.multiplatformer.screens.GameScreen;
import games.apolion.multiplatformer.udpnet.entitties.ServerCommands;
import games.apolion.multiplatformer.utils.Constants;
import games.apolion.multiplatformer.utils.HttpRequestSender;

public class NetworkedMatrixLevel extends MatrixLevel {
    private final GameScreen gameScreen;
    private final NetworkManager networkMan;


    public NetworkedMatrixLevel(Player player, Camera camera, int x, int y, LevelLayout levelLayout, TextureAtlas atlas, int tileWidth, int tileHeigh, GameScreen gameScreen, Main main) {
        super(player, camera, x, y, levelLayout, atlas, tileWidth, tileHeigh);
        this.gameScreen= gameScreen;
        networkMan = new NetworkManager(gameScreen,main);

    }

    @Override
    public void show() {
        super.show();
        networkMan.show();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        networkMan.update(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        networkMan.dispose();
    }
    @Override
    public boolean InvokeAction(String action, String data) {
        if(action.equals(NetworkedMatricLevelActions.chat.commandStr)){
            networkMan.sendUDPPacket(ServerCommands.chat.commandStr+": user:"+HttpRequestSender.me.tokenDTO.username+" "+data
                    +" token:"+ HttpRequestSender.me.tokenDTO.token);

        }
        return false;
    }
}
