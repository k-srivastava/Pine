package pine;

/**
 * Abstract scene class for all in-game scenes.
 */
public abstract class Scene {
    protected Camera camera;

    /**
     * Create a new scene.
     */
    public Scene() { }

    /**
     * Initialize the scene.
     */
    public void initialize() { }

    /**
     * Update the scene; called every frame.
     *
     * @param deltaTime Time between current and previous frame.
     */
    public abstract void update(double deltaTime);
}
