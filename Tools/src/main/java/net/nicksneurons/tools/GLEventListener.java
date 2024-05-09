package net.nicksneurons.tools;

public interface GLEventListener {
    void onSurfaceCreated(int width, int height);
    void onDrawFrame();
    void onSurfaceChanged(int width, int height);
    void onSurfaceDestroyed();
}
