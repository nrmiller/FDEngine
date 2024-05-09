package net.nicksneurons.engine.framework;

public interface EntityListener {

    void onComponentAdded(Entity sender, Component component);
    void onComponentRemoved(Entity sender, Component component);
}
