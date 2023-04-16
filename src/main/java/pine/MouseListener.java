package pine;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;

/**
 * Custom mouse listener singleton to interop with the GLFW mouse listener.
 */
public class MouseListener {
    public static MouseListener instance;
    private final Vector2d position;
    private final boolean[] mouseButtonPressed = new boolean[3];
    private Vector2d lastPosition;
    private Vector2d scroll;
    private boolean isDragging;

    /**
     * Create a new mouse listener.
     */
    private MouseListener() {
        this.scroll = new Vector2d(0D, 0D);
        this.position = new Vector2d(0D, 0D);
        this.lastPosition = new Vector2d(0D, 0D);
    }

    /**
     * @return Mouse listener singleton instance.
     */
    public static MouseListener get() {
        if (MouseListener.instance == null) { MouseListener.instance = new MouseListener(); }
        return MouseListener.instance;
    }

    /**
     * Set up a mouse position callback to interop with GLFW mouse position callback.
     *
     * @param windowPointer Pointer to the current window.
     * @param xPosition     x-coordinate of the mouse position.
     * @param yPosition     y-coordinate of the mouse position.
     */
    public static void mousePositionCallback(long windowPointer, double xPosition, double yPosition) {
        MouseListener instance = MouseListener.get();

        instance.lastPosition = instance.position;
        instance.position.x = xPosition;
        instance.position.y = yPosition;

        instance.isDragging = instance.mouseButtonPressed[0] || instance.mouseButtonPressed[1] || instance.mouseButtonPressed[2];
    }

    /**
     * Set up a mouse button press callback to interop with GLFW mouse button callback.
     *
     * @param windowPointer Pointer to current window.
     * @param button        Mouse button to check for press.
     * @param action        Whether the mouse button is pressed or released.
     * @param modifiers     Other modifier keys pressed.
     */
    public static void mouseButtonCallback(long windowPointer, int button, int action, int modifiers) {
        MouseListener instance = MouseListener.get();

        if (action == GLFW.GLFW_PRESS) {
            if (button < instance.mouseButtonPressed.length) { instance.mouseButtonPressed[button] = true; }
        }

        else if (action == GLFW.GLFW_RELEASE) {
            instance.mouseButtonPressed[button] = false;
            instance.isDragging = false;
        }
    }

    /**
     * Set up mouse scroll to interop with GLFW mouse scroll callback.
     *
     * @param windowPointer Pointer to the current window.
     * @param xOffset       x-coordinate of the scroll offset.
     * @param yOffset       y-coordinate of the scroll offset.
     */
    public static void mouseScrollCallback(long windowPointer, double xOffset, double yOffset) {
        MouseListener instance = MouseListener.get();
        instance.scroll.x = xOffset;
        instance.scroll.y = yOffset;
    }

    /**
     * Change the position of the mouse its last position and stop it scrolling. Useful to call on the last frame of the
     * window.
     */
    public static void endFrame() {
        MouseListener instance = MouseListener.get();

        instance.scroll = new Vector2d(0D, 0D);
        instance.lastPosition = instance.position;
    }

    /**
     * @return Position of the mouse.
     */
    public Vector2d position() { return this.position; }

    /**
     * @return Scroll direction of the mouse.
     */
    public Vector2d scroll() { return this.scroll; }

    /**
     * @return Displacement of the mouse from the last frame to the current frame.
     */
    public Vector2d displacement() {
        Vector2d displacement = new Vector2d(0D, 0D);
        this.lastPosition.sub(this.position, displacement);

        return displacement;
    }

    /**
     * @return Whether the mouse is being dragged.
     */
    public boolean isDragging() { return this.isDragging; }

    /**
     * Check if a mouse button is pressed.
     *
     * @param button Mouse button to check for press.
     * @return Whether the mouse button is pressed.
     */
    public boolean mouseButtonDown(int button) {
        MouseListener instance = MouseListener.get();
        return button < instance.mouseButtonPressed.length && instance.mouseButtonPressed[button];
    }
}
