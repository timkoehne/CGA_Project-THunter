package cga.exercise.components.geometry.skybox

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.*
import org.lwjgl.stb.STBImage

class Skybox() {

    //    private val textureFiles = listOf(
//        "project/assets/textures/skybox/right.png",
//        "project/assets/textures/skybox/left.png",
//        "project/assets/textures/skybox/top.png",
//        "project/assets/textures/skybox/bottom.png",
//        "project/assets/textures/skybox/front.png",
//        "project/assets/textures/skybox/back.png"
//    )
    private val textureFiles = listOf(
        "project/assets/textures/skybox/dirt.png",
        "project/assets/textures/skybox/dirt.png",
        "project/assets/textures/skybox/dirt.png",
        "project/assets/textures/skybox/dirt.png",
        "project/assets/textures/skybox/dirt.png",
        "project/assets/textures/skybox/dirt.png"
    )

    var texID: Int

    init {
        texID = GL11.glGenTextures()
        GL13.glActiveTexture(GL13.GL_TEXTURE5)
        GL11.glBindTexture(GL20.GL_TEXTURE_CUBE_MAP, texID)


        val x = BufferUtils.createIntBuffer(1)
        val y = BufferUtils.createIntBuffer(1)
        val readChannels = BufferUtils.createIntBuffer(1)
        //flip y coordinate to make OpenGL happy
        STBImage.stbi_set_flip_vertically_on_load(true)
        val imageData = STBImage.stbi_load("project/assets/textures/skybox/dirt.png", x, y, readChannels, 4)
            ?: throw Exception("Image file \"" + "project/assets/textures/skybox/dirt.png" + "\" couldn't be read:\n" + STBImage.stbi_failure_reason())

        try {
            val width = x.get()
            val height = y.get()
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData)
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData)
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData)
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData)
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData)
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData)



            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
            throw ex
        } finally {
            STBImage.stbi_image_free(imageData)
        }



    }


}