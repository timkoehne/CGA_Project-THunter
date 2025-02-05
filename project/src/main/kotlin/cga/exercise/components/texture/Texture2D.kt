package cga.exercise.components.texture

import cga.exercise.components.Util
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL46.GL_MAX_TEXTURE_MAX_ANISOTROPY
import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer


/**
 * Created by Fabian on 16.09.2017.
 */
class Texture2D(imageData: ByteBuffer, width: Int, height: Int, genMipMaps: Boolean) : ITexture {
    var texID: Int = -1
        private set

    init {
        try {
            processTexture(imageData, width, height, genMipMaps)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }

    companion object {

        private val geladeneTexturen = HashMap<String, Texture2D>()

        //create texture from file
        //don't support compressed textures for now
        //instead stick to pngs
        operator fun invoke(path: String, genMipMaps: Boolean): Texture2D {

            if (geladeneTexturen.containsKey(path)) {
//                println("Textur wurde aus dem Cache geladen: $path")
                return geladeneTexturen[path]!!
            }

            var t = Util.decodeTextureFile(path, true)
            val texture = Texture2D(t.imagedata, t.width, t.height, genMipMaps)
            geladeneTexturen[path] = texture
            STBImage.stbi_image_free(t.imagedata)
            return texture

        }
    }

    override fun processTexture(imageData: ByteBuffer, width: Int, height: Int, genMipMaps: Boolean) {
        texID = GL11.glGenTextures()
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL20.GL_TEXTURE_2D, texID)
        GL12.glTexImage2D(
            GL20.GL_TEXTURE_2D,
            0,
            GL11.GL_RGBA,
            width,
            height,
            0,
            GL11.GL_RGBA,
            GL11.GL_UNSIGNED_BYTE,
            imageData
        )
        GL30.glGenerateMipmap(GL_TEXTURE_2D)
        unbind()
    }

    override fun setTexParams(wrapS: Int, wrapT: Int, minFilter: Int, magFilter: Int) {
        GL11.glBindTexture(GL20.GL_TEXTURE_2D, texID)
        GL11.glTexParameteri(
            GL20.GL_TEXTURE_2D,
            GL20.GL_TEXTURE_MIN_FILTER,
            minFilter
        ) // z.b. GL11.GL_NEAREST oder GL11.GL_LINEAR
        GL11.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MAG_FILTER, magFilter)
        GL11.glTexParameteri(
            GL20.GL_TEXTURE_2D,
            GL20.GL_TEXTURE_WRAP_S,
            wrapS
        ) //z.b. GL20.GL_CLAMP_TO_EDGE oder GL11.GL_REPEAT
        GL11.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, wrapT)
        GL11.glTexParameterf(
            GL11.GL_TEXTURE_2D,
            EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT,
            16.0f
        ) //kein unterschied?


        println("max anisotropic $GL_MAX_TEXTURE_MAX_ANISOTROPY")

        unbind()
    }

    override fun bind(textureUnit: Int) {

        if (textureUnit < 100) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit)
        } else {
            GL13.glActiveTexture(textureUnit)
        }
        GL11.glBindTexture(GL20.GL_TEXTURE_2D, texID)
    }

    override fun unbind() {
        GL11.glBindTexture(GL20.GL_TEXTURE_2D, 0)
    }

    override fun cleanup() {
        unbind()
        if (texID != 0) {
            GL11.glDeleteTextures(texID)
            texID = 0
        }
    }
}