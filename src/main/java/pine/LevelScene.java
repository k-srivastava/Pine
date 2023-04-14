package pine;

public class LevelScene extends Scene {
    public LevelScene() {
        Window window = Window.get();

        System.out.println("Inside the level scene.");

        window.r = 1F;
        window.g = 1F;
        window.b = 1F;
    }

    @Override
    public void update(double deltaTime) {

    }
}
