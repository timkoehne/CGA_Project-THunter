package cga.exercise.components.skybox

import cga.exercise.components.Util
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
    private val cube = Cube(myMap)
    private val dayTexID: Int = Util.loadCubeMap(dayTextures)
    private val nightTexID: Int = Util.loadCubeMap(nightTextures)


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

        myMap.setNeededUniforms(skyboxShader)

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

    fun cleanUp(){
        cube.cleanUp()
        skyboxShader.cleanUp()

    }

}