package Core.Scenes;

import Core.Camera;
import Core.Components.SpriteRenderer;
import Core.GameObject;
import Core.Scene;
import Core.Transform;
import Utils.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static Utils.Utilities.Print;

public class LevelEditorScene extends Scene {

    public LevelEditorScene(){

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(-250, 0));

        GameObject obj1 = new GameObject("Player", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/texture.jpg")));
        this.addGameObjectToScene(obj1);

//        int xOffset = 10;
//        int yOffset = 10;
//
//        float totalWidth = (float)(600 - xOffset * 2);
//        float totalHeight = (float)(300 - yOffset * 2);
//        float sizeX = totalWidth / 100.0f;
//        float sizeY = totalHeight / 100.0f;
//        float padding = 3;
//
//        for(int x = 0; x < 100; x++){
//            for(int y = 0; y < 100; y++){
//                float xPos = xOffset + (x * sizeX);
//                float yPos = yOffset + (y * sizeY);
//
//                GameObject go = new GameObject("Object" + x + " " + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
//                go.addComponent(new SpriteRenderer(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
//                this.addGameObjectToScene(go);
//            }
//        }

        loadResources();
    }

    private void loadResources() {
        AssetPool.getShader("assets/Shaders/default.glsl");
    }

    @Override
    public void update(float dt) {

        Print("FPS: " + (1.0f / dt));

        for(GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}
