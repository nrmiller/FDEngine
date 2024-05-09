package net.nicksneurons.tools;

import java.lang.Math;

/**
 * TODO Document
 */
public class MathUtil {

    /**
     * TODO Document
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    /**
     * TODO Document
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static long clamp(long value, long min, long max) {
        return Math.max(min, Math.min(value, max));
    }

    /**
     * TODO Document
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    /**
     * TODO Document
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }
}
