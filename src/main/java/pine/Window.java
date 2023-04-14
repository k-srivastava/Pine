package pine;

import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Window {
    private static Window window = null;
    private final int width, height;
    private final String title;
    private long windowPointer;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Pine Window";
    }

    public static Window get() {
        if (Window.window == null) { Window.window = new Window(); }
        return Window.window;
    }

    public void run() {
        System.out.printf("Hello LWJGL %s!", Version.getVersion());

        initialize();
        mainLoop();

        Callbacks.glfwFreeCallbacks(this.windowPointer);
        GLFW.glfwDestroyWindow(this.windowPointer);

        GLFW.glfwTerminate();

        GLFWErrorCallback nullCallback = GLFW.glfwSetErrorCallback(null);
        if (nullCallback != null) { nullCallback.free(); }
    }

    public void initialize() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) { throw new IllegalStateException("Unable to initialize GLFW."); }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);

        this.windowPointer = GLFW.glfwCreateWindow(
            this.width, this.height, this.title, MemoryUtil.NULL, MemoryUtil.NULL
        );

        if (this.windowPointer == MemoryUtil.NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        GLFW.glfwSetCursorPosCallback(this.windowPointer, MouseListener::mousePositionCallback);
        GLFW.glfwSetMouseButtonCallback(this.windowPointer, MouseListener::mouseButtonCallback);
        GLFW.glfwSetScrollCallback(this.windowPointer, MouseListener::mouseScrollCallback);
        GLFW.glfwSetKeyCallback(this.windowPointer, KeyListener::keyCallback);

        GLFW.glfwMakeContextCurrent(this.windowPointer);
        GLFW.glfwSwapInterval(1);

        GLFW.glfwShowWindow(this.windowPointer);
        GL.createCapabilities();
    }

    public void mainLoop() {
        while (!GLFW.glfwWindowShouldClose(this.windowPointer)) {
            GLFW.glfwPollEvents();

            GL11.glClearColor(1F, 0F, 0F, 1F);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            if (KeyListener.keyPressed(GLFW.GLFW_KEY_SPACE)) { System.out.println("Space key is pressed."); }

            GLFW.glfwSwapBuffers(this.windowPointer);
        }
    }
}
