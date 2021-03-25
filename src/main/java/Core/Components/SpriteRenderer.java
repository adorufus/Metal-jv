package Core.Components;

import Core.Component;
import Core.Renderer.Texture;
import Core.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static Utils.Utilities.Print;

public class SpriteRenderer extends Component {

    private Vector4f color;
    private Sprite sprite;
    
    private Transform lastTransform;
    private boolean isDirty = false;

    public SpriteRenderer(Vector4f color){
        this.color = color;
        this.sprite = new Sprite(null);
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = new Vector4f(1, 1, 1, 1);
    }

    @Override
    public void start() {
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        if(!this.lastTransform.equals(this.gameObject.transform));
    }

    public Vector4f getColor() {
        return color;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
        this.isDirty = true;
    }

    public void setColor (Vector4f color){
        if(!this.color.equals(color)){
            this.isDirty = true;
            this.color.set(color);
        }
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public void setClean() {
        this.isDirty = false;
    }
}
