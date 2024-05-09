package net.nicksneurons.engine.components;

import net.nicksneurons.engine.framework.Component;
import net.nicksneurons.tools.Vector2f;

import java.util.ArrayList;

/**
 * TODO Document
 */
public class Transform2D extends Component {

    private Vector2f position;
    private float zOrder;
    private float angle;
    private Vector2f scale;

    private Transform2D parent;
    private ArrayList<Transform2D> children = new ArrayList<Transform2D>();

    /**
     * TODO Document
     */
    public Transform2D() {
        this(new Vector2f(0, 0), 0);
    }

    /**
     * TODO Document
     * @param position
     */
    public Transform2D(Vector2f position) {
        this(position, 0);
    }

    /**
     * TODO Document
     * @param position
     * @param zOrder
     */
    public Transform2D(Vector2f position, float zOrder) {
        this.position = position;
        this.zOrder = zOrder;
    }

    /**
     * TODO Document
     * @param position
     */
    public void setPosition(Vector2f position) { this.position = position; }

    /**
     * TODO Document
     * @return
     */
    public Vector2f getPosition() { return position; }

    /**
     * TODO Document
     * @param zOrder
     */
    public void setZOrder(float zOrder) { this.zOrder = zOrder; }

    /**
     * TODO Document
     * @return
     */
    public float getZOrder() { return zOrder; }

    /**
     * TODO Document
     * @param angle
     */
    public void setAngle(float angle) { this.angle = angle; }

    /**
     * TODO Document
     * @return
     */
    public float getAngle() { return angle; }

    /**
     * TODO Document
     * @param scale
     */
    public void setScale(Vector2f scale) { this.scale = scale; }

    /**
     * TODO Document
     * @return
     */
    public Vector2f getScale() { return scale; }

    public void addTransform(Transform2D transform) {
        if (children.contains(transform)) throw new IllegalArgumentException("Component already added.");
        children.add(transform);
    }

    public void removeTransform(Transform2D transform) {
        if (!children.contains(transform)) throw new IllegalArgumentException("Transform not added.");
        children.remove(transform);
    }

    public Transform2D removeTransform(int index) {
        if (index < 0 || index >= children.size()) throw new IndexOutOfBoundsException("Invalid index...");
        return children.remove(index);
    }

    /**
     * TODO Document
     * @param x
     * @param y
     */
    public void translate(float x, float y) {
        position.add(new Vector2f(x, y));
    }

    /**
     * TODO Document
     * @param vec
     */
    public void translate(Vector2f vec) {
        position.add(vec);
    }

    /**
     * TODO Document
     * @param angle
     */
    public void rotate(float angle) {
        this.angle += angle;
    }

    /**
     * TODO Document
     * @param scaleX
     * @param scaleY
     */
    public void scale(float scaleX, float scaleY) {
        this.scale = new Vector2f(scale.getX() * scaleX, scale.getY() * scaleY);
    }
}