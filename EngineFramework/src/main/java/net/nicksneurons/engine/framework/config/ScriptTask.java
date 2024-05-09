package net.nicksneurons.engine.framework.config;

import net.nicksneurons.engine.framework.ScriptComponent;

/**
 * TODO Document
 * @param <ScriptType>
 */
public class ScriptTask<ScriptType extends ScriptComponent> extends EngineTask {

    private Class<ScriptType> scriptClass;
    private String methodName;

    /**
     * TODO Document
     * @param scriptClass
     * @param methodName
     */
    public ScriptTask(Class<ScriptType> scriptClass, String methodName) {
        this.scriptClass = scriptClass;
        this.methodName = methodName;
    }

    /**
     * TODO Document
     * @return
     */
    public Class<ScriptType> getScriptClass() {
        return scriptClass;
    }

    /**
     * TODO Document
     * @return
     */
    public String getMethodName() {
        return methodName;
    }
}
