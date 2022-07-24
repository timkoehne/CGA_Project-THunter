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
    ingameStundenDauerInSekunden: Int,
    val ambientLightTagsueber: Float, //TODO random noise reinbringen
    val ambientLightNachts: Float

) {

    private var ground: Renderable
    private val proceduralGround: ProceduralGround
    private val skyboxStationary: SkyboxRenderer
    private val skyboxRotating: SkyboxRenderer

    val myClock: MyClock

    private val skyboxShader: ShaderProgram

    init {
        proceduralGround = ProceduralGround.createGround(anzX, anzZ, abstand)
        ground = Renderable(mutableListOf(proceduralGround))

        myClock =
            MyClock(sonnenaufgangUhrzeit, sonnenuntergangUhrzeit, fadeDauerIngameStunden, ingameStundenDauerInSekunden)

        skyboxShader =
            ShaderProgram("project/assets/shaders/skybox_vert.glsl", "project/assets/shaders/skybox_frag.glsl")

        skyboxStationary = SkyboxRenderer(
            listOf(
                "project/assets/textures/skybox/day-stationary/right.png",
                "project/assets/textures/skybox/day-stationary/left.png",
                "project/assets/textures/skybox/day-stationary/top.png",
                "project/assets/textures/skybox/day-stationary/bottom.png",
                "project/assets/textures/skybox/day-stationary/front.png",
                "project/assets/textures/skybox/day-stationary/back.png"
            ), listOf(
                "project/assets/textures/skybox/night-stationary/right.png",
                "project/assets/textures/skybox/night-stationary/left.png",
                "project/assets/textures/skybox/night-stationary/top.png",
                "project/assets/textures/skybox/night-stationary/bottom.png",
                "project/assets/textures/skybox/night-stationary/front.png",
                "project/assets/textures/skybox/night-stationary/back.png"
            ), myClock, false
        )

        skyboxRotating = SkyboxRenderer(
            listOf(
                "project/assets/textures/skybox/day-rotating/right.png",
                "project/assets/textures/skybox/day-rotating/left.png",
                "project/assets/textures/skybox/day-rotating/top.png",
                "project/assets/textures/skybox/day-rotating/bottom.png",
                "project/assets/textures/skybox/day-rotating/front.png",
                "project/assets/textures/skybox/day-rotating/back.png"
            ), listOf(
                "project/assets/textures/skybox/night-rotating/right.png",
                "project/assets/textures/skybox/night-rotating/left.png",
                "project/assets/textures/skybox/night-rotating/top.png",
                "project/assets/textures/skybox/night-rotating/bottom.png",
                "project/assets/textures/skybox/night-rotating/front.png",
                "project/assets/textures/skybox/night-rotating/back.png"
            ), myClock, true
        )


    }

    fun getHeight(x: Float, z: Float): Float {
        return proceduralGround.getHeight(x, z)
    }

    fun getAmbient() {

    }

    fun update(dt: Float, time: Float) {
        myClock.update(time)
        skyboxStationary.update(dt)
        skyboxRotating.update(dt)
    }

    fun render(staticShader: ShaderProgram) {
        staticShader.use()
        ground.render(staticShader)
    }

    fun renderSkybox() {
        GL11.glDepthMask(false)
        skyboxShader.use()
        camera.bind(skyboxShader)
        skyboxRotating.render(skyboxShader, myClock.ingameTime)
        skyboxStationary.render(skyboxShader, myClock.ingameTime)
        GL11.glDepthMask(true)
    }


}