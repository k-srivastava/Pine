package pine;

import org.lwjgl.glfw.GLFW;

public class KeyListener {
    private static KeyListener instance;
    private final boolean[] keyPressed = new boolean[350];

    private KeyListener() { }

    public static KeyListener get() {
        if (KeyListener.instance == null) { KeyListener.instance = new KeyListener(); }
        return KeyListener.instance;
    }

    public static void keyCallback(long windowPointer, int key, int scanCode, int action, int modifiers) {
        KeyListener instance = KeyListener.get();

        if (action == GLFW.GLFW_PRESS) { instance.keyPressed[key] = true; }
        else if (action == GLFW.GLFW_RELEASE) { instance.keyPressed[key] = false; }
    }

    public static boolean keyPressed(int key) {
        KeyListener instance = KeyListener.get();
        return key < instance.keyPressed.length && instance.keyPressed[key];
    }
}
