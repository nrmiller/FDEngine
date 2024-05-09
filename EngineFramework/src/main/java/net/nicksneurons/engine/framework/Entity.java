package net.nicksneurons.engine.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Entity {
    private List<EntityListener> listeners = new ArrayList<>();
    private List<Component> components = new ArrayList<>();

    public Entity() {

    }

    public void addEntityListener(EntityListener listener) {
        if (listeners.contains(listener)) throw new IllegalArgumentException("Listener already added.");
        listeners.add(listener);
    }

    public void removeEntityListener(EntityListener listener) {
        //Note: we do not provide a means to remove listeners by index.
        //This would allow listeners to affect other listeners, which is bad.
        if (!listeners.contains(listener)) throw new IllegalArgumentException(("Listener not added."));
        listeners.remove(listener);
    }

    protected void postOnComponentAdded(Component component) {
        for (EntityListener listener : listeners) {
            listener.onComponentAdded(this, component);
        }
    }

    protected void postOnComponentRemoved(Component component) {
        for (EntityListener listener : listeners) {
            listener.onComponentRemoved(this, component);
        }
    }

    public void addComponent(Component component) {
        if (components.contains(component)) throw new IllegalArgumentException("Component already added.");
        components.add(component);

        //Notify any listeners.
        postOnComponentAdded(component);
    }

    public void removeComponent(Component component) {
        if (!components.contains(component)) throw new IllegalArgumentException("Component not added.");
        components.remove(component);

        //Notify any listeners.
        postOnComponentRemoved(component);
    }

    public Component removeComponentAt(int index) {
        if (index < 0 || index >= components.size()) throw new IndexOutOfBoundsException("Invalid index...");
        Component component = components.remove(index);

        //Notify listeners.
        postOnComponentRemoved(component);

        return component;
    }

    public List<Component> getComponents() {
        return Collections.unmodifiableList(components);
    }
}

