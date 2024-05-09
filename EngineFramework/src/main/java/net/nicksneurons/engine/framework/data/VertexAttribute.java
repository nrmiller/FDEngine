package net.nicksneurons.engine.framework.data;

/**
 * TODO Document
 */
public class VertexAttribute {

    private VertexAttributeLocation location;
    private int components;
    private int componentType;
    private boolean normalized;

    /**
     * TODO Document
     * @param location
     * @param components
     * @param componentType
     */
    public VertexAttribute(VertexAttributeLocation location, int components, int componentType, boolean normalized) {
        this.location = location;
    }

    /**
     * TODO Document
     * @param location
     */
    public void setLocation(VertexAttributeLocation location) { this.location = location; }

    /**
     * TODO Document
     * @return
     */
    public VertexAttributeLocation getLocation() { return location; }

    /**
     * TODO Document
     * @param components
     */
    public void setComponents(int components) { this.components = components; }

    /**
     * TODO Document
     * @return
     */
    public int getComponents() { return components; }

    /**
     * TODO Document
     * @param componentType
     */
    public void setComponentType(int componentType) { this.componentType = componentType; }

    /**
     * TODO Document
     * @return
     */
    public int getComponentType() { return componentType; }

    /**
     * TODO Document
     * @param normalized
     */
    public void setNormalized(boolean normalized) { this.normalized = normalized; }

    /**
     * TODO Document
     * @return
     */
    public boolean isNormalized() { return normalized; }
}
