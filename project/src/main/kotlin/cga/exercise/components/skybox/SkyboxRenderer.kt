package cga.exercise.components.skybox

import cga.exercise.components.Loader
import cga.exercise.components.camera.TronCamera
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

    private val skyboxShader: ShaderProgram
    private val cube = Cube()
    private val dayTexID: Int = Loader.loadCubeMap(dayTextures)
    private val nightTexID: Int = Loader.loadCubeMap(nightTextures)


    init {
        cube.scale(Vector3f(50f))
        skyboxShader =
            ShaderProgram("project/assets/shaders/skybox_vert.glsl", "project/assets/shaders/skybox_frag.glsl")
    }

    fun update(dt: Float) {
        if (rotation)
            cube.rotate(0f, 0.05f * dt, 0f)
    }

    fun render(ingameTime: Float, camera: TronCamera) {

        GL11.glDepthMask(false)
        GL11.glCullFace(GL11.GL_FRONT)
        skyboxShader.use()
        camera.bind(skyboxShader)
        skyboxShader.setUniform("ingameTime", ingameTime)
        skyboxShader.setUniform("ambientTag", myMap.ambientLightTagsueber)
        skyboxShader.setUniform("ambientNacht", myMap.ambientLightNachts)
        skyboxShader.setUniform("sonnenaufgangUhrzeit", myMap.myClock.sonnenaufgangUhrzeit)
        skyboxShader.setUniform("sonnenuntergangUhrzeit", myMap.myClock.sonnenuntergangUhrzeit)
        skyboxShader.setUniform("fadeDauerIngameStunden", myMap.myClock.fadeDauerIngameStunden)

        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, dayTexID)
        skyboxShader.setUniform("cubeMapDay", 0)

        GL13.glActiveTexture(GL13.GL_TEXTURE1)
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, nightTexID)
        skyboxShader.setUniform("cubeMapNight", 1)
        cube.render(skyboxShader)
        GL11.glDepthMask(true)
        GL11.glCullFace(GL11.GL_BACK)
    }
}