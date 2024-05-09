package net.nicksneurons.engine.components;

import net.nicksneurons.tools.Vector2f;

public class CircleCollider extends Collider2D {

    private float radius;

    /**
     * * TODO Document
     * @param radius
     */
    public void setRadius(float radius) { this.radius = radius; }

    /**
     * * TODO Document
     * @return
     */
    public float getRadius() { return radius; }

    /**
     * TODO Document
     * @param direction
     * @param distance
     * @return
     */
    @Override
    public int cast(Vector2f direction, float distance) {
        return 0;
    }
}
