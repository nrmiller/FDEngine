package net.nicksneurons.engine.framework;

import net.nicksneurons.tools.errors.ExitDetails;

public enum ExitCodes implements ExitDetails {
    ConfigPluginDeserializationError(0x00000001, "Cannot deserialize \'Config.xml\'.");

    private int exitCode = 0;
    private String message = null;

    ExitCodes(int exitCode, String message) {
        this.exitCode = exitCode;
        this.message = message;
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
