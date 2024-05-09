package net.nicksneurons.engine.framework.debug

class BuildConfig {
    companion object {
        // This field indicates if general debugging is enabled.
        @JvmField
        var DEBUG_MODE = true

        // This field indicates if GLFW debugging is enabled. General debugging must also be enabled for this to work.
        @JvmField
        var GLFW_DEBUGGING_ENABLED = true
    }
}