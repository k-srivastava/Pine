package pine;

import pine.utils.GameScene;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {
    private boolean changingScene = false;
    private double timeToChangeScene = 2L;

    public LevelEditorScene() { System.out.println("Inside the level editor scene."); }

    @Override
    public void update(double deltaTime) {
        if (!this.changingScene && KeyListener.keyPressed(KeyEvent.VK_SPACE)) {
            this.changingScene = true;
        }

        if (this.changingScene && this.timeToChangeScene > 0L) {
            Window window = Window.get();
            double colorDelta = deltaTime * 5F;

            this.timeToChangeScene -= deltaTime;
            window.r -= colorDelta;
            window.g -= colorDelta;
            window.b -= colorDelta;
        }

        else if (this.changingScene) {
            Window.changeScene(GameScene.LevelScene);
        }
    }
}
