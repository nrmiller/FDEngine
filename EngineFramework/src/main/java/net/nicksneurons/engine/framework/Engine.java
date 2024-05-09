package net.nicksneurons.engine.framework;

import net.nicksneurons.engine.framework.config.EngineConfiguration;
import net.nicksneurons.engine.framework.config.EngineTask;
import net.nicksneurons.engine.framework.config.ScriptTask;
import net.nicksneurons.engine.framework.config.SystemTask;
import net.nicksneurons.tools.GLEventListener;
import net.nicksneurons.tools.UpdateListener;
import net.nicksneurons.tools.input.KeyListener;
import net.nicksneurons.tools.input.MouseListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;
import static java.lang.System.err;

/**
 * TODO Document
 * TODO The engine must support handling multiple systems in a configurable order.
 * TODO A script component must have callbacks consistent with this configuration.
 *
 * We can create a default configuration and a default "ScriptComponent".
 * Any other script components would need to inherit ScriptComponent or Component (or Behavior?)
 *
 * This class must support:
 * 1. Locating and using a custom configuration of Systems
 *  - This is called the 'System-Loop Configuration'
 *
 * 2. Instantiating Systems
 * 3. Invoking each System's update function.
 * 4. Keeping track of time.
 * 5. Invoking each ScriptComponent's callback functions based on another configuration.
 *  - This configuration is intertwined with the System-Loop Configuration
 * 6. Managing/storing all Entities.
 * 7. Filtering the entities according to the components they have, since each system will only
 *    operate entities with the correct components.
 *    Note: Systems may depend on more than one component. For example, the Collision Detection System
 *    requires the following components (Transform2D, RigidBody2D, and Collider2D).
 */
public class Engine implements GLEventListener, UpdateListener, EntityListener, KeyListener, MouseListener {

    /**
     * This holds a map that maps component classes literals to a list of components
     * of the same type.
     * There is no better solution to doing in Java since generic introspection has
     * limitations due to type erasure.
     */
    @SuppressWarnings("unchecked")
    Map<Class<? extends Component>, List<? extends Component>> components = new HashMap<>();

    //region TODO remove these.
    private int sampleIndex = 0;
    DecimalFormat formatter = new DecimalFormat("#00.000");
    DecimalFormat formatter2 = new DecimalFormat(" #0.000000;-#0.000000");
    volatile long l;
    volatile double d;
    //endregion

    private EngineConfiguration config;

    public Engine(EngineConfiguration config) {
        this.config = config;
    }

    /**
     * TODO Document
     * The purpose of this method is to add an entity to the engine.
     * This will organize all of the entity's components into the engine's component map.
     *
     * @param entity
     */
    public void addEntity(Entity entity) {
        //TODO Implement me!


    }

    @SuppressWarnings("unchecked")
    private <T extends Component> void putComponents(Class<T> clazz, List<T> components)
    {
        List<T> registeredComps = (List<T>)this.components.get(clazz);
        //registeredComps.add
        registeredComps.addAll(components);
    }

