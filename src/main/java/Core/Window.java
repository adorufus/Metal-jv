package Core;

import Core.Events.KeyListener;
import Core.Events.MouseListener;
import Core.ImGUI.ImGuiLayer;
import Core.Scenes.LevelEditorScene;
import Core.Scenes.LevelScene;
import Utils.Debug;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;

import static Utils.Utilities.Print;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;
    private ImGuiLayer imguiLayer;

    public float r, g, b ,a;
    private boolean fadeToBlack;

    private static Window window = null;

    private static Scene currentScene;

    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Metal-Jv";
        this.r = .1f;
        this.g = .1f;
        this.b = .1f;
        this.a = 1f;
    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.load();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.load();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false : "Unknown scene '" + newScene + "'";
        }
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }

        return Window.window;
    }

    public static Scene getScene() {
        return get().currentScene;
    }

    public void run() {
        Print("Engine Running: " + "");

        init();
        update();

        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        Print("Initializing...");
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
           window.setHeight(newHeight);
           window.setWidth(newWidth);
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        this.imguiLayer = new ImGuiLayer(glfwWindow);
        this.imguiLayer.initImGui();

//        GLUtil.setupDebugMessageCallback();

        Window.changeScene(0);

    }

    public void update() {

        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)){
            //polling events
            glfwPollEvents();

            Debug.beginFrame();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt > 0) {
                Debug.draw();
                currentScene.update(dt);
            }

            this.imguiLayer.update(dt, currentScene);
            glfwSwapBuffers(glfwWindow);

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

        currentScene.saveExit();
    }

    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static void setWidth(int newWidth){
        get().width = newWidth;
    }
    public static void setHeight(int newHeight){
        get().height = newHeight;
    }
}
