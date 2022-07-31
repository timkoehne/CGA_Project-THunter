package cga.exercise.components.map

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.skybox.SkyboxRenderer
import cga.exercise.components.shader.ShaderProgram

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

    private val ground: Renderable
    private val proceduralGround: ProceduralGround
    private var skyboxStationary: SkyboxRenderer? = null
    private var skyboxRotating: SkyboxRenderer? = null

    val myClock: MyClock


    init {
        proceduralGround = ProceduralGround.createGround(anzX, anzZ, abstand)
        ground = Renderable(mutableListOf(proceduralGround))

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
        return proceduralGround.getHeight(x, z)
    }

    fun update(dt: Float, time: Float) {
        myClock.update(time)
        skyboxStationary?.update(dt)
        skyboxRotating?.update(dt)
    }

    fun render(shaderProgram: ShaderProgram) {
        shaderProgram.use()
        shaderProgram.setUniform("ingameTime", myClock.ingameTime)
        shaderProgram.setUniform("sonnenaufgangUhrzeit", myClock.sonnenaufgangUhrzeit)
        shaderProgram.setUniform("sonnenuntergangUhrzeit", myClock.sonnenuntergangUhrzeit)
        shaderProgram.setUniform("fadeDauerIngameStunden", myClock.fadeDauerIngameStunden)
        shaderProgram.setUniform("ambientTag", ambientLightTagsueber)
        shaderProgram.setUniform("ambientNacht", ambientLightNachts)

        ground.render(shaderProgram)
    }

    fun renderSkybox() {
        skyboxRotating?.render(myClock.ingameTime, camera)
        skyboxStationary?.render(myClock.ingameTime, camera)
    }

    fun cleanUp(){
        ground.cleanUp()
        skyboxRotating?.cleanUp()
        skyboxStationary?.cleanUp()
    }


}