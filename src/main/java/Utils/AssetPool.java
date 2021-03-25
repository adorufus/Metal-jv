package Utils;

import Core.Components.Spritesheet;
import Core.Renderer.Shader;
import Core.Renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();

    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);
        if(AssetPool.shaders.containsKey(file.getAbsolutePath())){
            return AssetPool.shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(resourceName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);
        if(AssetPool.textures.containsKey(file.getAbsolutePath())){
            return AssetPool.textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture();
            texture.init(resourceName);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpriteSheet(String resourceName, Spritesheet spritesheet){
        File file = new File(resourceName);

        if(!AssetPool.spritesheets.containsKey(file.getAbsolutePath())){
            AssetPool.spritesheets.put(file.getAbsolutePath(), spritesheet);
        }
    }

    public static Spritesheet getSpritesheet (String resourceName){
        File file = new File(resourceName);
        if(!AssetPool.spritesheets.containsKey(file.getAbsolutePath())){
            assert false : "Error: tried to access spritesheet '" + resourceName + "'" + " and will not be added to asset pool!";
        }

        return AssetPool.spritesheets.getOrDefault(file.getAbsolutePath(), null);
    }
}
