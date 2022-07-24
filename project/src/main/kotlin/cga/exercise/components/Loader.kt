package cga.exercise.components

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL13
import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer


data class TextureData(var imagedata: ByteBuffer, var width: Int, var height: Int)

class Loader {

    companion object {
        fun decodeTextureFile(filepath: String): TextureData {
            val x = BufferUtils.createIntBuffer(1)
            val y = BufferUtils.createIntBuffer(1)
            val readChannels = BufferUtils.createIntBuffer(1)
            //flip y coordinate to make OpenGL happy
//            STBImage.stbi_set_flip_vertically_on_load(true)
            val imageData = STBImage.stbi_load(filepath, x, y, readChannels, 4)
                ?: throw Exception("Image file \"" + filepath + "\" couldn't be read:\n" + STBImage.stbi_failure_reason())
            try {
                return TextureData(imageData, x.get(), y.get())
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
                throw ex
            } finally {
            }
        }

        fun loadCubeMap(textureFiles: List<String>): Int {
            val texID = GL11.glGenTextures()
            GL13.glActiveTexture(GL13.GL_TEXTURE0)
            GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID)

            for (i in textureFiles.indices) {
                val data = decodeTextureFile(textureFiles[i])
                GL11.glTexImage2D(
                    GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0,
                    GL11.GL_RGBA,
                    data.width,
                    data.height,
                    0,
                    GL11.GL_RGBA,
                    GL11.GL_UNSIGNED_BYTE,
                    data.imagedata
                )
            }
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL13.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE)

            return texID
        }


    }


}