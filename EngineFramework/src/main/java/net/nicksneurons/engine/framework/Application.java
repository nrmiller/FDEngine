package net.nicksneurons.engine.framework;

import net.nicksneurons.engine.framework.config.ConfigurationLoader;
import net.nicksneurons.engine.framework.config.ConfigurationPlugin;
import net.nicksneurons.engine.framework.config.EngineConfiguration;
import javafx.stage.Stage;
import net.nicksneurons.tools.*;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.glfwInit;

import static java.lang.System.out;
import static java.lang.System.err;

/**
 * @deprecated This class has been relocated outisde of the engine and into the game module.
 */
@Deprecated
public class Application extends javafx.application.Application implements GLWindowListener {

    public static void main(String[] args) {
        javafx.application.Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (!glfwInit())
            throw new IllegalStateException("Cannot start application!");

        //TODO Remove for release mode, or add a debug flags.
        GLFWErrorCallback.createPrint(err).set();

        ConfigurationLoader configLoader = new ConfigurationLoader("out\\artifacts\\Configuration\\Configuration.jar");
        ConfigurationPlugin plugin = configLoader.loadConfiguration();
        plugin.onLoad();
        EngineConfiguration config = plugin.getConfiguration();
        out.printf("Plugin Name: \'%s\'\n", plugin.getName());

        Engine engine = new Engine(config);

        GLWindow window = new GLWindow("Fractal Dungeon", 800, 600);

        //TODO We need to pass a different Animator
        window.setAnimator(new DefaultAnimator());
        window.setGLProfile(GLProfile.OPENGL_CORE_PROFILE);
        window.setGLClientAPI(GLClientAPI.OPENGL_API);
        window.setGLVersion(3, 3);
        window.setGLEventListener(engine);
        window.setUpdateListener(engine);
        window.addWindowListener(this);
        window.show();

        /*engine.setTargetFPS(80);
        //engine.setLimitFPS(false);
        engine.setAveragingSamples(10);
        engine.start();*/
    }

    @Override
    public void onWindowOpened(GLWindow source) {
        out.println("Window Opened");
    }

    @Override
    public void onWindowClosing(GLWindow source) {
        out.println("Window Closing");
    }

    @Override
    public void onWindowClosed(GLWindow source) {
        out.println("Window Closed");

        //TODO Implement this.
        /*if (!closedByUser) {
            javafx.application.Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Application Quit Unexpectedly.");
                alert.setContentText("The application quit unexpectedly with the following message: ???");
                alert.showAndWait();
            });
        }*/
    }
}
