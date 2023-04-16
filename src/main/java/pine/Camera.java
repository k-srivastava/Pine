package pine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Core 2D camera class.
 */
public class Camera {
    private final Vector2f position;
    private final Matrix4f projectionMatrix, viewMatrix;

    /**
     * Create a new camera with specified position, a zero view matrix and a standard orthographic projection matrix.
     *
     * @param position Position of the camera.
     */
    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();

        this.modifyProjection();
    }

    /**
     * Modify the projection matrix to a standard orthographic projection.
     */
    public void modifyProjection() {
        this.projectionMatrix.identity();
        this.projectionMatrix.ortho(0F, 32F * 40F, 0F, 32F * 21F, 0F, 100F);
    }

    /**
     * @return Projection matrix of the camera.
     */
    public Matrix4f projectionMatrix() { return projectionMatrix; }

    /**
     * Recalculate the view matrix by making the camera look at its position.
     *
     * @return View matrix of the camera.
     */
    public Matrix4f viewMatrix() {
        Vector3f front = new Vector3f(0F, 0F, -1F);
        Vector3f up = new Vector3f(0F, 1F, 0F);

        this.viewMatrix.identity();
        this.viewMatrix.lookAt(
            new Vector3f(this.position.x, this.position.y, 20F),
            front.add(this.position.x, this.position.y, 0F),
            up
        );

        return this.viewMatrix;
    }
}
