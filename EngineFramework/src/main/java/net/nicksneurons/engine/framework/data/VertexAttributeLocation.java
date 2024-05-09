package net.nicksneurons.engine.framework.data;

/**
 * TODO Document
 * This fake enum reserves locations 0 to 3 for basic vertex attributes.
 * These are standard, so all shaders must acquire these attributes with these locations.
 */
public final class VertexAttributeLocation {

    /** TODO Document */
    public static final VertexAttributeLocation POSITION_ATTRIBUTE = new VertexAttributeLocation(0);

    /** TODO Document */
    public static final VertexAttributeLocation COLOR_ATTRIBUTE = new VertexAttributeLocation(1);

    /** TODO Document */
    public static final VertexAttributeLocation NORMAL_ATTRIBUTE = new VertexAttributeLocation(2);

    /** TODO Document */
    public static final VertexAttributeLocation TEXTURE_COORDINATE_ATTRIBUTE = new VertexAttributeLocation(3);

    private final int location;

    /**
     * TODO Document
     * @param location
     */
    public VertexAttributeLocation(int location) {
        if (location >= 0 && location <= 3) throw new IllegalArgumentException("The location provided is reserved.");
        this.location = location;
    }

    /**
     * TODO Document
     * @return
     */
    public int getLocation() { return location; }
}