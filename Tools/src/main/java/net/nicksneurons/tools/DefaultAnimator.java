package net.nicksneurons.tools;

/**
 * TODO Create an Animator very similar to this that sleeps at 1 ms intervals in a while loop.
 * The full implementation details can be found on the LWJGL tutorial book (see page 11).
 */
public class DefaultAnimator extends Animator {

    private Long lastUpdatedTime = null, currentUpdatedTime = null;;

    @Override
    public void run() {
        postOnCreate();
        setInitialized(true);

        while(isRunning()) {

            currentUpdatedTime = System.nanoTime();
            if (lastUpdatedTime == null) {
                postOnUpdate(0);
            }
            else {
                long deltaNs = currentUpdatedTime - lastUpdatedTime;
                postOnUpdate((double)deltaNs / 1_000_000_000);
            }
            lastUpdatedTime = currentUpdatedTime;
        }

        postOnDestroy();
    }
}
