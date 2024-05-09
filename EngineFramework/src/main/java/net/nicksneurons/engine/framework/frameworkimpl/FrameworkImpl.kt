package net.nicksneurons.engine.framework.frameworkimpl

import net.nicksneurons.engine.framework.Engine
import net.nicksneurons.engine.framework.config.ConfigurationLoader
import net.nicksneurons.tools.*
import javafx.stage.Stage
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import javafx.application.Platform
import net.nicksneurons.engine.framework.debug.BuildConfig

class FrameworkImpl : javafx.application.Application(), GLWindowListener {

    private lateinit var primaryStage: Stage
    override fun start(primaryStage: Stage?) {
        if (!GLFW.glfwInit())
            throw IllegalStateException("Cannot start application!")

        if (primaryStage == null)
            throw IllegalStateException("Cannot start application! No primary stage")

        this.primaryStage = primaryStage

        // Show GLFW errors if debugging is enabled.
        if (BuildConfig.DEBUG_MODE && BuildConfig.GLFW_DEBUGGING_ENABLED) {
            GLFWErrorCallback.createPrint(System.err).set()
        }

        val configLoader = ConfigurationLoader("out\\artifacts\\EngineConfiguration\\EngineConfiguration.jar")
        val plugin = configLoader.loadConfiguration()
        plugin.onLoad()
        val config = plugin.configuration
        System.out.printf("Plugin Name: \'%s\'\n", plugin.name)

        val engine = Engine(config)

        val window = GLWindow("Fractal Dungeon", 800, 600)

        //TODO We need to pass a different Animator
        //TODO Why do some properties have the shorted syntax?
        window.animator = DefaultAnimator()
        window.setGLProfile(GLProfile.OPENGL_CORE_PROFILE)
        window.setGLClientAPI(GLClientAPI.OPENGL_API)
        window.setGLVersion(3, 3)
        window.glEventListener = engine
        window.updateListener = engine
        window.addWindowListener(this)
        window.show()

        /*engine.setTargetFPS(80);
        //engine.setLimitFPS(false);
        engine.setAveragingSamples(10);
        engine.start();*/
    }

    override fun onWindowOpened(source: GLWindow?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onWindowClosing(source: GLWindow?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onWindowClosed(source: GLWindow?) {
        System.out.println("Window Closed")

        //TODO Implement this.
        /*if (!closedByUser) {
            javafx.application.Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Application Quit Unexpectedly.");
                alert.setContentText("The application quit unexpectedly with the following message: ???");
                alert.showAndWait();
            });
        }*/

        Platform.runLater {
            // Closing will not terminate the app unless the stage is first shown.
            primaryStage.close();

            // Therefore we must manually exit as well.
            Platform.exit()
        }
    }
}