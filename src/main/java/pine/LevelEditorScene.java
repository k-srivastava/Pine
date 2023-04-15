package pine;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import pine.utils.ShaderType;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class LevelEditorScene extends Scene {
    private final String vertexShaderSource = """
        #version 330 core
        
        layout (location = 0) in vec3 attributePosition;
        layout (location = 1) in vec4 attributeColor;
        
        out vec4 fragmentColor;
        
        void main(void) {
            fragmentColor = attributeColor;
            gl_Position = vec4(attributePosition, 1.0);
        }
        """;
    private final String fragmentShaderSource = """
        #version 330 core
        
        in vec4 fragmentColor;
        
        out vec4 color;
        
        void main(void) {
            color = fragmentColor;
        }
        """;
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

    public LevelEditorScene() { }

    @Override
    public void initialize() {
        this.compileShader(ShaderType.Vertex);
        this.compileShader(ShaderType.Fragment);

        this.shaderProgramID = GL20.glCreateProgram();
        this.linkShaders();

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
        GL20.glUseProgram(this.shaderProgramID);

        GL30.glBindVertexArray(this.vaoID);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL11.glDrawElements(GL11.GL_TRIANGLES, this.elementArray.length, GL11.GL_UNSIGNED_INT, 0);

        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        GL20.glUseProgram(0);
    }

    private void compileShader(@NotNull ShaderType shaderType) {
        int shaderID = -1;
        String shader = "";

        switch (shaderType) {
            case Fragment -> {
                this.fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
                GL20.glShaderSource(this.fragmentID, this.fragmentShaderSource);

                shaderID = this.fragmentID;
                shader = "Fragment shader";
            }

            case Vertex -> {
                this.vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
                GL20.glShaderSource(this.vertexID, this.vertexShaderSource);

                shaderID = this.vertexID;
                shader = "Vertex shader";
            }
        }

        GL20.glCompileShader(shaderID);

        int success = GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS);
        if (success == GL11.GL_FALSE) {
            int logLength = GL20.glGetShaderi(shaderID, GL20.GL_INFO_LOG_LENGTH);

            System.err.printf("'defaultShader.glsl': %s compilation failed.\n", shader);
            System.err.println(GL20.glGetShaderInfoLog(shaderID, logLength));

            assert false : "";
        }
    }

    private void linkShaders() {
        GL20.glAttachShader(this.shaderProgramID, this.vertexID);
        GL20.glAttachShader(this.shaderProgramID, this.fragmentID);
        GL20.glLinkProgram(this.shaderProgramID);

        int success = GL20.glGetProgrami(this.shaderProgramID, GL20.GL_LINK_STATUS);
        if (success == GL11.GL_FALSE) {
            int logLength = GL20.glGetProgrami(this.shaderProgramID, GL20.GL_INFO_LOG_LENGTH);

            System.err.println("'defaultShader.glsl': Linking of shaders failed.\n");
            System.err.println(GL20.glGetProgramInfoLog(this.shaderProgramID, logLength));

            assert false : "";
        }
    }
}
