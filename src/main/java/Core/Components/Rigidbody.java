package Core.Components;

import Core.Component;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Rigidbody extends Component {
    private int colliderType = 0;
    private int myType = 0;
    private float friction = 0.8f;
    private boolean useGravity = false;
    public Vector3f velocity = new Vector3f().zero();
    public transient Vector4f tmp = new Vector4f().zero();
}
