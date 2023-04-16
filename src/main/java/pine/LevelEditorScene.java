package pine;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import pine.renderer.Shader;
import pine.renderer.Texture;
import pine.utils.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Level editor scene - currently a test scene.
 */
public class LevelEditorScene extends Scene {
    private final float[] vertexArray = {
        100F,   0F, 0F,     1F, 0F, 0F, 1F,     1F, 0F,  // Bottom-right.
          0F, 100F, 0F,     0F, 1F, 0F, 1F,     0F, 1F,  // Top-left.
        100F, 100F, 0F,     1F, 0F, 1F, 1F,     1F, 1F,  // Top-right.
          0F,   0F, 0F,     1F, 1F, 0F, 1F,     0F, 0F   // Bottom-left.
    };
    private final int[] elementArray = {
        2, 1, 0,  // Top-right triangle.
        0, 1, 3   // Bottom-left triangle.
    };
    private int vertexID, fragmentID, shaderProgramID;
    private int vaoID, vboID, eboID;
    private Shader defaultShader;
    private Texture testTexture;

    /**
     * Create the new scene and initialize the camera to (0, 0).
     */
    public LevelEditorScene() {
        this.camera = new Camera(new Vector2f());
    }

    /**
     * Load the shaders and bind the data to the VAO, VBO and EBOs.
     */
    @Override
    public void initialize() {
        this.defaultShader = new Shader("src/main/resources/shaders/default.glsl");
        this.defaultShader.compileAll(true);

        this.testTexture = new Texture("src/main/resources/images/testImage.jpg");

        this.vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(this.vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(this.vertexArray.length);
        vertexBuffer.put(this.vertexArray).flip();

        this.vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(this.elementArray.length);
        elementBuffer.put(this.elementArray).flip();

        this.eboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.eboID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL15.GL_STATIC_DRAW);

        final int positionSize = 3;
        final int colorSize = 4;
        final int uvSize = 2;

        final int vertexSize = (positionSize + colorSize + uvSize) * Float.BYTES;

        GL20.glVertexAttribPointer(0, positionSize, GL11.GL_FLOAT, false, vertexSize, 0);
        GL20.glEnableVertexAttribArray(0);

        GL20.glVertexAttribPointer(
            1, colorSize, GL11.GL_FLOAT, false, vertexSize, positionSize * Float.BYTES
        );
        GL20.glEnableVertexAttribArray(1);

        GL20.glVertexAttribPointer(
            2, uvSize, GL11.GL_FLOAT, false, vertexSize, (positionSize + colorSize) * Float.BYTES
        );
        GL20.glEnableVertexAttribArray(2);
    }

    /**
     * Use ths shaders, draw the elements and then detach the shaders every frame.
     *
     * @param deltaTime Time between current and previous frame.
     */
    @Override
    public void update(double deltaTime) {
        this.defaultShader.use();

        this.defaultShader.uploadTexture("TEXTURE_SAMPLER", 0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        this.testTexture.bind();

        this.defaultShader.uploadMatrix("uniformProjection", this.camera.projectionMatrix());
        this.defaultShader.uploadMatrix("uniformView", this.camera.viewMatrix());
        this.defaultShader.uploadFloat("uniformTime", (float) Time.time());

        GL30.glBindVertexArray(this.vaoID);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL11.glDrawElements(GL11.GL_TRIANGLES, this.elementArray.length, GL11.GL_UNSIGNED_INT, 0);

        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        this.defaultShader.detach();
    }
}
