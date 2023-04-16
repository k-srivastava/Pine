package pine.renderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * 2D Texture class.
 */
public class Texture {
    private final int ID;
    private final String filePath;

    /**
     * Create a new texture with default settings and upload to OpenGL.
     *
     * @param filePath Location of the texture file.
     */
    public Texture(String filePath) {
        this.filePath = filePath;

        this.ID = GL11.glGenTextures();
        this.bind();

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = STBImage.stbi_load(this.filePath, width, height, channels, 0);

        if (image != null) {
            int format = -1;
            int numChannels = channels.get(0);

            switch (numChannels) {
                case 3 -> format = GL11.GL_RGB;
                case 4 -> format = GL11.GL_RGBA;
                default -> {
                    assert false : "Error (Texture.java): Unknown number of channels '" + numChannels + "'.";
                }
            }

            GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D, 0, format, width.get(0), height.get(0), 0, format,
                GL11.GL_UNSIGNED_BYTE, image
            );
        }

        else {
            assert false : "Error (Texture.java): Could not load image + '" + this.filePath + "'.";
        }

        STBImage.stbi_image_free(image);
    }

    /**
     * Bind the texture to the texture target.
     */
    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.ID);
    }

    /**
     * Unbind the texture.
     */
    public void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
}
