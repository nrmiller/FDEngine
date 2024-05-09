package net.nicksneurons.engine.systems;

import net.nicksneurons.engine.components.Collider2D;
import net.nicksneurons.engine.framework.Component;
import net.nicksneurons.engine.framework.System;

import java.util.HashSet;
import java.util.Set;

public class CollisionDetectionSystem extends System {

    @Override
    public Set<Class<? extends Component>> getRequiredComponents() {
        Set<Class<? extends Component>> requiredComponents = new HashSet<>();
        requiredComponents.add(Collider2D.class);
        return requiredComponents;
    }

    @Override
    public void onUpdate(float deltaTime) {

    }
}
