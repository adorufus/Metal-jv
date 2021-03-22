package Core.Components;

import Core.Component;

import static Utils.Utilities.Print;

public class SpriteRenderer extends Component {

    private boolean firtTime = false;

    @Override
    public void start() {
        Print("I am starting");
    }

    @Override
    public void update(float dt) {
        if (!firtTime){
            Print("I am updating");
            firtTime = true;
        }
    }
}
