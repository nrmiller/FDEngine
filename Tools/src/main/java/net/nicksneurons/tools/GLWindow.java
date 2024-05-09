package net.nicksneurons.tools;

import net.nicksneurons.tools.input.KeyListener;
import net.nicksneurons.tools.input.MouseListener;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * TODO When running on the main thread, the closing callback is processed after poll-events.
 * This means that the window will be used for an additional frame. This will likely cause issues.
 * Hence, we need to figure out how to handle the close callback for when running on the main thread. Perhaps no callback
 * is necessary at all.
 * TODO This even occurs when showing a modeless window.
 *
 * TODO Implement close cancelling.
 *
 * TODO When resizing, I don't think update/draw are called.
 *
 * TODO It seems like there are some issues with choosing an OpenGL version.
 */
public class GLWindow implements AnimatorListener {

    private GLFWErrorCallback errorPrinter;

    private UpdateListener updateListener;
    private KeyListener keyListener;
    private MouseListener mouseListener;
    private final List<GLWindowListener> listeners = new ArrayList<>();

    private boolean windowShown = false;
    private boolean closed = false;
    private GLWindowMode mode;

    private Animator animator;
    private GLEventListener glEventListener;
    private Thread thread;
    private CountDownLatch initializedLatch = new CountDownLatch(1);
    private CountDownLatch destroyedLatch = new CountDownLatch(1);
    private long window;
    public long getHandle() {
        return window;
    }

    private String title;
    private int width;
    private int height;
    private String[] iconSetResources;

    public GLWindow() {
        this("Untitled", 400, 400);
    }

    public GLWindow(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;

        errorPrinter = GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
    }

    public GLWindow setGLProfile(GLProfile profile) {
        glfwWindowHint(GLFW_OPENGL_PROFILE, profile.getValue());
        return this;
    }

