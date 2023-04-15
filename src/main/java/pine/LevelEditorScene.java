package pine;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import pine.renderer.Shader;
import pine.utils.ShaderType;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class LevelEditorScene extends Scene {
    private final float[] vertexArray = {
        0.5F, -0.5F, 0.0F, 1F, 0F, 0F, 1F,  // Bottom-right.
        -0.5F, 0.5F, 0.0F, 0F, 1F, 0F, 1F,  // Top-left.
        0.5F, 0.5F, 0.0F, 0F, 0F, 1F, 1F,  // Top-right.
        -0.5F, -0.5F, 0.0F, 1F, 1F, 0F, 1F   // Bottom-left.
    };
    private final int[] elementArray = {
        2, 1, 0,  // Top-right triangle.
        0, 1, 3   // Bottom-left triangle.
    };
    private int vertexID, fragmentID, shaderProgramID;
    private int vaoID, vboID, eboID;
    private Shader defaultShader;

    public LevelEditorScene() { }

    @Override
    public void initialize() {
        this.defaultShader = new Shader("src/main/resources/shaders/default.glsl");
        this.defaultShader.compile(true);

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
        final int vertexSize = (positionSize + colorSize) * Float.BYTES;

        GL20.glVertexAttribPointer(0, positionSize, GL11.GL_FLOAT, false, vertexSize, 0);
        GL20.glEnableVertexAttribArray(0);

        GL20.glVertexAttribPointer(
            1, colorSize, GL11.GL_FLOAT, false, vertexSize, positionSize * Float.BYTES
        );
        GL20.glEnableVertexAttribArray(1);
    }

    @Override
    public void update(double deltaTime) {
        this.defaultShader.use();

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
