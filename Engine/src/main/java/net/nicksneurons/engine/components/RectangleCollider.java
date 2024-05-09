package net.nicksneurons.engine.components;

import net.nicksneurons.tools.Vector2f;

public class RectangleCollider extends Collider2D {

    private Vector2f size;

    /**
     * TODO Document
     * @param size
     */
    public void setSize(Vector2f size) { this.size = size; }

    /**
     * TODO Document
     * @return
     */
    public Vector2f getSize() { return size; }

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
