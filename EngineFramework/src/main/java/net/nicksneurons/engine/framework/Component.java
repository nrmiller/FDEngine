package net.nicksneurons.engine.framework;

import java.util.ArrayList;

/**
 * TODO Document
 */
public class Component {

    private Entity entity;
    private ArrayList<String> tags = new ArrayList<String>();

    /**
     * TODO Document
     * TODO Should components be bound to a single entity through their lifetimes?
     * @param entity
     */
    public void setEntity(Entity entity) { this.entity = entity; }

    /**
     * TODO Document
     */
    public Entity getEntity() { return entity; }

    /**
     * TODO Document
     * @param tag
     */
    public void addTag(String tag) {
        tags.add(tag);
    }

    /**
     * TODO Document
     * @param tag
     * @return
     */
    public boolean removeTag(String tag) {
        if (tags.contains(tag)) {
            return tags.remove(tag);
        }
        return false;
    }
}

