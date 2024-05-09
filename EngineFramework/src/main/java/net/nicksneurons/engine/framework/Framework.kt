package net.nicksneurons.engine.framework

import net.nicksneurons.engine.framework.frameworkimpl.FrameworkImpl

/**
 * The Framework class is responsible for initializing the core components used
 * by the engine framework.
 * @apiNote The reason for this class is to primarily abstract away the setup of any
 *          third party dependencies.
 * TODO High javafx.application.Application and GLWindowListener members by using another class (e.g. anonymous class).
 */
class Framework {

    /**
     * Launches the framework.
     */
    fun launch(vararg args: String) {
        javafx.application.Application.launch(FrameworkImpl::class.java, *args)
    }
}