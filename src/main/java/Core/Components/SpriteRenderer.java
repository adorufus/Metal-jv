package Core.Components;

import Core.Component;
import Core.Renderer.Texture;
import Core.Transform;
import imgui.ImGui;
import imgui.enums.ImGuiColorEditFlags;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static Utils.Utilities.Print;

public class SpriteRenderer extends Component {

    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private Sprite sprite = new Sprite();
    
    private transient Transform lastTransform;
    private transient boolean isDirty = false;

//    public SpriteRenderer(Vector4f color){
//        this.color = color;
//        this.sprite = new Sprite(null);
//        this.isDirty = true;
//    }
//
//    public SpriteRenderer(Sprite sprite) {
//        this.sprite = sprite;
//        this.color = new Vector4f(1, 1, 1, 1);
//        this.isDirty = true;
//    }

    @Override
    public void start() {
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        if(!this.lastTransform.equals(this.gameObject.transform)){
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    @Override
    public void imgui() {
        float[] refColorV = {1.0f, 1.0f, 1.0f, 1.0f};
        if(ImGui.colorPicker4("Color Picker", refColorV)){
            this.color.set(refColorV[0], refColorV[1], refColorV[2], refColorV[3]);
            this.isDirty = true;
        }
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
