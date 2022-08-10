package cga.exercise.game

import cga.exercise.audio.AudioMaster
import cga.exercise.components.map.TerrainGenerator
import cga.exercise.components.camera.TronCamera
import cga.exercise.components.entities.*
import cga.exercise.components.geometry.*
import cga.exercise.components.gui.GuiRenderer
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.map.MyClock
import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.shadows.ShadowRenderer
import cga.framework.GLError
import cga.framework.GameWindow
import org.joml.Math
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.*


/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(val window: GameWindow) {

    private var prevX: Double = window.windowWidth / 2.0
    private var prevY: Double = window.windowHeight / 2.0
    private var scrollValue: Double = 0.0

    private val staticShader: ShaderProgram
    val camera: TronCamera

    val guiRenderer: GuiRenderer
    val myMap: MyMap
    val entityManager: EntityManager
    var audioMaster = AudioMaster()
    val shadowRenderer: ShadowRenderer
//    val grassInstance: GrassInstance

    private val lights = arrayListOf<PointLight>()
    lateinit var spotlight: SpotLight


    //scene setup
    init {
        staticShader = ShaderProgram("project/assets/shaders/tron_vert.glsl", "project/assets/shaders/tron_frag.glsl")

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLError.checkThrow()

        val backfaceCullingEnable = false
        if (backfaceCullingEnable) {
            GL11.glEnable(GL11.GL_CULL_FACE); GLError.checkThrow()
            GL11.glFrontFace(GL11.GL_CCW); GLError.checkThrow()
            GL11.glCullFace(GL11.GL_BACK); GLError.checkThrow()
        } else {
            GL11.glDisable(GL11.GL_CULL_FACE); GLError.checkThrow()
            GL11.glFrontFace(GL11.GL_CCW); GLError.checkThrow()
            GL11.glCullFace(GL11.GL_BACK); GLError.checkThrow()
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST); GLError.checkThrow()
        GL11.glDepthFunc(GL11.GL_LESS); GLError.checkThrow()

        camera = TronCamera()
        camera.translate(Vector3f(-1.0f, 1.3f, 1.9f))


        myMap = MyMap(
            5, 1f, this, 6f, 18f, 2f, 2, 0.1f, 0.9f
        )

        shadowRenderer = ShadowRenderer(this)

        entityManager = EntityManager(camera, this)

        initilizeLights()


        var audioSource = audioMaster.createAudioSource("project/assets/sounds/test.ogg")
        audioSource.play()

        guiRenderer = GuiRenderer(this, window)

//        grassInstance = GrassInstance(1, myMap)


    }

    fun initilizeLights() {
        lights.add(PointLight(Vector3f(0f, 0.75f, 0f), Vector3f(0.3f)))
        lights[0].parent = entityManager.getPlayer()
//        lights.add(PointLight(Vector3f(-5f, 2f, -5f), Vector3f(1f, 1f, 1f)))
//        lights.add(PointLight(Vector3f(+5f, 2f, -5f), Vector3f(1f, 1f, 1f)))


        spotlight = SpotLight(
            Vector3f(), Vector3f(1f, 1f, 1f), Math.toRadians(10f), Math.toRadians(45f)
        )
        spotlight.translate(Vector3f(-0f, 1f, -1.5f))
        spotlight.rotate(Math.toRadians(-20f), 0f, 0f)
        spotlight.parent = entityManager.getPlayer()
    }

    fun rainbowColor(time: Float): Vector3f {
        return when {
            time % (6 * Math.PI) < 1.0 * Math.PI -> Vector3f(Math.abs(Math.sin(time)), 0f, 0f) //rot
            time % (6 * Math.PI) < 2.0 * Math.PI -> Vector3f(
                Math.abs(Math.sin(time)), Math.abs(Math.sin(time) / 2), 0f
            ) //orange
            time % (6 * Math.PI) < 3.0 * Math.PI -> Vector3f(
                Math.abs(Math.sin(time)), Math.abs(Math.sin(time)), 0f
            ) //gelb
            time % (6 * Math.PI) < 4.0 * Math.PI -> Vector3f(0f, Math.abs(Math.sin(time)), 0f) //gruen
            time % (6 * Math.PI) < 5.0 * Math.PI -> Vector3f(0f, 0f, Math.abs(Math.sin(time))) //blau
            time % (6 * Math.PI) < 6.0 * Math.PI -> Vector3f(
                Math.abs(Math.sin(time)), 0f, Math.abs(Math.sin(time))
            ) //lila
            else -> Vector3f(Math.abs(Math.sin(time)), Math.abs(Math.sin(time)), Math.abs(Math.sin(time))) //sonst weiss
        }
    }


    fun setNeededUniforms(shaderProgram: ShaderProgram) {
        shaderProgram.use()
        camera.bind(shaderProgram)
        shaderProgram.setUniform("celShadingLevels", 0)
        shaderProgram.setUniform("depthMap", 10)
        shaderProgram.setUniform("viewPos", camera.getPosition())
        shaderProgram.setUniform("sunPos", shadowRenderer.sun.getPosition())
        shaderProgram.setUniform("sunSpaceMatrix", shadowRenderer.sunSpaceMatrix, false)
        shaderProgram.setUniform("anzLichter", lights.size)
        spotlight.bind(shaderProgram, camera.getCalculateViewMatrix())

        //Map
        shaderProgram.setUniform("ingameTime", myMap.myClock.ingameTime)
        shaderProgram.setUniform("sonnenaufgangUhrzeit", myMap.myClock.sonnenaufgangUhrzeit)
        shaderProgram.setUniform("sonnenuntergangUhrzeit", myMap.myClock.sonnenuntergangUhrzeit)
        shaderProgram.setUniform("fadeDauerIngameStunden", myMap.myClock.fadeDauerIngameStunden)
        shaderProgram.setUniform("ambient", myMap.ambient)
        shaderProgram.setUniform("maxSunIntensity", myMap.maxSunIntensity)

        shaderProgram.setUniform("sandUpperBound", 0.10f * TerrainGenerator.terrainMaxHeight)
        shaderProgram.setUniform("dirtUpperBound", 0.30f * TerrainGenerator.terrainMaxHeight)
        shaderProgram.setUniform("grassUpperBound", 0.95f * TerrainGenerator.terrainMaxHeight)
    }

    fun render(dt: Float, time: Float) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        shadowRenderer.render(dt, time)

        staticShader.use()
        this.setNeededUniforms(staticShader)

//        grassInstance.render(camera)

        shadowRenderer.sun.render(staticShader)
        entityManager.render(dt, time, staticShader)
        renderLights(dt, time, staticShader)


        myMap.render(dt, time)
        myMap.renderSkybox()
        myMap.renderEntities(staticShader)



        guiRenderer.render()

    }

    fun renderLights(dt: Float, time: Float, shaderProgram: ShaderProgram) {
        lights.forEachIndexed { index, it ->
            run {
                if (index == 0) {
//                    it.color = rainbowColor(time).mul(0.3f)
                } else {
                    val formula = Math.sin(time) * 1.8f
                    if (formula > 0) {
                        it.color = Vector3f(0f, 1f, 0f)
                    } else {
                        it.color = Vector3f(1f, 0f, 0f)
                    }
                    it.translate(Vector3f(0f, 0f, -5 * dt).mul(formula))
                    it.translate(
                        Vector3f(
                            0f, (myMap.getHeight(it.getPosition().x, it.getPosition().z) + 1) - it.getPosition().y, 0f
                        )
                    )
                }
                it.bind(shaderProgram, index)
            }
        }

        spotlight.bind(shaderProgram, camera.getCalculateViewMatrix())
    }

    fun update(dt: Float, time: Float) {


//        println("playerpos: ${entityManager.getPlayer().getWorldPosition()}")
//        println(camera.getWorldPosition())

        myMap.update(dt, time)

        entityManager.update(window, dt, time)

        guiRenderer.update(dt, time)

        if (window.getKeyState(GLFW_KEY_I)) camera.translate(Vector3f(0f, 0f, -5 * dt))
        if (window.getKeyState(GLFW_KEY_K)) camera.translate(Vector3f(0f, 0f, 5 * dt))
        if (window.getKeyState(GLFW_KEY_J)) camera.rotate(0f, dt, 0f)
        if (window.getKeyState(GLFW_KEY_L)) camera.rotate(0f, -dt, 0f)

        if (window.getKeyState(GLFW_KEY_0)) Mesh.renderTriangles()
        if (window.getKeyState(GLFW_KEY_P)) Mesh.renderLines()
    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {
        if (key == GLFW_KEY_T && action == GLFW_PRESS) {
            MyClock.stopTime = !MyClock.stopTime
        }


        if (key == GLFW_KEY_E && action == GLFW_PRESS) {
            guiRenderer.wantedPoster.toggle()
            guiRenderer.wantedPosterButton.toggle()
        }




        if (key == GLFW_KEY_R && action == GLFW_PRESS) {
            guiRenderer.ammoAnzeige.reload()
        }


    }

    fun onMouseButton(button: Int, action: Int, mods: Int) {
        //action kann GL_PRESS oder GL_RELEASE sein
        //mods sind modifier keys wie shift und ctrl

        if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
            guiRenderer.ammoAnzeige.shoot()
            guiRenderer.wantedPoster.hit()
        }

        if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
            camera.fov = TronCamera.zoom_fov
            guiRenderer.sniperScope.enable()
            guiRenderer.crosshair.disable()
        } else if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_RELEASE) {
            camera.fov = TronCamera.default_fov
            guiRenderer.sniperScope.disable()
            guiRenderer.crosshair.enable()
        }

        println("mouse button press: $button $action $mods")
    }

    fun onMouseScroll(xoffset: Double, yoffset: Double) {
        scrollValue += yoffset
    }

    fun onMouseMove(xpos: Double, ypos: Double) {
        entityManager.getPlayer().onMouseMove(prevX - xpos, 0.0) //prevY - ypos

        prevX = xpos
        prevY = ypos
    }

    fun cleanup() {

        staticShader.cleanUp()

        myMap.cleanUp()
        entityManager.cleanUp()
        guiRenderer.cleanUp()
        shadowRenderer.cleanUp()
        audioMaster.cleanUp()
    }
}