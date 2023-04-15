package pine;

public abstract class Scene {
    protected Camera camera;

    public Scene() { }

    public void initialize() { }

    public abstract void update(double deltaTime);
}
