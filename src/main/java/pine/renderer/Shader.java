package pine.renderer;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import pine.utils.ShaderType;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * GLSL shader representation class.
 */
public class Shader {
    private final int shaderProgramID;
    private final String filePath;
    private String vertexSource;
    private String fragmentSource;

    /**
     * Create a new shader by parsing the shader file to split it into the vertex and fragment shaders and create the
     * OpenGL shader program.
     *
     * @param filePath Location of the shader file.
     */
    public Shader(String filePath) {
        this.filePath = filePath;

        try {
            String source = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String shaderType_1 = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String shaderType_2 = source.substring(index, eol).trim();

            final String shaderSource_1 = splitString[1].trim();
            final String shaderSource_2 = splitString[2].trim();

            switch (shaderType_1) {
                case "vertex" -> this.vertexSource = shaderSource_1;
                case "fragment" -> this.fragmentSource = shaderSource_1;
                default -> throw new IOException("Unexpected token '" + shaderType_1 + "'.");
            }

            switch (shaderType_2) {
                case "vertex" -> this.vertexSource = shaderSource_2;
                case "fragment" -> this.fragmentSource = shaderSource_2;
                default -> throw new IOException("Unexpected token '" + shaderType_2 + "'.");
            }
        }

        catch (IOException e) {
            e.printStackTrace();
            assert false : "Error could not open shader file: '" + this.filePath + "'.";
        }

        this.shaderProgramID = GL20.glCreateProgram();
    }

    /**
     * Compile the vertex and fragment shaders.
     *
     * @param link Whether to link the shaders to the shader program.
     */
    public void compileAll(boolean link) {
        int vertexID = this.compile(ShaderType.Vertex);
        int fragmentID = this.compile(ShaderType.Fragment);

        if (link) { this.link(vertexID, fragmentID); }
    }

    /**
     * Use the current shader program.
     */
    public void use() { GL20.glUseProgram(this.shaderProgramID); }

    /**
     * Stop using the current shader program.
     */
    public void detach() { GL20.glUseProgram(0); }

    /**
     * Upload a 4x4 matrix to the shader.
     *
     * @param variableName Name of thr matrix variable in the shader.
     * @param matrix       Matrix to be uploaded.
     */
    public void uploadMatrix(String variableName, Matrix4f matrix) {
        int variableLocation = GL20.glGetUniformLocation(this.shaderProgramID, variableName);

        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
        matrix.get(matrixBuffer);

        GL20.glUniformMatrix4fv(variableLocation, false, matrixBuffer);
    }

    /**
     * Compile a specific shader.
     *
     * @param shaderType Type of shader to be compiled.
     * @return Shader ID of the compiled shader.
     */
    private int compile(@NotNull ShaderType shaderType) {
        int shaderID = -1;
        String shader = "";

        switch (shaderType) {
            case Fragment -> {
                shaderID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
                GL20.glShaderSource(shaderID, this.fragmentSource);
                shader = "Fragment shader";
            }

            case Vertex -> {
                shaderID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
                GL20.glShaderSource(shaderID, this.vertexSource);
                shader = "Vertex shader";
            }
        }

        GL20.glCompileShader(shaderID);

        int success = GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS);
        if (success == GL11.GL_FALSE) {
            int logLength = GL20.glGetShaderi(shaderID, GL20.GL_INFO_LOG_LENGTH);

            System.err.printf("'%s': %s compilation failed.\n", this.filePath, shader);
            System.err.println(GL20.glGetShaderInfoLog(shaderID, logLength));

            assert false : "";
        }

        return shaderID;
    }

    /**
     * Link the vertex and fragment shaders to the shader program.
     *
     * @param vertexID   ID of the compiled vertex shader.
     * @param fragmentID ID of the compiled fragment shader.
     */
    private void link(int vertexID, int fragmentID) {
        GL20.glAttachShader(this.shaderProgramID, vertexID);
        GL20.glAttachShader(this.shaderProgramID, fragmentID);
        GL20.glLinkProgram(this.shaderProgramID);

        int success = GL20.glGetProgrami(this.shaderProgramID, GL20.GL_LINK_STATUS);
        if (success == GL11.GL_FALSE) {
            int logLength = GL20.glGetProgrami(this.shaderProgramID, GL20.GL_INFO_LOG_LENGTH);

            System.err.printf("'%s': Linking of shaders failed.\n", this.filePath);
            System.err.println(GL20.glGetProgramInfoLog(this.shaderProgramID, logLength));

            assert false : "";
        }
    }
}
