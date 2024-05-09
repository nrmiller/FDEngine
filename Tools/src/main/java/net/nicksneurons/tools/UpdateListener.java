package net.nicksneurons.tools;

/**
 * TODO Document
 * TODO Consider adding "attached" and "detached" event callbacks.
 */
public interface UpdateListener {

    /**
     * Called periodically when the observed object updates
     * @param delta - the time since the last update (in seconds)
     */
    void onUpdate(double delta);
}
