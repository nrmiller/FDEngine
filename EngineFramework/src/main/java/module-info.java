module EngineFramework {
    requires Tools;
    requires java.xml.bind;
    requires javafx.graphics;
    requires javafx.controls;
    requires org.lwjgl.glfw;
    requires kotlin.stdlib;
    exports net.nicksneurons.engine.framework;
    exports net.nicksneurons.engine.framework.config;
    exports net.nicksneurons.engine.framework.data;
    exports net.nicksneurons.engine.framework.debug;

    // Allow JAXB to be able to deserialize PluginInfo.
    opens net.nicksneurons.engine.framework.config to java.xml.bind;

    // Allow JavaFX to have access to the internal framework implementation.
    opens net.nicksneurons.engine.framework.frameworkimpl to javafx.graphics;
}