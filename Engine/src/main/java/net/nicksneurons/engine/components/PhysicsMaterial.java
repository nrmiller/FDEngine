package net.nicksneurons.engine.components;

/**
 * TODO Document
 * These 2 links have some common friction coefficients that we can perhaps make an enum out of.
 * https://www.engineeringtoolbox.com/friction-coefficients-d_778.html
 * http://www.engineershandbook.com/Tables/frictioncoefficients.htm
 *
 * Also see:
 * https://docs.unity3d.com/560/Documentation/Manual/class-PhysicMaterial.html
 *
 * TODO Bill wants to simulate material on material friction (e.g. ice on ice, or ice on wood).
 * TODO Bill mentions we might need other attributes to fully define a material (e.g. hardness).
 */
public class PhysicsMaterial {
    private float staticFriction;
    private float dynamicFriction;
    private float bounciness;

    /***
     * TODO Document
     */
    public PhysicsMaterial() {

    }

    /**
     * TODO Document
     * @param bounciness
     */
    public void setBounciness(float bounciness) {
        this.bounciness = bounciness;
    }

    /**
     * TODO Document
     * @return
     */
    public float getBounciness() { return bounciness; }

    /**
     * TODO Document
     * @param friction
     */
    public void setStaticFriction(float friction) { staticFriction = friction; }

    /**
     * TODO Document
     * @return
     */
    public float getStaticFriction() {return staticFriction; }

    /**
     * TODO Document
     * @param friction
     */
    public void setDynamicFriction(float friction) { dynamicFriction = friction; }

    /**
     * TODO Document
     * @return
     */
    public float getDynamicFriction() {return dynamicFriction; }
}

