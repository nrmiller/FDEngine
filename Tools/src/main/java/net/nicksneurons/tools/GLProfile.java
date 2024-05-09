package net.nicksneurons.tools;

import static org.lwjgl.glfw.GLFW.*;

public enum GLProfile {
    OPENGL_CORE_PROFILE(GLFW_OPENGL_CORE_PROFILE),
    OPENGL_COMPAT_PROFILE(GLFW_OPENGL_COMPAT_PROFILE);

    private int value;

    GLProfile(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
