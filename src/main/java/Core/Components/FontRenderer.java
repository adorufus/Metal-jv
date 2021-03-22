package Core.Components;

import Core.Component;

import static Utils.Utilities.Print;

public class FontRenderer extends Component {

    @Override
    public void start() {
        if(gameObject.getComponent(SpriteRenderer.class) != null){
            Print("Found Font Renderer");
        }
    }

    @Override
    public void update(float dt) {

    }
}
