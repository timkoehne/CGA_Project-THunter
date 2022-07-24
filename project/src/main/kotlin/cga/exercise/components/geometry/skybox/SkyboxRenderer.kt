package cga.exercise.components.geometry.skybox

import cga.exercise.components.MyClock
import cga.exercise.components.Loader
import cga.exercise.components.geometry.Cube
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f
import org.lwjgl.opengl.*


class SkyboxRenderer(val dayTextures: List<String>, val nightTextures: List<String>, val myClock: MyClock) {

    private val cube = Cube()
    private val dayTexID: Int = Loader.loadCubeMap(dayTextures)
    private val nightTexID: Int = Loader.loadCubeMap(nightTextures)


    init {
        cube.scale(Vector3f(50f))
    }

    fun update(dt: Float) {
        cube.rotate(0f, 0.05f * dt, 0f)
    }

    fun render(shaderProgram: ShaderProgram, ingameTime: Float) {

        shaderProgram.setUniform("ingameTime", ingameTime)
        shaderProgram.setUniform("cubeMapDay", 0)
        shaderProgram.setUniform("cubeMapNight", 1)

        shaderProgram.setUniform("sonnenaufgangUhrzeit", myClock.sonnenaufgangUhrzeit)
        shaderProgram.setUniform("sonnenuntergangUhrzeit", myClock.sonnenuntergangUhrzeit)
        shaderProgram.setUniform("fadeDauerIngameStunden", myClock.fadeDauerIngameStunden)

        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, dayTexID)
        GL13.glActiveTexture(GL13.GL_TEXTURE1)
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, nightTexID)
        cube.render(shaderProgram)
    }
}