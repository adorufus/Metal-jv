package Core;

import Core.Renderer.Renderer;
import Utils.Deserializer;
import Utils.GameObjectDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import imgui.ImGui;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static Utils.Utilities.Print;
import static Utils.Utilities.createGson;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    private String objectName = null;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;
    protected boolean loadedLevel = false;

    public Scene(){

    }

    public void init() {}

    public void start() {
        for(GameObject go : gameObjects){
            go.start();
            this.renderer.add(go);
        }

        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go){
        if(!isRunning){
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public abstract void update(float dt);

    public Camera camera() {
        return this.camera;
    }

    public void sceneImgui() {

        if(activeGameObject != null) {
            ImGui.begin("Inspecting " + objectName);
            activeGameObject.imgui();
            ImGui.end();
        }

        imgui();
    }

    public void imgui() {

    }

    public void saveExit() {
        Gson gson = createGson();

        try {
            FileWriter writer = new FileWriter(this.getClass().getSimpleName() + ".level", false);
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
        } catch (IOException e) {
            Print(e.getLocalizedMessage());
        }
    }

    public void load() {
        Gson gson = createGson();

        String inFile = "";
        try{
            inFile = new String(Files.readAllBytes(Paths.get(this.getClass().getSimpleName() + ".level")));
        } catch (IOException e){
            e.printStackTrace();
        }

        if(!inFile.equals("")){

            int maxGoId = -1;
            int maxCompId = -1;

            GameObject[] objects = gson.fromJson(inFile, GameObject[].class);
            for(int i = 0; i < objects.length; i++){
                addGameObjectToScene(objects[i]);

                for(Component c : objects[i].getAllComponents()){
                    if(c.getUid() > maxCompId){
                        maxCompId = c.getUid();
                    }
                }

                if(objects[i].getUid() > maxGoId) {
                    maxGoId = objects[i].getUid();
                }
            }

            maxGoId++;
            maxCompId++;
            GameObject.init(maxGoId);
            Component.init(maxCompId);

            this.loadedLevel = true;
        }
    }

    public void setActiveObjectName(String activeObjectName) {
        if(activeObjectName == null || activeObjectName == ""){
            this.objectName = "gameObject";
        } else {
            this.objectName = activeObjectName;
        }
    }
}
