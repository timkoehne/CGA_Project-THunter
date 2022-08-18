package cga.exercise.game

import cga.exercise.audio.AudioMaster
import cga.exercise.collision.AABB
import cga.exercise.components.camera.OrbitCamera
import cga.exercise.components.map.TerrainGenerator
import cga.exercise.components.camera.TronCamera
import cga.exercise.components.entities.*
import cga.exercise.components.entities.traits.ReloadingAnimationTrait
import cga.exercise.components.geometry.*
import cga.exercise.components.gui.GuiRenderer
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.shadows.ShadowRenderer
import cga.framework.GLError
import cga.framework.GameWindow
import org.joml.AxisAngle4f
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.*


/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(val window: GameWindow) {


    companion object {
        var audioMaster = AudioMaster()
        var gunShotSound = audioMaster.createAudioSource("project/assets/sounds/gun_shot.ogg")

        init {
            gunShotSound.setVolume(0.1f)
        }

    }

    private var prevX: Double = window.windowWidth / 2.0
    private var prevY: Double = window.windowHeight / 2.0
    private var scrollValue: Double = 0.0


    private val staticShader: ShaderProgram =
        ShaderProgram("project/assets/shaders/tron_vert.glsl", "project/assets/shaders/tron_frag.glsl")

    var camera: TronCamera

    private val flyThroughCamera: TronCamera
    private val orbitCamera: OrbitCamera

    private val guiRenderer: GuiRenderer
    val myMap: MyMap
    val entityManager: EntityManager
    val shadowRenderer: ShadowRenderer
//    val grassInstance: GrassInstance

    var celShadingLevel = 0

    private val lights = arrayListOf<PointLight>()
    lateinit var spotlight: SpotLight

    //scene setup
    init {

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLError.checkThrow()

        val backfaceCullingEnable = true
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

        flyThroughCamera = TronCamera()
        orbitCamera = OrbitCamera()
        camera = flyThroughCamera

        flyThroughCamera.translate(Vector3f(0f, 1.6f, 0f))


        myMap = MyMap(
            5, 1f, this, 6f, 18f, 2f, 2, 0.3f, 0.9f
        )


        entityManager = EntityManager(camera, this)
        orbitCamera.parent = entityManager.drone

        spotlight = SpotLight(
            Vector3f(), Vector3f(1f, 1f, 1f), Math.toRadians(10f), Math.toRadians(45f)
        )
        spotlight.translate(Vector3f(-0f, 1f, -0.75f))
        spotlight.rotate(Math.toRadians(-35f), 0f, 0f)
        spotlight.parent = entityManager.drone


        shadowRenderer = ShadowRenderer(this)

        initilizeLights()

        guiRenderer = GuiRenderer(this, window)

        entityManager.character.reloadingAnimationTrait.ammoAnzeige = guiRenderer.ammoAnzeige

//        grassInstance = GrassInstance(1, myMap)


    }

    private fun initilizeLights() {
        lights.add(PointLight(Vector3f(0f, -0.75f, 0f), Vector3f(0.3f)))
        lights[0].parent = entityManager.player
//        lights.add(PointLight(Vector3f(-5f, 2f, -5f), Vector3f(1f, 1f, 1f)))
//        lights.add(PointLight(Vector3f(+5f, 2f, -5f), Vector3f(1f, 1f, 1f)))
//        spotlight.parent = entityManager.player
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
        shaderProgram.setUniform("celShadingLevels", celShadingLevel)
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
        shaderProgram.setUniform("grassUpperBound", 1.5f * TerrainGenerator.terrainMaxHeight)
    }

    fun render(dt: Float, time: Float) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        shadowRenderer.render(dt, time)

        staticShader.use()
        this.setNeededUniforms(staticShader)

//        grassInstance.render(camera)

//        shadowRenderer.sun.render(staticShader)
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
        myMap.update(dt, time)
        entityManager.update(window, dt, time)
        guiRenderer.update(dt, time)


    }

    private fun switchCamera() {
        camera = if (camera == flyThroughCamera) orbitCamera else flyThroughCamera
    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {

        if (key == GLFW_KEY_X && action == GLFW_PRESS) {
            guiRenderer.controlDisplay.toggle()
        }

        if (key == GLFW_KEY_C && action == GLFW_PRESS) {
            entityManager.switchPlayer()
            switchCamera()
        }

        if (key == GLFW_KEY_E && action == GLFW_PRESS) {
            guiRenderer.wantedPoster.toggle()
            guiRenderer.wantedPosterButton.toggle()
        }

        if (key == GLFW_KEY_R && action == GLFW_PRESS) {
            if (entityManager.player == entityManager.character) {
                (entityManager.player as Character).reload()
            }
        }

        if (key == GLFW_KEY_N && action == GLFW_PRESS) {
            entityManager.drone.toggle()
        }

        if (key == GLFW_KEY_0 && action == GLFW_PRESS) {
            Mesh.toggleWireframe()
        }

        if (key == GLFW_KEY_P && action == GLFW_PRESS) {
            AABB.toggleHitbox()
        }

        if (key == GLFW_KEY_UP && action == GLFW_PRESS) {
            println("Celshading Level at $celShadingLevel")
            celShadingLevel++
        }

        if (key == GLFW_KEY_DOWN && action == GLFW_PRESS) {
            if (celShadingLevel > 0) {
                println("Celshading Level at $celShadingLevel")
                celShadingLevel--
            } else {
                println("Celshading is off")
            }
        }


    }

    fun onMouseButton(button: Int, action: Int, mods: Int) {
        //action kann GL_PRESS oder GL_RELEASE sein
        //mods sind modifier keys wie shift und ctrl

        if (entityManager.player == entityManager.character) {
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {

                if (guiRenderer.ammoAnzeige.ammoVorhanden() &&
                    entityManager.character.reloadingAnimationTrait.state == ReloadingAnimationTrait.State.Idle
                ) {

                    gunShotSound.play()
                    val bullet = Bullet(myMap)
                    EntityManager.bullets.add(bullet)

                    bullet.setModelMatrix(camera.getWorldModelMatrix())
                    bullet.translate(Vector3f(0f, 0f, -1f))

                    guiRenderer.ammoAnzeige.shoot()
                }
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
        }

        println("mouse button press: $button $action $mods")
    }

    fun onMouseScroll(xoffset: Double, yoffset: Double) {
        if (camera is OrbitCamera) {
            orbitCamera.updateRadius(yoffset * 0.1f)
        }
//        scrollValue += yoffset
    }

    fun onMouseMove(xpos: Double, ypos: Double) {
        if (camera is OrbitCamera) {
            (camera as OrbitCamera).updateTheta((prevY - ypos) * 0.1f)
            (camera as OrbitCamera).updatePhi((prevX - xpos) * 0.1f)
        } else {
            entityManager.character.onMouseMove(prevX - xpos, prevY - ypos)
        }

        camera.updatePhi((prevX - xpos) * 0.1f)
        camera.updateTheta((prevY - ypos) * 0.1f)

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