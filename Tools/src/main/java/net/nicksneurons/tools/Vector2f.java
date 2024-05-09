package net.nicksneurons.tools;

/**
 * This class is an immutable type.
 */
public final class Vector2f {

    /** TODO Document */
    public static final Vector2f right = new Vector2f(1.0f, 0.0f);

    /** TODO Document */
    public static final Vector2f left = new Vector2f(-1.0f, 0.0f);

    /** TODO Document */
    public static final Vector2f up = new Vector2f(0.0f, 1.0f);

    /** TODO Document */
    public static final Vector2f down = new Vector2f(0.0f, -1.0f);

    private final float x, y;

    /**
     * TODO Document
     * @param x
     * @param y
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * TODO Document
     * @param vec
     * @return
     */
    public Vector2f add(Vector2f vec) {
        return new Vector2f(x + vec.getX(), y + vec.getY());
    }

    /**
     * TODO Document
     * @param vec
     * @return
     */
    public Vector2f subtract(Vector2f vec) {
        return new Vector2f(x - vec.getX(), y - vec.getY());
    }

    /**
     * TODO Document
     * Avoid calling this function unless necessary.
     * Square length is will be more often the correct choice since it
     * is optimized for speed.
     * @return
     */
    public float length() {
        return (float)Math.sqrt((double)(x * x + y * y));
    }

    /***
     * TODO Document
     * This function exists for optimization purposes. We may need the length for an operation
     * but want to minimize the number of square-roots. This can be used granted proper care is
     * taken to ensure that the computation accounts for the fact that this length is squared.
     * @return
     */
    public float squareLength() {
        return (x * x) + (y * y);
    }

    /**
     * TODO Document
     * @return
     */
    public Vector2f normalize() {
        float len = length();
        return new Vector2f(x / len, y / len);
    }

    /**
     * TODO Document
     * @param scale
     * @return
     */
    public Vector2f scale(float scale) {
        return new Vector2f(x * scale, y * scale);
    }

    /**
     * TODO Document
     * @param vec
     * @return
     */
    public float dot(Vector2f vec) {
        return (x * vec.x + y * vec.y);
    }

    /**
     * TODO Document
     * This function calculates the angle between this vector and the
     * the one provided.
     * It uses the dot product to find it.
     * @param vec
     * @return
     */
    public float angle(Vector2f vec) {
        //dot = |a||b| * cos(theta)
        return (float) Math.acos((this.dot(vec))/(this.length() * vec.length()));
    }

    /**
     * TODO Please implement.
     * https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/reflect.xhtml
     * Note the incident vector will be this vector. Result is reflection vector.
     * We need to decide if the incident vector should be reversed or not.
     * We can probably just use GLM's algorithm.
     * @param normal
     * @return
     */
    public Vector2f reflect(Vector2f normal) {
        throw new UnsupportedOperationException();
    }

    /**
     * TODO Document
     * @param vec
     * @return
     */
    public float cross(Vector2f vec) {
        return this.x * vec.y + this.y * vec.x;
    }

    /**
     * TODO Document
     * @return
     */
    public float getX() { return x; }

    /**
     * TODO Document
     * @return
     */
    public float getY() { return y; }
}