package net.nicksneurons.tools.input;

public interface MouseListener {

    void onMouseButtonDown(int button, int modifiers, double x, double y);
    void onMouseButtonUp(int button, int modifiers, double x, double y);
    void onMouseMove(double deltaX, double deltaY);
}
