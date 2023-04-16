package pine;

import org.lwjgl.glfw.GLFW;

/**
 * Custom key listener singleton to interop with the GLFW key listener.
 */
public class KeyListener {
    private static KeyListener instance;
    private final boolean[] keyPressed = new boolean[350];

    /**
     * Create a new key listener.
     */
    private KeyListener() { }

    /**
     * @return Key listener singleton instance.
     */
    public static KeyListener get() {
        if (KeyListener.instance == null) { KeyListener.instance = new KeyListener(); }
        return KeyListener.instance;
    }

    /**
     * Set up a key callback to interop with GLFW key callback.
     *
     * @param windowPointer Pointer to the current window.
     * @param key           Key to check for press.
     * @param scanCode
     * @param action        Whether the key is pressed or released.
     * @param modifiers     Other modifier keys pressed.
     */
    public static void keyCallback(long windowPointer, int key, int scanCode, int action, int modifiers) {
        KeyListener instance = KeyListener.get();

        if (action == GLFW.GLFW_PRESS) { instance.keyPressed[key] = true; }
        else if (action == GLFW.GLFW_RELEASE) { instance.keyPressed[key] = false; }
    }

    /**
     * Check if a key is pressed.
     *
     * @param key Key to check for press.
     * @return Whether the key is pressed.
     */
    public static boolean keyPressed(int key) {
        KeyListener instance = KeyListener.get();
        return key < instance.keyPressed.length && instance.keyPressed[key];
    }
}
