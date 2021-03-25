package Core.Scenes;

import Core.Camera;
import Core.Components.Sprite;
import Core.Components.SpriteRenderer;
import Core.Components.Spritesheet;
import Core.GameObject;
import Core.Scene;
import Core.Transform;
import Utils.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static Utils.Utilities.Print;

public class LevelEditorScene extends Scene {

    private GameObject mario;
    private Spritesheet sprites;

    public LevelEditorScene(){

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));
        sprites = AssetPool.getSpritesheet("assets/images/sprites/spritesheet.png");

        mario = new GameObject("Player", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        mario.addComponent(new SpriteRenderer(sprites.getSprite(5)));
        this.addGameObjectToScene(mario);

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


    }

    private void loadResources() {
        AssetPool.getShader("assets/Shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/sprites/spritesheet.png", new Spritesheet(AssetPool.getTexture("assets/images/sprites/spritesheet.png"), 16, 16, 26, 0, 0));
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;

    @Override
    public void update(float dt) {

        Print("FPS: " + (1.0f / dt));

        spriteFlipTimeLeft -= dt;
        if (spriteFlipTimeLeft <= 0) {
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if (spriteIndex > 4) {
                spriteIndex = 0;
            }
            mario.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
        }

        for(GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}
