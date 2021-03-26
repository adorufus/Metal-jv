package Core;

import imgui.ImGui;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {
    private static int ID_COUNTER = 0;
    private int uid = -1;
    public transient GameObject gameObject = null;

    public void start() {

    }

    public void update(float dt){

    };

    public void imgui() {
        try{
            Field[] fields = this.getClass().getDeclaredFields();
            for(Field field : fields) {
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());

                if(isTransient){
                    continue;
                }


                if(isPrivate){
                    field.setAccessible(true);
                }

                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if(type == int.class){
                    int val = (int) value;
                    int[] imInt = {val};
                    ImGui.text(name + ": ");
                    if(ImGui.dragInt(name, imInt)){
                        field.set(this, imInt[0]);
                    }
                    ImGui.spacing();
                } else if(type == float.class){
                    float val = (float) value;
                    float[] imFloat = {val};
                    ImGui.text(name + ": ");
                    if(ImGui.dragFloat(name, imFloat)){
                        field.set(this, imFloat[0]);
                    }
                    ImGui.spacing();
                } else if(type == boolean.class) {
                    boolean val = (boolean) value;
                    ImGui.text(name + ": ");
                    if(ImGui.checkbox(name, val)){
                        field.set(this, !val);
                    }
                    ImGui.spacing();
                } else if(type == Vector3f.class){
                    Vector3f val = (Vector3f) value;
                    float[] imVector3f = {val.x, val.y, val.z};
                    ImGui.text(name + ": ");
                    if(ImGui.dragFloat3(name, imVector3f)){
                        val.set(imVector3f[0], imVector3f[1], imVector3f[2]);
                    }
                    ImGui.spacing();
                } else if(type == Vector4f.class) {
                    Vector4f val = (Vector4f) value;
                    float[] imVec4 = {val.x, val.y, val.z, val.w};
                    ImGui.text(name + ": ");
                    if(ImGui.dragFloat4(name, imVec4)){
                        val.set(imVec4);
                    }
                    ImGui.spacing();
                }

                if(isPrivate){
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    public void generateId() {
        if(this.uid == -1){
            this.uid = ID_COUNTER++;
        }
    }

    public int getUid() {
        return uid;
    }

    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }
}
