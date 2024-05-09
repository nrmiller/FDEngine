package net.nicksneurons.engine.components;

import net.nicksneurons.engine.framework.Component;
import net.nicksneurons.tools.Vector2f;

/**
 * TODO Document
 * This is a base class for all colliders
 * There are various collider types we may wish to support.
 * https://docs.unity3d.com/Manual/Collider2D.html
 *
 * Made abstract because the base collider does not define a boundary.
 *
 * TODO Add more fields/properties with get/set methods.
 */
public abstract class Collider2D extends Component {

    //Since a material is only used for collision response, it only makes sense to couple it to the collider.
    //We do not need it as a separate component.
    private PhysicsMaterial material;

    //Offset relative to the transform.
    private Vector2f offset;

    /**
     * TODO We may need this to help prevent tunneling.
     * The ECS model requires that components contain only state information.
     * It is okay to have functions, in a component class, but it should not reach outside of
     * the 'database/model' layer. That is to say, it should not have any dependency on any particular system.
     * https://docs.unity3d.com/ScriptReference/Collider2D.Cast.html
     * @returns TODO a cast result?
     */
    public abstract int cast(Vector2f direction, float distance);
}
