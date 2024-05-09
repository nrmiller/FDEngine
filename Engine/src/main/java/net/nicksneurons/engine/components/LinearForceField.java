package net.nicksneurons.engine.components;

import net.nicksneurons.engine.framework.Component;
import net.nicksneurons.tools.Vector2f;

/**
 * TODO Document
 * If an entity only contains this component, then the force field is globally applied.
 * However, if the entity has a Transform2D and Collider, then the force field will be applied
 * for all physical entities within the bounds of the Collider.
 * The entity may also contain a mesh component so that it can be rendered.
 *
 * TODO longrange forces decay as the potential energy decreases, when doing our physics engine, we should keep this in mind. Reference Coulombs Law.
 */
public class LinearForceField extends Component {
    private float force;
    private Vector2f direction;
    //This dictates of the force is uniform or obeys an inverse-square law.
    //If false, the strongest point will be the edge of the Collider opposite to the direction.
    private boolean isUniformForce;

    //TODO add set/gets

    /**
     * TODO Document
     * @param force
     */
    public void setForce(float force){this.force = force;}

    /**
     * TODO Document
     * @return
     */
    public float getForce(){return force;}
}
