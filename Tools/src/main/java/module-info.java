module Tools {
    requires org.lwjgl;
    requires org.lwjgl.opengl;
    requires org.lwjgl.glfw;
    requires org.lwjgl.stb;
    requires kotlin.stdlib;
    requires org.joml;
    exports net.nicksneurons.tools;
    exports net.nicksneurons.tools.errors;
    exports net.nicksneurons.tools.input;
}