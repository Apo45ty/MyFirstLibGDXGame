package games.apolion.multiplatformer.entities;

import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class UIGameObject implements Drawable {
    @Override
    public abstract void draw(Batch batch);
}
