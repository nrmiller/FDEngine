package net.nicksneurons.tools.input;

public class Modifier {

    public static int SHIFT = 0x0001;
    public static int CONTROL = 0x0002;
    public static int ALT = 0x0004;
    public static int SUPER = 0x0008;
    public static int CAPS_LOCK = 0x0010;
    public static int NUM_LOCK = 0x0020;

    public static boolean has(int modifiers, int other) {
        return (modifiers & other) == other;
    }
}
