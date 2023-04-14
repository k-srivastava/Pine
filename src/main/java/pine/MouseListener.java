package pine;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;

public class MouseListener {
    public static MouseListener instance;
    private final Vector2d position;
    private final boolean[] mouseButtonPressed = new boolean[3];
    private Vector2d lastPosition;
    private Vector2d scroll;
    private boolean isDragging;

    private MouseListener() {
        this.scroll = new Vector2d(0L, 0L);
        this.position = new Vector2d(0L, 0L);
        this.lastPosition = new Vector2d(0L, 0L);
    }

    public static MouseListener get() {
        if (MouseListener.instance == null) { MouseListener.instance = new MouseListener(); }
        return MouseListener.instance;
    }

    public static void mousePositionCallback(long windowPointer, double xPosition, double yPosition) {
        MouseListener instance = MouseListener.get();

        instance.lastPosition = instance.position;
        instance.position.x = xPosition;
        instance.position.y = yPosition;

        instance.isDragging = instance.mouseButtonPressed[0] || instance.mouseButtonPressed[1] || instance.mouseButtonPressed[2];
    }

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

    public static void mouseScrollCallback(long windowPointer, double xOffset, double yOffset) {
        MouseListener instance = MouseListener.get();
        instance.scroll.x = xOffset;
        instance.scroll.y = yOffset;
    }

    public static void endFrame() {
        MouseListener instance = MouseListener.get();

        instance.scroll = new Vector2d(0L, 0L);
        instance.lastPosition = instance.position;
    }

    public Vector2d position() {
        return position;
    }

    public Vector2d scroll() {
        return this.scroll;
    }

    public Vector2d displacement() {
        Vector2d displacement = new Vector2d(0L, 0L);
        this.lastPosition.sub(this.position, displacement);

        return displacement;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public boolean mouseButtonDown(int button) {
        MouseListener instance = MouseListener.get();
        return button < instance.mouseButtonPressed.length && instance.mouseButtonPressed[button];
    }
}
