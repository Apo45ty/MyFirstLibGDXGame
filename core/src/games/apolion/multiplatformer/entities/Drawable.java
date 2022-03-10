package games.apolion.multiplatformer.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;

public interface Drawable {
   public void draw(Batch batch, Viewport viewport);
   public void draw(Batch batch);
}
