package net.nicksneurons.tools.errors;

/**
 * TODO before implementing this class, we need to check to see how the JVM currently handles runtime exceptions
 * when there isn't any console to output the stacktrace to.
 *
 * Ultimately, the goal is to provide a means to handle runtime exception and display crash information.
 * If the end-user is using the application, he/she will not be able to see the stacktrace. Instead, he/she is presented
 * with an option to send the "crash details" to the developer.
 *
 * @deprecated Until we know what to do.
 */
@Deprecated
public class ErrorHandler {

    /**
     * TODO Implement this function to display the stack track in a dialog along with the exit code and message.
     * TODO We should also look into display crash updates.
     * @Deprecated Until we kow what to do (see class comments above).
     * @param details
     */
    public static void forcedExit(ExitDetails details) {

        //TODO Implement...
        //Maybe use swing or JavaFX?


        System.exit(details.getExitCode());
    }
}
