package Core.Renderer;

import Utils.Utilities;
import org.lwjgl.BufferUtils;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

    private String filePath;
    private int textureID;
    private int height, width;

//    public Texture (String filePath) {
//
//    }

    public void init(String filePath) {
        this.filePath = filePath;

        //generate texture on GPu
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        //set Texture param
        //repeat image in both direction
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        //when stretching the image, then pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //when shrinking, then pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filePath, width, height, channels, 0);

        if(image != null){
            this.width = width.get(0);
            this.height = height.get(0);

            Utilities.Print("Current Texture Channel: " + channels.get(0));

            if(channels.get(0) == 3){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if(channels.get(0) == 4){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                assert false : "Error: (Texture) Unknown number of channel '" + channels.get(0) + "'";
            }
        } else {
            assert false : "Error: (Texture) Could not load image '" + filePath + "'";
        }

        stbi_image_free(image);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }
}
