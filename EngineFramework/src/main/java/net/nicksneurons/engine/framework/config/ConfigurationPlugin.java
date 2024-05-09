package net.nicksneurons.engine.framework.config;

import net.nicksneurons.engine.framework.Plugin;

public interface ConfigurationPlugin extends Plugin {
    EngineConfiguration getConfiguration();

    String getName();
}
