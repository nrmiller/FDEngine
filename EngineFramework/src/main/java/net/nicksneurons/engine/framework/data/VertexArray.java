package net.nicksneurons.engine.framework.data;

/**
 * TODO Document
 *
 * Add/remove attributes are used to specify what the vertex array contains.
 */
public class VertexArray {

    private boolean interleaved;

    /**
     * TODO Document
     * Location cannot be added twice.
     * @param location
     * @param attribute
     */
    public void addAttribute(int location, VertexAttribute attribute) {

    }

    /**
     * TODO Document
     * @param attribute
     */
    public void removeAttribute(VertexArray attribute) {

    }

    /**
     * TODO Document
     * @param location
     */
    public void removeAttribute(int location) {

    }

    /**
     * TODO Document
     * Location needs to be valid.
     * TODO byte[] may need to change depending on component type.
     * @param location
     * @param data
     */
    public void setVertexData(int location, byte[] data) {

    }

    /**
     * TODO Document
     * @param interleaved
     */
    public void setInterleaved(boolean interleaved) { this.interleaved = interleaved; }

    /**
     * TODO Document
     * @return
     */
    public boolean isInterleaved() { return interleaved; }
}
