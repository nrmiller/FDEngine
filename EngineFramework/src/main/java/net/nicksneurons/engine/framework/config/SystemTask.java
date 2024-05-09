package net.nicksneurons.engine.framework.config;

import net.nicksneurons.engine.framework.System;

public class SystemTask extends EngineTask {

    private System system;

    public SystemTask(System system) {
        this.system = system;
    }

    public System getSystem() {
        return system;
    }

    /*@Override
    public void run() {
        //TODO Is it okay to just compute the deltaTime parameter here?
        system.onUpdate(0);
    }*/
}
