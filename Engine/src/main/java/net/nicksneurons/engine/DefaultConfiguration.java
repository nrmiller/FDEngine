package net.nicksneurons.engine;

import net.nicksneurons.engine.framework.System;
import net.nicksneurons.engine.framework.config.ConfigurationPlugin;
import net.nicksneurons.engine.framework.config.EngineConfiguration;
import net.nicksneurons.engine.framework.config.EngineTask;
import net.nicksneurons.engine.framework.config.ScriptTask;
import net.nicksneurons.engine.systems.CollisionDetectionSystem;

import java.util.ArrayList;
import java.util.List;

public class DefaultConfiguration implements ConfigurationPlugin {

    private EngineConfiguration configuration;

    @Override
    public EngineConfiguration getConfiguration() {
        return configuration;
    }

    public String getName() {
        return "Default Configuration";
    }

    @Override
    public void onLoad() {
        configuration = new EngineConfiguration();

        List<System> systems = new ArrayList<>();
        systems.add(new CollisionDetectionSystem());

        configuration.setSystems(systems);

        List<EngineTask> updateTasks = new ArrayList<>();
        updateTasks.add(new ScriptTask<>(DefaultScript.class, "onUpdate"));
        updateTasks.add(new ScriptTask<>(DefaultScript.class, "onLateUpdate"));
        updateTasks.add(new ScriptTask<>(DefaultScript.class, "onPhysics"));
        updateTasks.add(new ScriptTask<>(DefaultScript.class, "onCollisions"));
        updateTasks.add(new ScriptTask<>(DefaultScript.class, "onRender"));

        configuration.setUpdateTasks(updateTasks);
    }

    @Override
    public void onUnload() {

    }
}
