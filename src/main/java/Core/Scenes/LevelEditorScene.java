package Core.Scenes;

import Core.Camera;
import Core.Renderer.Shader;
import Core.Renderer.Texture;
import Core.Scene;
import Utils.Time;
import Utils.Utilities;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private float[] vertexArray = {
            //position                  //color                     //uv coordinates
             100.5f, -0.5f,  0.0f,      1.0f, 0.0f, 0.0f, 1.0f,     1, 1,//bottom right
             -0.5f,  100.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f,     0, 0,//top left
             100.5f, 100.5f, 0.0f,      0.0f, 0.0f, 1.0f, 1.0f,     1, 0,//top right
             -0.5f,  -0.5f,  0.0f,      1.0f, 1.0f, 0.0f, 1.0f,     0, 1//bottom left
    };

    private int[] elementArray = {
            2, 1, 0, //top right triangle
            0, 1, 3  //bottom right triangle

    };

    private int vaoID, vboID, eboID;
    private Shader defaultShader;
    private Texture texture;

    public LevelEditorScene(){

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(-200, -300));
        defaultShader = new Shader("assets/Shaders/default.glsl");
        defaultShader.compile();
        this.texture = new Texture("assets/texture.jpg");

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //create float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //create vbo upload vertexBuffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //create indicies and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //add vertex attribute pointer
        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        camera.position.x -= dt * 50.0f;
        camera.position.y -= dt * 20.0f;

        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        defaultShader.use();
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

        //bind the VAO
        glBindVertexArray(vaoID);

        //enable the vertex attrib pointer
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //unbind everyting
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();
    }
}
