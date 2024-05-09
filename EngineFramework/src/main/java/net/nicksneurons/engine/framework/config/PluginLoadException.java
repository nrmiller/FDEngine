package net.nicksneurons.engine.framework.config;

public class PluginLoadException extends Exception {

    public PluginLoadException() {
        super();
    }

    public PluginLoadException(String message) {
        super(message);
    }

    public PluginLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
