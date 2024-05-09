package net.nicksneurons.engine.components;

import net.nicksneurons.engine.framework.Component;
import net.nicksneurons.tools.Vector2f;

/**
 * TODO Document
 */
public class RigidBody2D extends Component {

    private Vector2f velocity;
    private Vector2f accleration;
    private float angularVelocity;
    private float angularAcceleration;
    private float mass;
    private Vector2f centerOfMass;
    private float momentOfInertia;
    private boolean useGravity;

    /**
     * TODO Document
     */
    public RigidBody2D() {

    }

    /**
     * TODO Document
     */
    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

    /**
     * TODO Document
     * @return
     */
    public Vector2f getVelocity() { return velocity; }

    //TODO Added getters/setters + documentation.
}