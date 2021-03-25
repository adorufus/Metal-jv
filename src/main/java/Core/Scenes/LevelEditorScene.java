package Core.Scenes;

import Core.Camera;
import Core.Components.Sprite;
import Core.Components.SpriteRenderer;
import Core.Components.Spritesheet;
import Core.GameObject;
import Core.Scene;
import Core.Transform;
import Utils.AssetPool;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static Utils.Utilities.Print;

public class LevelEditorScene extends Scene {

    private GameObject mario;
    private GameObject mario2;
    private Spritesheet sprites;

    public LevelEditorScene(){

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));
        sprites = AssetPool.getSpritesheet("assets/images/sprites/spritesheet.png");

        mario = new GameObject("Player", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 2);
        SpriteRenderer marioSprite = new SpriteRenderer();
        marioSprite.setColor(new Vector4f(1, 0, 0, 1));
        mario.addComponent(marioSprite);
        this.addGameObjectToScene(mario);
        this.activeGameObject = mario;
        this.setActiveObjectName(mario.getName());

        mario2 = new GameObject("Player 2", new Transform(new Vector2f(300, 100), new Vector2f(32, 32)), 3);
        SpriteRenderer mario2SprRenderer = new SpriteRenderer();
        Sprite mario2Sprite = new Sprite();
        mario2Sprite.setTexture(AssetPool.getTexture("assets/texture.jpg"));
        mario2SprRenderer.setSprite(mario2Sprite);
        mario2.addComponent(mario2SprRenderer);
        this.addGameObjectToScene(mario2);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        Print(gson.toJson(1));
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

//        Print("FPS: " + (1.0f / dt));

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

    public void imgui() {
        ImGui.begin("Level Editor");
        ImGui.text("Random text");
        ImGui.end();
    }
}
