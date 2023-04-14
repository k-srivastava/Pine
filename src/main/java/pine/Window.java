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
    private long windowHandle;

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

        Callbacks.glfwFreeCallbacks(this.windowHandle);
        GLFW.glfwDestroyWindow(this.windowHandle);

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

        this.windowHandle = GLFW.glfwCreateWindow(
                this.width, this.height, this.title, MemoryUtil.NULL, MemoryUtil.NULL
        );

        if (this.windowHandle == MemoryUtil.NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        GLFW.glfwMakeContextCurrent(this.windowHandle);
        GLFW.glfwSwapInterval(1);

        GLFW.glfwShowWindow(this.windowHandle);
        GL.createCapabilities();
    }

    public void mainLoop() {
        while (!GLFW.glfwWindowShouldClose(this.windowHandle)) {
            GLFW.glfwPollEvents();

            GL11.glClearColor(1F, 0F, 0F, 1F);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            GLFW.glfwSwapBuffers(this.windowHandle);
        }
    }
}
