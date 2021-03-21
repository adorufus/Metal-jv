package Utils;

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
}
