package Core;

import org.joml.Vector2f;

public abstract class Scene {

    protected Camera camera;

    public Scene(){
        this.camera = new Camera(new Vector2f());
    }

    public void init() {}

    public abstract void update(float dt);
}
