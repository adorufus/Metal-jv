package Core.Scenes;

import Core.*;
import Core.Components.*;
import Core.Events.MouseControls;
import Core.Events.MouseListener;
import Utils.AssetPool;
import Utils.Debug;
import Utils.Deserializer;
import Utils.GameObjectDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static Utils.Utilities.Print;

public class LevelEditorScene extends Scene {

    private GameObject mario;
    private GameObject mario2;
    private Spritesheet envSprite;
    private float currentFps = 0;
    SpriteRenderer marioSprite;

    GameObject levelEditorObject = new GameObject("LevelEditor", new Transform(new Vector2f()), 0);

    public LevelEditorScene(){

    }

    @Override
    public void init() {
        levelEditorObject.addComponent(new MouseControls());
        levelEditorObject.addComponent(new GridLines());

        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));

        envSprite = AssetPool.getSpritesheet("assets/images/sprites/level.png");
        if(loadedLevel){
            if(!gameObjects.isEmpty()) {
                this.activeGameObject = gameObjects.get(0);
                this.activeGameObject.addComponent(new Rigidbody());
                this.setActiveObjectName(gameObjects.get(0).getName());
            }
            return;
        }

//        mario = new GameObject("Player", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 2);
//        marioSprite = new SpriteRenderer();
//        marioSprite.setColor(new Vector4f(1, 0, 0, 1));
//        mario.addComponent(marioSprite);
//        mario.addComponent(new Rigidbody());
//        this.addGameObjectToScene(mario);
//        this.activeGameObject = mario;
//        this.setActiveObjectName(mario.getName());
    }

    private void loadResources() {
        AssetPool.getShader("assets/Shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/sprites/level.png", new Spritesheet(AssetPool.getTexture("assets/images/sprites/level.png"), 16, 16, 81, 0, 0));

        AssetPool.getTexture("assets/texture.jpg");

        for(GameObject g: gameObjects) {
            if(g.getComponent(SpriteRenderer.class) != null){
                SpriteRenderer sprite = g.getComponent(SpriteRenderer.class);
                if(sprite.getTexture() != null) {
                    sprite.setTexture(AssetPool.getTexture(sprite.getTexture().getFilePath()));
                }
            }
        }
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;

    @Override
    public void update(float dt) {
        levelEditorObject.update(dt);

//        Print("FPS: " + (1.0f / dt));
        currentFps = 1.0f / dt;

        MouseListener.getOrthoX();

        for(GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }

    public void imgui() {

        int flags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.HorizontalScrollbar;

        ImGui.begin("Sprite Editor", flags | ImGuiWindowFlags.NoCollapse);

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for(int i = 0; i < envSprite.size(); i++){
            Sprite sprite = envSprite.getSprite(i);
            float spriteWidth = sprite.getWidth() * 4;
            float spriteHeight = sprite.getHeight() * 4;
            int id = sprite.getTexId();

            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);

            if(ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)){
                Print("Button " + i + "clicked");
                GameObject object = Prefabs.generateSpriteObject(sprite, 32, 32);

                //attach to the mouse cursor
                levelEditorObject.getComponent(MouseControls.class).pickupObject(object);

            }

            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if(i + 1 < envSprite.size() && nextButtonX2 < windowX2){
                ImGui.sameLine();
            }
        }

        spriteEditorMenuBar();

        ImGui.end();

//        ImGui.begin("Level Editor", flags);
//
//        if(ImGui.beginMenuBar()){
//            if(ImGui.beginMenu("File")){
//                if(ImGui.menuItem("Save", "ctrl+s", false, true)){
//                    this.saveExit();
//                    Print("Level Saved Successfuly");
//                }
//
//                if(ImGui.menuItem("Load", "ctrl+o", false, true)){
//                    this.load();
//                    Print("Level Loaded");
//                }
//                ImGui.endMenu();
//            }
//            if(ImGui.beginMenu("Edit")) {
//                ImGui.endMenu();
//            }
//            if(ImGui.beginMenu("Components")) {
//                ImGui.endMenu();
//            }
//
//            if(ImGui.menuItem("FPS: " + (int) currentFps, "", false, false)){
//
//            }
//            ImGui.endMenuBar();
//        }
////        ImGui.text("Random text");
//        ImGui.end();
    }

    private void spriteEditorMenuBar() {
        if(ImGui.beginMenuBar()){
            if(ImGui.menuItem("Open", "ctrl+s", false, true)){
                this.saveExit();
                Print("Level Saved Successfuly");
            }
            ImGui.endMenuBar();
        }
    }
}
