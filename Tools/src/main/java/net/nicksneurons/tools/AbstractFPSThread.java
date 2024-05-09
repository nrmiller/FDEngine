package net.nicksneurons.tools;

/**
 * TODO Document
 * This class creates a thread that precisely manages FPS.
 * It supports a fixed FPS mode, and an unlimited FPS mode.
 *
 * TODO Monitor how fast lag reaches the threshold. This is the true measure for notifying the user
 * that their machine cannot handle the current settings.
 * TODO We can implement frame skipping to that updates take a shorter amount of time.
 * Basically, we update the game without rendering for frame or two. This can be done to keep the lag in check.
 * In order to implement this, we will need to pass a parameter to the update function, and rely on the implementer
 * to respect the thread's wishes.
 * TODO Note: if the target frame rate is set low enough, then the target frame rate is never reached.
 * We should look at using predictive logic to stabilize the FPS. We may need to look at PID (proportional, integral, derivative).
 * For some reason, the FPS is too high if the update is too fast (compared to target FPS interval).
 * @deprecated Move logic to FPSUpdater. Fix the bugs. Also create an updater that uses PID so we can test it...
 */
@Deprecated
public abstract class AbstractFPSThread implements Runnable {

    //TODO Java has a TimeUnit enum that we should make use of.
    //https://stackoverflow.com/a/27108104/975724
    private static final long NANOS_PER_SECOND = 1000000000L;
    private static final double SECONDS_PER_NANO = 1.0d / NANOS_PER_SECOND;
    private static final double MILLIS_PER_NANO = 1.0d / 1000000L;

    private static final int DEFAULT_FPS = 60;

    //By default, allow the thread to accumulate up to 5 seconds of lag.
    private static final int DEFAULT_LAG_THRESHOLD_MILLIS = 5000;

    private Thread thread;
    private boolean running = false;

    /* Public state variables */
    private boolean limitFPS = true;
    private double targetFPS = DEFAULT_FPS;
    private double currentFPS;
    private double lagThreshold = DEFAULT_LAG_THRESHOLD_MILLIS * MILLIS_PER_NANO;
    private boolean lagging = false;

    /* Internal time measurement variables. */
    private double elapsedTimeNanos;

    //The goal is to keep accumulated lag close to zero.
    //Accumulated lag is in nanos.
    private double accumulatedLag;
    private double sleepDeficit;

    private int sampleIndex = 0;
    private double[] averagingBuffer = new double[10];

    public void setAveragingSamples(int samples) {
        if (running) throw new IllegalStateException("This operation is not valid while the thread is running.");

        averagingBuffer = new double[samples];
    }

    public double getAverageFPS() {
        double average = 0;
        for (double sample : averagingBuffer) {
            average += sample;
        }
        return average / averagingBuffer.length;
    }

    public void start() {
        thread = new Thread(this);
        running = true;
        thread.start();
    }

    public void stop() {
        running = false;
        thread = null;
    }

    //TODO Consider using a "Mode" enum b/c 'isFPSLimited' is an awkward name.
    public void setLimitFPS(boolean limitFPS) { this.limitFPS = limitFPS; }
    public boolean isFPSLimited() { return limitFPS; }

    public void setTargetFPS(double targetFPS) { this.targetFPS = targetFPS; }

    public double getTargetFPS() { return targetFPS; }

    public double getCurrentFPS() { return currentFPS; }

    public void setLagThreshold(int millis) { this.lagThreshold = millis; }
    public boolean isLagging() { return lagging; }

    public double getAccumulatedLag() { return accumulatedLag; }

