package games.apolion.multiplatformer.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import games.apolion.multiplatformer.Main;
import games.apolion.multiplatformer.entities.GameObject;
import games.apolion.multiplatformer.entities.Player;
import games.apolion.multiplatformer.level.AbstractLevel;
import games.apolion.multiplatformer.level.MatrixLevel;
import games.apolion.multiplatformer.level.NetworkedMatrixLevel;
import games.apolion.multiplatformer.screens.GameScreen;

public class LevelLoader {

    public static AbstractLevel levelLoadByName(String levelName, Player player, Camera camera, TextureAtlas textureAtlas, GameScreen gameScreen, Main main){
        AbstractLevel level = null;
        if(levelName.equals("Test")){
            MatrixLevel tmpLevel = new NetworkedMatrixLevel(player,camera,0,0, MatrixLevel.LevelLayout.Test,textureAtlas,50,50,gameScreen,main);
            tmpLevel.setTileMatrix(new short[][]{
                    {1,1,1,1,1},
                    {1,1,1,1,1},
                    {1,1,0,1,1},
                    {1,1,1,1,1},
                    {1,1,1,1,1}
            });
            level = tmpLevel;
        }
        return  level;
    }
}
