package cga.exercise.components.geometry

import cga.exercise.components.MyClock
import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.skybox.SkyboxRenderer
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f
import org.lwjgl.opengl.GL11

class MyMap(
    anzX: Int,
    anzZ: Int,
    abstand: Float,
    val camera: TronCamera,
    sonnenaufgangUhrzeit: Float,
    sonnenuntergangUhrzeit: Float,
    fadeDauerIngameStunden: Float,
    ingameStundenDauerInSekunden: Int
) {

    var ground: Renderable
    val proceduralGround: ProceduralGround
    val skybox: SkyboxRenderer

    val myClock: MyClock

    private val skyboxShader: ShaderProgram

    init {
        proceduralGround = ProceduralGround.createGround(anzX, anzZ, abstand)
        ground = Renderable(mutableListOf(proceduralGround))

        myClock =
            MyClock(sonnenaufgangUhrzeit, sonnenuntergangUhrzeit, fadeDauerIngameStunden, ingameStundenDauerInSekunden)

        skyboxShader =
            ShaderProgram("project/assets/shaders/skybox_vert.glsl", "project/assets/shaders/skybox_frag.glsl")

        skybox = SkyboxRenderer(
            listOf(
                "project/assets/textures/skybox/right.png",
                "project/assets/textures/skybox/left.png",
                "project/assets/textures/skybox/top.png",
                "project/assets/textures/skybox/bottom.png",
                "project/assets/textures/skybox/front.png",
                "project/assets/textures/skybox/back.png"
            ), listOf(
                "project/assets/textures/skybox/night/right.png",
                "project/assets/textures/skybox/night/left.png",
                "project/assets/textures/skybox/night/top.png",
                "project/assets/textures/skybox/night/bottom.png",
                "project/assets/textures/skybox/night/front.png",
                "project/assets/textures/skybox/night/back.png"
            ), myClock
        )


    }

    fun getHeight(x: Float, z: Float): Float {
        return proceduralGround.getHeight(x, z)
    }

    fun update(dt: Float, time: Float) {
        myClock.update(time)


    }

    fun render(staticShader: ShaderProgram) {
        staticShader.use()
        ground.render(staticShader)

    }

    fun renderSkybox() {
        GL11.glDepthMask(false)
        skyboxShader.use()
        camera.bind(skyboxShader)
        skybox.render(skyboxShader, myClock.ingameTime)
        GL11.glDepthMask(true)
    }


}