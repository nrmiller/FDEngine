package net.nicksneurons.tools;

import static org.lwjgl.glfw.GLFW.*;

public enum GLClientAPI {
    OPENGL_API(GLFW_OPENGL_API),
    OPENGL_ES_API(GLFW_OPENGL_ES_API);

    private int value;

    GLClientAPI(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
