package cga.exercise.components.map

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.skybox.SkyboxRenderer
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.game.Scene

class MyMap(
    val numChunksSquare: Int,
    val abstand: Float,
    val scene: Scene,
    sonnenaufgangUhrzeit: Float,
    sonnenuntergangUhrzeit: Float,
    fadeDauerIngameStunden: Float,
    ingameStundenDauerInSekunden: Int,
    val ambient: Float,
    val maxSunIntensity: Float

) {
    private val proceduralGround: ProceduralGroundInfinite
    private var skyboxStationary: SkyboxRenderer? = null
    private var skyboxRotating: SkyboxRenderer? = null
    var groundShader: ShaderProgram

    val myClock: MyClock


    init {
        groundShader = ShaderProgram("project/assets/shaders/tron_vert.glsl", "project/assets/shaders/ground_frag.glsl")
        proceduralGround = ProceduralGroundInfinite(scene.camera, numChunksSquare, abstand)

        myClock = MyClock(
            sonnenaufgangUhrzeit, sonnenuntergangUhrzeit,
            fadeDauerIngameStunden, ingameStundenDauerInSekunden
        )


//        skyboxStationary = SkyboxRenderer(
//            listOf(
//                "project/assets/textures/skybox/day-stationary/right.png",
//                "project/assets/textures/skybox/day-stationary/left.png",
//                "project/assets/textures/skybox/day-stationary/top.png",
//                "project/assets/textures/skybox/day-stationary/bottom.png",
//                "project/assets/textures/skybox/day-stationary/front.png",
//                "project/assets/textures/skybox/day-stationary/back.png"
//            ), listOf(
//                "project/assets/textures/skybox/night-stationary/right.png",
//                "project/assets/textures/skybox/night-stationary/left.png",
//                "project/assets/textures/skybox/night-stationary/top.png",
//                "project/assets/textures/skybox/night-stationary/bottom.png",
//                "project/assets/textures/skybox/night-stationary/front.png",
//                "project/assets/textures/skybox/night-stationary/back.png"
//            ), this, false
//        )

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
            ), this, true
        )
    }

    fun getHeight(x: Float, z: Float): Float {
        return proceduralGround.getHeight(x, z) //TOD
    }

    fun update(dt: Float, time: Float) {

        proceduralGround.update(dt, time)

        myClock.update(time)
        skyboxStationary?.update(dt)
        skyboxRotating?.update(dt)
    }

    fun render(dt: Float, time: Float) {
        groundShader.use()
        setNeededUniforms(groundShader)
        proceduralGround.render(groundShader)
        scene.renderLights(dt, time, groundShader)
    }

    fun setNeededUniforms(shaderProgram: ShaderProgram) {
        scene.setNeededUniforms(shaderProgram)
    }

    fun renderWithoutGroundShader(shaderProgram: ShaderProgram) {
        shaderProgram.use()
        setNeededUniforms(shaderProgram)
        proceduralGround.render(shaderProgram)
        proceduralGround.renderEntites(shaderProgram)
    }

    fun renderEntities(shaderProgram: ShaderProgram){
        shaderProgram.use()
        proceduralGround.renderEntites(shaderProgram)
    }





    fun renderSkybox() {
        skyboxRotating?.render(myClock.ingameTime, scene.camera)
        skyboxStationary?.render(myClock.ingameTime, scene.camera)
    }

    fun cleanUp() {
        proceduralGround.cleanUp()
        skyboxRotating?.cleanUp()
        skyboxStationary?.cleanUp()
    }


}