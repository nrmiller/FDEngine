package net.nicksneurons.tools;

public abstract class Animator implements Runnable {

    private AnimatorListener animatorListener;
    private Thread thread;
    private boolean isRunning = false;
    private boolean initialized = false;

    public void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public void stop() {
        isRunning = false;
        thread = null;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Thread getThread() {
        return thread;
    }

    public void setAnimatorListener(AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    public AnimatorListener getAnimatorListener() {
        return animatorListener;
    }

    protected void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public boolean isInitialized() {
        return initialized;
    }

    protected void postOnCreate() {
        if (animatorListener != null) {
            animatorListener.onCreate();
        }
    }

    protected void postOnUpdate(double delta) {
        if (animatorListener != null) {
            animatorListener.onUpdate(delta);
        }
    }

    protected void postOnDestroy() {
        if (animatorListener != null) {
            animatorListener.onDestroy();
        }
    }
}
