package Core.Components;

import Core.Renderer.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet {
    private Texture texture;
    private List<Sprite> sprites;

    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int numSprite, int spacingX, int spacingY) {
        this.sprites = new ArrayList<>();
        this.texture = texture;

        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;

        for (int i = 0; i < numSprite; i++){
            float topY = (currentY + spriteHeight) / (float) texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float) texture.getWidth();
            float leftX = currentX / (float) texture.getWidth();
            float bottomY = currentY / (float) texture.getHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite(this.texture, texCoords);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacingX;
            if(currentX >= texture.getWidth()){
                currentX = 0;
                currentY -= spriteHeight + spacingY;
            }
        }
    }

    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }
}
