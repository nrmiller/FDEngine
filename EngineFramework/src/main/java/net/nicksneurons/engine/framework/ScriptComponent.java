package net.nicksneurons.engine.framework;

/**
 * TODO Document
 *
 * A script component contains various callback functions so that
 * an Entity can respond to system events, and so that the Entity
 * can have a user-defined behavior.
 *
 * For example, a ScriptComponent may be added which animates a platform
 * between a start and end point. This platform may need to react to collisions, hence,
 * it might have a behavior (as defined in a subclass of ScriptComponent) that causes
 * its direction to change after a collision.
 * Hence, the behavior may need to act before and after different systems (e.g. the
 * physics system) to have a finely-tuned behavior.
 *
 * The callback functions that belong to a script component will be invoked at particular
 * stages in the Engine's System-Loop and will be based on a runtime configuration.
 */
public class ScriptComponent extends Component {
}
