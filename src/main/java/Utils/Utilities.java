package Utils;

import Core.Component;
import Core.GameObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.io.PrintStream;

public class Utilities {
    public static void Print(Object msg){
        System.out.println(msg);
    }

    public static void PrintErr(Object msg){
        System.err.println(msg);
    }

    public static GLFWErrorCallback PrintGLFW(PrintStream msg){
        return GLFWErrorCallback.createPrint(msg);
    }

    public static Gson createGson () {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new Deserializer<Component>())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
    }
}
