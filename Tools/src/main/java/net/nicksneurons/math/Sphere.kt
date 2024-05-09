package net.nicksneurons.math

/**
 * This class represents a 3d sphere in space.
 *
 * @remarks
 * It subscribes to the philosophy that all objects should be immutable at first.
 * If at a later time this is needed to be mutable we can consider that then.
 * Usually, small data objects should be immutable, and larger entities should be mutable.
 */
data class Sphere(val radius: Float = 1.0f, val position: Vector3f = Vector3f())