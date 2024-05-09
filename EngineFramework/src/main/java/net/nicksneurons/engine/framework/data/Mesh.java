package net.nicksneurons.engine.framework.data;

/**
 * TODO Document
 */
public class Mesh {

    private int[] indices;
    private VertexArray vertices;

    //TODO Texture things...

    /**
     * TODO Document
     */
    public Mesh() {

    }

    /**
     * TODO Document
     * @param indices
     */
    public void setIndices(int[] indices) { this.indices = indices; }

    /**
     * TODO Document
     * @return
     */
    public int[] getIndices() { return indices; }

    /**
     * TODO Document
     * @param vertices
     */
    public void setVertexArray(VertexArray vertices) { this.vertices = vertices; }

    /**
     * TODO Document
     * @return
     */
    public VertexArray getVertexArray() { return vertices; }
}
