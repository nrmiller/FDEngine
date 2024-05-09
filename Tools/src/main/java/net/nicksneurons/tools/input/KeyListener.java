package net.nicksneurons.tools.input;

public interface KeyListener {
    void onKeyDown(int key, int scancode, int modifiers);
    void onKeyRepeat(int key, int scancode, int modifiers);
    void onKeyUp(int key, int scancode, int modifiers);
}
