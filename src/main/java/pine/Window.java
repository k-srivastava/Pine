package pine;

import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import pine.utils.GameScene;
import pine.utils.Time;

/**
 * Window singleton.
 */
public class Window {
    private static Window window = null;
    private static Scene currentScene = null;
    private final int width, height;
    private final String title;
    public float r, g, b, a;
    private long windowPointer;

    /**
     * Create a new window with a 1920x1080 resolution, white background and title of "Pine Window".
     */
    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Pine Window";

        r = 1F;
        g = 1F;
        b = 1F;
        a = 1F;
    }

    /**
     * @return Window singleton instance.
     */
    public static Window get() {
        if (Window.window == null) { Window.window = new Window(); }
        return Window.window;
    }

    /**
     * Set the current scene to the new scene and initialize it.
     *
     * @param newScene New scene to the made the current scene.
     */
    public static void changeScene(GameScene newScene) {
        switch (newScene) {
            case LevelEditorScene -> Window.currentScene = new LevelEditorScene();
            case LevelScene -> Window.currentScene = new LevelScene();
        }

        Window.currentScene.initialize();
    }

    /**
     * Run lifetime of the window.
     */
    public void run() {
        System.out.printf("Hello LWJGL %s!\n", Version.getVersion());

        initialize();
        mainLoop();

        Callbacks.glfwFreeCallbacks(this.windowPointer);
        GLFW.glfwDestroyWindow(this.windowPointer);

        GLFW.glfwTerminate();

        GLFWErrorCallback nullCallback = GLFW.glfwSetErrorCallback(null);
        if (nullCallback != null) { nullCallback.free(); }
    }

    /**
     * Initialize the window with default settings, setup callbacks and OpenGL.
     */
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

        Window.changeScene(GameScene.LevelEditorScene);
    }

    /**
     * Main loop of the window to update the window every tick.
     */
    public void mainLoop() {
        double frameStartTime = Time.time();
        double frameEndTime;
        double deltaTime = 0D;

        while (!GLFW.glfwWindowShouldClose(this.windowPointer)) {
            GLFW.glfwPollEvents();

            GL11.glClearColor(this.r, this.g, this.b, this.a);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);


            Window.currentScene.update(deltaTime);

            GLFW.glfwSwapBuffers(this.windowPointer);

            frameEndTime = Time.time();
            deltaTime = frameEndTime - frameStartTime;
            frameStartTime = frameEndTime;
        }
    }
}