    public GLWindow setGLVersion(int major, int minor) {
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, major);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, minor);
        return this;
    }

    public GLWindow setGLClientAPI(GLClientAPI api) {
        glfwWindowHint(GLFW_CLIENT_API, api.getValue());
        return this;
    }

    public GLWindow setAnimator(Animator animator) {
        if (animator.isRunning()) {
            throw new RuntimeException("Cannot attach an animator that is already running.");
        }
        if (windowShown) {
            throw new RuntimeException("Cannot attach an animator to an already visible window.");
        }
        this.animator = animator;
        return this;
    }

    public Animator getAnimator() {
        return animator;
    }

    public GLWindow setGLEventListener(GLEventListener listener) {
        this.glEventListener = listener;
        return this;
    }

    public GLEventListener getGLEventListener() {
        return glEventListener;
    }

    public void show() {
        if (windowShown && mode == GLWindowMode.WINDOW_MODELESS) {
            //The window was already shown as a modeless window, which means
            //we should simply bring it to the foreground.
            activate();
        } else if (closed) {
            throw new IllegalStateException("The window has already been closed.");
        } else {
            windowShown = true;
            mode = GLWindowMode.WINDOW_MODELESS;

            //If we are showing a modeless window, and the caller has not provided an animator,
            //then just use the default one.
            if (animator == null) {
                animator = new DefaultAnimator();
            }

            startAnimator();
        }
    }

    public void showDialog() {
        if (windowShown) {
            throw new IllegalStateException("The window can only be shown once.");
        }
        windowShown = true;
        mode = GLWindowMode.WINDOW_MODAL;

        run();
    }

    private void startAnimator() {
        //Begin updating the window.
        animator.setAnimatorListener(this);
        animator.start();

        //Block the current thread until the animator thread is initialized.
        //This prevents synchronization bugs with modeless windows.
        try {
            initializedLatch.await();
        } catch (InterruptedException e) {
            onClose();
        }
    }

    private boolean updating = false;
    private Long lastUpdatedTime = null, currentUpdatedTime = null;
    private boolean isFixedTimestep = false;


    //TODO Bug, this function is called regardless of modeless and modal.
    //We need to block in the case of modal.
    //There are also the conditions of whether or not the main thread should be used.
    //This appears to contradict, because a modeless window cannot run on the main thread.
    //
    // Here are the modes of operation:
    // 1. show (no animator)
    //    create and use the DefaultAnimator since modeless requires a separate thread.
    // 2. show (animator)
    //    create and use the animator provided.
    // 3. showDialog (no animator)
    //    create and block on the main thread.
    // 4. showDialog (animator)
    //    create and block on the animator.
    //
    private void run() {
        //If there is an animator, we use it.
        if (animator != null) {
            startAnimator();

            //Block the current thread until the animator thread is destroyed.
            //This is how a modal dialog is implemented.
            try {
                destroyedLatch.await();
            } catch (InterruptedException e) {
                onClose();
            }
        } else {
            //If there is no animator, we need to run on the main thread...
            onCreate();

            //Process message loop on the main thread.
            updating = true;
            while(updating) {
                while (!glfwWindowShouldClose(window)) {
                    //Just update from the calling thread.


                    currentUpdatedTime = System.nanoTime();
                    if (lastUpdatedTime == null) {
                        onUpdate(0);
                    }
                    else {
                        long deltaNs = currentUpdatedTime - lastUpdatedTime;

                        onUpdate((double)deltaNs / 1_000_000_000);
                    }
                    lastUpdatedTime = currentUpdatedTime;
                }

                //TODO WindowEvent for closing here... allow the user to cancel it.
                //postWindowEvent(this, GLWindowEvent.WINDOW_CLOSING);

                //TODO Replace this call when close cancelling is implemented.
                //updating = cancelled;

                if (closed) {
                    updating = false;
                }
            }

            //  onClose();
            glfwTerminate();
        }
    }

    protected void onClosing() {
        //TODO WindowEvent for closing here... allow the user to cancel it.
        postWindowEvent(this, GLWindowEvent.WINDOW_CLOSING);

        //Stop the animator so that the update loop will finish when the user clicks the 'x' button.
        if (animator != null) {
            animator.stop();
        } else {
            updating = false; // kill the update loop for when no animator is provided.
        }

        //TODO If we properly check for closing event, then we can call onClosing from the main thread pump (above).
        onClose();
    }

    protected void onClose() {
        //Event posted before so that the listener has a chance to free OpenGL resources.
        postOnSurfaceDestroyed();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        thread = null;
        closed = true;

        //Notify window listeners.
        postWindowEvent(this, GLWindowEvent.WINDOW_CLOSED);

        if (errorPrinter != null) {
            errorPrinter.free();
        }
    }

    /**
     * TODO Document
     * TODO This should be protected, this will require an abstract class.
     */
    @Override
    public void onCreate() {
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Disable high-resolution retina displays as this is causing scaling artifacts
        glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_FALSE);

        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create GLFW window.");
        if (iconSetResources != null)
            loadIconSet();

        //Set up a window close callback because we need it to stop the animator.
        glfwSetWindowCloseCallback(window, (long window) -> onClosing());
        glfwSetWindowSizeCallback(window, (long window, int width, int height) -> postOnSurfaceChanged(width, height));
        glfwSetMouseButtonCallback(window,(long window, int button, int action, int mods) -> {
            if (mouseListener == null)
                return;

            Vector2d cursorPosition = getCursorPosition();

            switch (action) {
                case GLFW_PRESS -> mouseListener.onMouseButtonDown(button, mods, cursorPosition.x, cursorPosition.y);
                case GLFW_RELEASE -> mouseListener.onMouseButtonUp(button, mods, cursorPosition.x, cursorPosition.y);
            }
        });
        glfwSetKeyCallback(window, (long window, int key, int scancode, int action, int mods) -> {
            if (keyListener == null)
                return;

            switch (action) {
                case GLFW_PRESS -> keyListener.onKeyDown(key, scancode, mods);
                case GLFW_RELEASE -> keyListener.onKeyUp(key, scancode, mods);
                case GLFW_REPEAT -> keyListener.onKeyRepeat(key, scancode, mods);
            }
        });

        // Center the window.
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (vidMode != null) {
                glfwSetWindowPos(
                        window,
                        (vidMode.width() - pWidth.get(0)) / 2,
                        (vidMode.height() - pHeight.get(0)) / 2);
            }
        } // the stack frame is popped automatically

        glfwMakeContextCurrent(window); //Make the OpenGL context current
        GL.createCapabilities(); // similar to glewInit()

        System.out.println("Made current. (version=" + GL11.glGetString(GL11.GL_VERSION) + ")");
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT); // Prevent flicker on macOS https://stackoverflow.com/a/27679342/975724

        glfwSwapInterval(1); // Enable v-sync (note: this throttles fps to refresh rate)

        glfwShowWindow(window); // Make the window visible

        postWindowEvent(this, GLWindowEvent.WINDOW_OPENED);
        postOnSurfaceCreated();

        initializedLatch.countDown();
    }

    public GLWindow setUpdateListener(UpdateListener listener) {
        this.updateListener = listener;
        return this;
    }
    public UpdateListener getUpdateListener() {
        return updateListener;
    }

    public GLWindow setKeyListener(KeyListener listener) {
        this.keyListener = listener;
        return this;
    }
    public KeyListener getKeyListener() {
        return keyListener;
    }

    public GLWindow setMouseListener(MouseListener listener) {
        this.mouseListener = listener;
        return this;
    }
    public MouseListener getMouseListener() {
        return mouseListener;
    }

    // Debugging vars
    private double accumulatedDeltas = 0.0;
    private int framesPerSecond = 0;
    public int getFramesPerSecond() {
        return framesPerSecond;
    }

    /**
     * TODO Document
     * TODO This should be protected, this will require an abstract class.
     */
    @Override
    public void onUpdate(double delta) {
        accumulatedDeltas += delta;
        framesPerSecond++;
        if (accumulatedDeltas > 1) {
            System.out.println(framesPerSecond + "fps");

            accumulatedDeltas -= 1;
            framesPerSecond = 0;
        }


        // Notes on input handling:
        // Polling for events first leads to an easy-to-understand design.
        // When user input occurs, something should happen. If we poll in the beginning of the frame,
        // then reacting to input happens in the same frame.
        // Regarding the decision between event-driven input and polling on the input handling end,
        // there shouldn't be any difference since input and updates happen on the same thread.
        //
        glfwPollEvents();
        if (glfwWindowShouldClose(window)) {
            // Do nothing as we are trying to close.
            // This must be checked after polling for events.
            // During close, we shouldn't update anymore as this can cause errors.
            return;
        }

        pollMouseMovement();

        if (isFixedTimestep) {
            /* todo Add support for a fixed timestep.
             * While it is good to call update on the same thread as rendering to retain consistency
             * we should iteratively call onUpdate() so that physics calculations are not throttled and
             * can be more accurate
             */
        }
        else {
            postOnUpdate(delta);
        }

        postOnDrawFrame();
        glfwSwapBuffers(window);
    }

    /**
     * TODO Document
     * TODO This should be protected, this will require an abstract class.
     */
    @Override
    public void onDestroy() {
        destroyedLatch.countDown();
    }

    public void activate() {
        glfwFocusWindow(window);
        /*glfwSetWindowFocusCallback(handle, (long window, boolean focused) ->
        {
            if (focused) {
                postWindowEvent(this, GLWindowEvent.WINDOW_ACTIVATED);
            } else {
                postWindowEvent(this, GLWindowEvent.WINDOW_DEACTIVATED);
            }
        });*/
    }

    public void close() {
        glfwSetWindowShouldClose(window, true);
    }

    public GLWindow addWindowListener(GLWindowListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
        return this;
    }

    public GLWindow removeWindowListener(GLWindowListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
        return this;
    }

    protected void postWindowEvent(GLWindow source, GLWindowEvent event) {
        for (GLWindowListener listener : listeners) {
            switch (event) {
                case WINDOW_OPENED:
                    listener.onWindowOpened(source);
                    break;
                case WINDOW_CLOSING:
                    listener.onWindowClosing(source);
                    break;
                case WINDOW_CLOSED:
                    listener.onWindowClosed(source);
                    break;
                default:
                    throw new RuntimeException("Unknown event: " + event.toString());

            }
        }
    }

    protected void postOnSurfaceCreated() {
        if (glEventListener != null) {
            glEventListener.onSurfaceCreated(width, height);
        }
    }

    protected void postOnSurfaceChanged(int width, int height) {
        this.width = width;
        this.height = height;
        if (glEventListener != null) {
            glEventListener.onSurfaceChanged(width, height);
        }

        // Because GLFW calls this function while polling events
        // we need to again track time and call update/draw
        if (animator == null) {
            currentUpdatedTime = System.nanoTime();
            if (lastUpdatedTime == null) {
                onUpdate(0);
            }
            else {
                long deltaNs = currentUpdatedTime - lastUpdatedTime;

                onUpdate((double)deltaNs / 1_000_000_000);
            }
            lastUpdatedTime = currentUpdatedTime;
        }
    }

    protected void postOnDrawFrame() {
        if (glEventListener != null) {
            glEventListener.onDrawFrame();
        }
    }

    protected void postOnSurfaceDestroyed() {
        if (glEventListener != null) {
            glEventListener.onSurfaceDestroyed();
        }
    }

    protected void postOnUpdate(double delta) {
        if (updateListener != null) {
            updateListener.onUpdate(delta);
        }
    }

    public GLWindow setWindowIconSet(String... iconSetResources) {
        this.iconSetResources = iconSetResources;
        return this;
    }

    /**
     * Icon must be loaded and set after the window is created.
     * Fluent API therefore may only cache the resource to load.
     */
    private void loadIconSet() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);
            try (GLFWImage.Buffer iconSet = GLFWImage.malloc(iconSetResources.length)) {

                ByteBuffer[] fileBuffers = new ByteBuffer[iconSetResources.length];
                ByteBuffer[] pixelBuffers = new ByteBuffer[iconSetResources.length];
                for (int index = 0; index < iconSetResources.length; index++)
                {
                    byte[] data = this.getClass().getResourceAsStream(iconSetResources[index]).readAllBytes();
                    fileBuffers[index] = (ByteBuffer) memAlloc(data.length).put(data).flip();

                    pixelBuffers[index] = stbi_load_from_memory(fileBuffers[index], w, h, comp, 4);

                    iconSet.position(index)
                            .width(w.get(0))
                            .height(h.get(0))
                            .pixels(pixelBuffers[index]);
                }

                glfwSetWindowIcon(window, iconSet);

                // Free all buffers after sending icon set to GLFW
                for (int index = 0; index < iconSetResources.length; index++) {
                    stbi_image_free(pixelBuffers[index]);
                    memFree(fileBuffers[index]);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Vector2d lastCursorPosition = null;
    private void pollMouseMovement() {
        if (glfwGetWindowAttrib(window, GLFW_HOVERED) == GLFW_FALSE)
            return;

        Vector2d currentCursorPosition = getCursorPosition();
        if (lastCursorPosition != null) {
            Vector2d delta = new Vector2d();
            currentCursorPosition.sub(lastCursorPosition, delta);

            if (mouseListener != null && delta.length() > 0) {
                mouseListener.onMouseMove(delta.x, delta.y);
            }
        }
        lastCursorPosition = currentCursorPosition;
    }

    public Vector2d getCursorPosition() {
        // Get the location of the cursor.
        try (MemoryStack stack = MemoryStack.stackPush()) {
            DoubleBuffer xBuf = stack.mallocDouble(1);
            DoubleBuffer yBuf = stack.mallocDouble(1);
            glfwGetCursorPos(window, xBuf, yBuf);

            return new Vector2d(xBuf.get(0), yBuf.get(0));
        }
    }
}
