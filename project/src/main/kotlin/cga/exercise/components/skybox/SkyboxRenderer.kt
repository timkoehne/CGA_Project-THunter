package cga.exercise.components.skybox

import cga.exercise.components.Loader
import cga.exercise.components.entities.Cube
import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f
import org.lwjgl.opengl.*


class SkyboxRenderer(
    private val dayTextures: List<String>,
    private val nightTextures: List<String>,
    private val myMap: MyMap,
    private val rotation: Boolean
) {

    private val cube = Cube()
    private val dayTexID: Int = Loader.loadCubeMap(dayTextures)
    private val nightTexID: Int = Loader.loadCubeMap(nightTextures)


    init {
        cube.scale(Vector3f(50f))
    }

    fun update(dt: Float) {
        if (rotation)
            cube.rotate(0f, 0.05f * dt, 0f)
    }

    fun render(shaderProgram: ShaderProgram, ingameTime: Float) {

        GL11.glDepthMask(false)
        shaderProgram.setUniform("ingameTime", ingameTime)
        shaderProgram.setUniform("ambientTag", myMap.ambientLightTagsueber)
        shaderProgram.setUniform("ambientNacht", myMap.ambientLightNachts)
        shaderProgram.setUniform("sonnenaufgangUhrzeit", myMap.myClock.sonnenaufgangUhrzeit)
        shaderProgram.setUniform("sonnenuntergangUhrzeit", myMap.myClock.sonnenuntergangUhrzeit)
        shaderProgram.setUniform("fadeDauerIngameStunden", myMap.myClock.fadeDauerIngameStunden)

        shaderProgram.setUniform("cubeMapDay", 0)
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, dayTexID)

        shaderProgram.setUniform("cubeMapNight", 1)
        GL13.glActiveTexture(GL13.GL_TEXTURE1)
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, nightTexID)
        cube.render(shaderProgram)
        GL11.glDepthMask(true)


    }
}