package net.nicksneurons.engine.framework.config;

/**
 * TODO Is there really a need for an empty abstract class?
 * Currently, this is only used to group ScriptTask and SystemTask as being children of a common
 * type that is not "Object".
 * Is there added compile-time type safety by doing this?
 * Are there any methods/fields that should be included in this abstract class?
 *
 * In terms of maintainability, it is quite cohesive to thing of the engine as processing "Engine Tasks".
 */
public abstract class EngineTask {
}
