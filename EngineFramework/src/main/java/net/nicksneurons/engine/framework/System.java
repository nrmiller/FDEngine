package net.nicksneurons.engine.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TODO Document
 */
public abstract class System {

    private List<Entity> entities = new ArrayList<>();
    private Set<Class<? extends Component>> requiredComponentClasses;

    public System() {
        //During the instantiation of the System, we must validate the required components list.
        //Also, we should cache the components list unless we wish to validate more than once.
        //The issue with allowing systems to change the required components they support is that we would need
        //a mechanism to detect those changes and change the list of entities added to a system.
        //A simpler implementation would be to prevent this altogether, and this should also add to the cohesion
        //of the design (single responsibility principle).
        //Until we see a viable use for dynamic systems, we will not support it (see also YAGNI principle).
        requiredComponentClasses = getRequiredComponents();
        if (requiredComponentClasses == null || requiredComponentClasses.isEmpty()) {
            throw new IllegalStateException("The system cannot have a null or empty list of required component classes.");
        }
    }

    /**
     * TODO Document
     * @param entity
     */
    public void addEntity(Entity entity) {
        if (entities.contains(entity)) throw new IllegalArgumentException("Entity already added.");

        //Check if the entity has the correct components.
        List<Class<? extends Component>> entityComponentClasses = entity.getComponents().stream().map(Component::getClass).collect(Collectors.toList());
        if (entityComponentClasses.containsAll(requiredComponentClasses)) {
            //The entity has all the required components, it can be added to the system.
            entities.add(entity);
        }

        //Build an error message to indicate the missing required components.
        StringBuilder failureMessage = new StringBuilder();

        List<Class<? extends Component>> missingClasses = requiredComponentClasses.stream().filter(requiredClass -> (!entityComponentClasses.contains(requiredClass))).collect(Collectors.toList());
        for (int index = 0; index < missingClasses.size(); index++) {
            failureMessage.append(missingClasses.get(index).getCanonicalName());
            if (index < missingClasses.size() - 1) {
                failureMessage.append(", ");
            }
        }
        throw new IllegalArgumentException(String.format("The entity does not contain the following required components: %s.", failureMessage.toString()));
    }

    /**
     * TODO Document
     * @param entity
     */
    public void removeEntity(Entity entity) {
        if (!entities.contains(entity)) throw new IllegalArgumentException("Entity not added.");
        entities.remove(entity);
    }

    /**
     * TODO Document
     * @return
     */
    public abstract Set<Class<? extends Component>> getRequiredComponents();

    /**
     * TODO Document
     * @param deltaTime
     */
    public abstract void onUpdate(float deltaTime);
}