    @Override
    public void run() {
        while (running) {

            long previousTime = System.nanoTime();
            onThreadUpdate();
            if (limitFPS) {
                elapsedTimeNanos = System.nanoTime() - previousTime;

                //FPS = frames per second = frames / seconds.
                //TODO Do we need to work in seconds? This will lose precision.
                //double elapsedTimeInSeconds = (elapsedTimeNanos / 1000000000L);

                //Now that we've seen how long the thread has taken, we need to see how
                //long it should sleep in order to main the FPS. This only needs to be done
                //if we are trying limit the FPS.
                //Start by calculating the target elapsed time.
                double targetElapsedTimeInNanos = (1.0d / targetFPS * NANOS_PER_SECOND);

                double difference = elapsedTimeNanos - targetElapsedTimeInNanos;
                if (difference > 0) {
                    //We took too long!!!
                    accumulatedLag += difference;
                } else {
                    //We finished on time or early.
                    double extraTime = -difference;

                    if (accumulatedLag > 0) {
                        //We are behind, don't sleep!
                        //But now we have time to decrease the accumulated lag.
                        accumulatedLag -= extraTime;
                    } else {
                        //We are on schedule, sleep normally.
                        long beforeSleep = System.nanoTime();
                        double targetSleepTime = (extraTime + sleepDeficit);
                        long sleepTime;
                        try {
                            //System.out.println("Sleeping for: " + (long)(targetSleepTime * MILLIS_PER_NANO) + "ms");
                            Thread.sleep((long) (targetSleepTime * MILLIS_PER_NANO));
                            sleepTime = System.nanoTime() - beforeSleep;

                            if (sleepTime > targetSleepTime) {
                                //We slept too long!!!
                                //Add to the lag, we'll deal with it later.
                                accumulatedLag += (sleepTime - targetSleepTime);
                                sleepDeficit = 0; //We slept too long, but took care of the sleep deficit.
                            } else {
                                //We slept just enough or not enough.
                                accumulatedLag -= (sleepTime - targetSleepTime);
                                if (accumulatedLag < 0) {
                                    //If there was previously a sleep deficit, we've already taken care of it.
                                    //We can safely reassign it.
                                    sleepDeficit = -accumulatedLag;
                                    accumulatedLag = 0;
                                } else {
                                    //We are behind schedule (b/c accumulatedLag >= 0)
                                    //Also, we slept too little.
                                    //We didn't take care of all the lag, so we unfortunately
                                    //need to just skip to the next frame.
                                }
                            }
                        } catch (InterruptedException e) {
                            //Interrupted, we should calculate how long it actually slept.
                            sleepTime = System.nanoTime() - beforeSleep;

                            //We did not sleep the full duration because we were interrupted.
                            //So, we can decrease the accumulated lag.
                            accumulatedLag -= (sleepTime - targetSleepTime);
                            if (accumulatedLag < 0) {
                                //If there was previously a sleep deficit, we've already taken care of it.
                                //We can safely reassign it.
                                sleepDeficit = -accumulatedLag;
                                accumulatedLag = 0;
                            } else {
                                //We are behind schedule (b/c accumulatedLag >= 0)
                                //Also, we slept too little.
                                //We didn't take care of all the lag, so we unfortunately
                                //need to just skip to the next frame.
                            }
                        }
                    }
                }

                //Since accumulated lag could have increased or decreased, we should check this every frame.
                //Note: it might be more efficient to check only where it changes.
                //Or perhaps use a change listener to avoid processing this so frequently.
                //TODO it appears that it is always updated once per frame.
                if (accumulatedLag > lagThreshold) {
                    //We are lagging too far behind...
                    //This happens because generally, the update function takes too long.
                    //It mays that the user's PC cannot handle the current graphics settings.
                    //They can turn the settings down to fix this.
                    //Note: turning down the FPS will not make the accumulate lag decrease.
                    //Instead, it makes the rate of increase for the lag become slower.

                    //There is nothing we can do to fix this error. We should just set a flag
                    //so that the owner of the thread can let the user know what to do.
                    lagging = true;
                } else {
                    lagging = false;
                }
            }

            long frameDurationNanos = System.nanoTime() - previousTime;
            if (frameDurationNanos != 0) {
                //TODO This should never be zero, so we should remove it later after proving it.
                currentFPS = 1 / (frameDurationNanos * SECONDS_PER_NANO);


                averagingBuffer[sampleIndex] = currentFPS;
                sampleIndex = (sampleIndex + 1) % averagingBuffer.length;
            }
        }
    }

    //TODO If the implement cares about lag, they can choose how to handle it.
    public abstract void onThreadUpdate();
}
