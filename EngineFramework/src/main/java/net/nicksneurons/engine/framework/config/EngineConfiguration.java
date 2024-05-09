package net.nicksneurons.engine.framework.config;

import net.nicksneurons.engine.framework.System;

import java.util.List;

public class EngineConfiguration {
    //Note: This is the list of systems made available to the Engine and are used within
    //the various engine tasks.
    List<System> systems;
    List<EngineTask> updateTasks;
    List<EngineTask> renderTasks;

    public void setSystems(List<System> systems) {
        this.systems = systems;
    }

    public List<System> getSystems() {
        return systems;
    }

    public void setUpdateTasks(List<EngineTask> updateTasks) {
        this.updateTasks = updateTasks;
    }

    public List<EngineTask> getUpdateTasks() {
        return updateTasks;
    }

    public void setRenderTasks(List<EngineTask> renderTasks) {
        this.renderTasks = renderTasks;
    }

    public List<EngineTask> getRenderTasks() {
        return renderTasks;
    }
}
