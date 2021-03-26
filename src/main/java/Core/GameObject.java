package Core;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int uid = -1;

    private String name;
    private List<Component> components;
    private int zIndex;
    public Transform transform;

    public GameObject(String name, Transform transform, int zIndex){
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.zIndex = zIndex;

        this.uid = ID_COUNTER++;
    }

    public <T extends Component> T getComponent(Class<T> componentClass){
        for(Component component : components) {
            try {
                if (componentClass.isAssignableFrom(component.getClass())) {
                    return componentClass.cast(component);
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
                assert false : "Error: Casting component.";
            }
        }

        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass){
        for(int i = 0; i < components.size(); i++){
            Component c = components.get(i);
            if(componentClass.isAssignableFrom(c.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c){
        c.generateId();
        this.components.add(c);
        c.gameObject = this;
    }

    public void update(float dt) {
        for(int i = 0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }

    public void start() {
        for(int i = 0; i < components.size(); i++){
            components.get(i).start();
        }
    }

    public void imgui() {
        for(Component c : components) {
            c.imgui();
        }
    }

    public int zIndex(){
        return this.zIndex;
    }

    public String getName() {
        return this.name;
    }

    public static void init(int maxId){
        ID_COUNTER = maxId;
    }

    public int getUid() {
        return uid;
    }

    public List<Component> getAllComponents() {
        return this.components;
    }
}
