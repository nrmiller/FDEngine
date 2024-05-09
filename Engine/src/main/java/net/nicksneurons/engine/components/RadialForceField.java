package net.nicksneurons.engine.components;

import net.nicksneurons.engine.framework.Component;
import net.nicksneurons.engine.framework.data.Polarity;

/**
 * TODO Document
 * This entity depends on a Transform component to know where to attract or repel from.
 * If the entity also has a Collider, then the force field will be applied
 * for only physical entities within the bounds of the Collider.
 * The entity may also contain a mesh component so that it can be rendered.
 */
public class RadialForceField extends Component {
    private float force;
    private Polarity polarity;
    //This dictates of the force is uniform or obeys an inverse-square law.
    //If false, the strongest point will be the location of the transform.
    private boolean isUniformForce;

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

    /**
     * TODO Document
     * @param polarity
     */
    public void setPolarity(Polarity polarity){this.polarity = polarity;}

    /**
     * TODO Document
     * @return
     */
    public Polarity getPolarity(){return polarity;}

    /**
     * TODO Document
     * @param isUniformForce
     */
    public void setUniformForce(boolean isUniformForce){this.isUniformForce = isUniformForce;}

    /**
     * TODO Document
     */
    public boolean isUniformForce(){return isUniformForce;}

    //TODO getters/setters w/ documentation.
}