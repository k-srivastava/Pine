package pine;

/**
 * Main level scene - currently a white background.
 */
public class LevelScene extends Scene {
    /**
     * Create the new scene and set the window background to be white.
     */
    public LevelScene() {
        Window window = Window.get();

        System.out.println("Inside the level scene.");

        window.r = 1F;
        window.g = 1F;
        window.b = 1F;
    }

    @Override
    public void update(double deltaTime) { }
}