    private <T extends Component> List<T> get(Class<T> clazz)
    {
        return (List<T>) components.get(clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onUpdate(double delta) {
        try {
            for (EngineTask task : config.getUpdateTasks()) {
                if (task instanceof ScriptTask<?>) {
                    ScriptTask<? extends ScriptComponent> scriptTask = (ScriptTask<? extends ScriptComponent>) task;
                    executeScriptTask(scriptTask);
                }
                else if (task instanceof SystemTask) {
                    /*
                    TODO Problem: if the task is a system task, then it needs to be passed
                    the components that that system is responsible for handling.
                     */

                }
            }

        } catch(EngineException e) {
            throw new RuntimeException(e);
        }
        /*for (int i = 0; i < 1000000; i++) {
            l = (long)(50.0d * 100.0d);
        }
        for (int i = 0; i < 1000000; i++) {
            d = Math.sqrt(355);
            d++;
        }*/

        /*try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            System.out.println("Forced sleep was interrupted.");
        }*/
        //This is where we invoke systems and script callbacks.
        //System.out.println(++sampleIndex + ", " + String.format(getCurrentFPS() + ", " + (getAccumulatedLag() / 1000000000L)));
        //System.out.println(String.format("Avg FPS: %s, Accumulated Lag: %s", formatter.format(getAverageFPS()), formatter2.format((getAccumulatedLag() / 1000000000L))));

        out.println("Engine Update");
    }

    /**
     * TODO Document
     * TODO The exceptions in  this function all need to be tested. The descriptions may not be entirely accurate.
     * @param scriptTask
     * @param <ScriptType>
     * @throws EngineException
     */
    @SuppressWarnings("unchecked")
    private <ScriptType extends ScriptComponent> void executeScriptTask(ScriptTask<ScriptType> scriptTask) throws EngineException {

        Class<ScriptType> clazz = scriptTask.getScriptClass();

        /*System.out.println("Executing task: " + scriptTask.getClass().getCanonicalName());
        System.out.println("--" + scriptTask.getScriptClass().getCanonicalName() + "(" + scriptTask.getScriptClass().getClassLoader().getName() + ")");
        System.out.println("--" + scriptTask.getMethodName());*/

        //Note: this is an unchecked cast since there isn't a way to ensure type safety with the components map.
        List<ScriptType> scriptComponents = (List<ScriptType>) components.get(scriptTask.getScriptClass());

        try {
            Method callbackMethod = clazz.getMethod(scriptTask.getMethodName());

            //Check modifiers to see if the method was defined correctly:
            int modifiers = callbackMethod.getModifiers();
            if (Modifier.isStatic(modifiers) ||
                    Modifier.isAbstract(modifiers) ||
                    !Modifier.isPublic(modifiers)) {
                throw new EngineException(String.format("The script callback method \'%s\' in the class \'%s\' cannot static or abstract, and it must be public.", scriptTask.getMethodName(), clazz.getCanonicalName()));
            }
            else if (!callbackMethod.getReturnType().equals(Void.TYPE)) {
                throw new EngineException(String.format("The script callback method \'%s\' in the class \'%s\' must have a void return type.", scriptTask.getMethodName(), clazz.getCanonicalName()));
            }
            else {
                //Execute the script callback method for all script components currently registered into the engine.
                //Note: If scriptComponents is null, then no entities have been added that make use of the script component class.
                if (scriptComponents != null) {
                    for (ScriptType script : scriptComponents) {
                        callbackMethod.invoke(script);
                    }
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException e) {
            err.println(e.getClass().getCanonicalName());
            throw new EngineException(String.format("The script class \'%s\' does not have a public, parameterless method with the name \'%s\'", clazz.getCanonicalName(), scriptTask.getMethodName()), e);
        } catch (InvocationTargetException e) {
            throw new EngineException(String.format("The script callback method \'%s\' in the class \'%s\' threw an exception.", scriptTask.getMethodName(), clazz.getCanonicalName()), e);
        }
    }

    @Override
    public void onDrawFrame() {
        out.println("Draw Frame");
    }

    @Override
    public void onSurfaceCreated(int width, int height) {
        out.println("Surface Created");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        out.println("Surface Changed: (" + width + ", " + height + ")");
    }

    @Override
    public void onSurfaceDestroyed() {
        out.println("Surface Destroyed");
    }

    @Override
    public void onComponentAdded(Entity sender, Component component) {
        //Component added to an entity, check to see if the entity should
        //be operated on by any systems.
        //TODO Implement.
    }

    @Override
    public void onComponentRemoved(Entity sender, Component component) {
        //Component removed from an entity, check if the entity should be
        //removed from any systems.
        //TODO Implement.
    }

    @Override
    public void onKeyDown(int key, int scancode, int modifiers) {

    }

    @Override
    public void onKeyRepeat(int key, int scancode, int modifiers) {

    }

    @Override
    public void onKeyUp(int key, int scancode, int modifiers) {

    }

    @Override
    public void onMouseButtonDown(int button, int modifiers, double x, double y) {

    }

    @Override
    public void onMouseButtonUp(int button, int modifiers, double x, double y) {

    }

    @Override
    public void onMouseMove(double x, double y) {

    }

    private class EngineException extends Throwable {

        public EngineException() {
            super();
        }

        public EngineException(String message) {
            super(message);
        }

        public EngineException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
