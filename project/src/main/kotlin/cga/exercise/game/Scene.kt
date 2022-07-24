package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.*
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import cga.framework.OBJLoader
import org.joml.Math
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*


/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {

    private var prevX: Double = window.windowWidth / 2.0
    private var prevY: Double = window.windowHeight / 2.0

    private val staticShader: ShaderProgram
    val camera: TronCamera
    var bike: Renderable?

    val myMap: MyMap

    val sniper: Renderable
    val tree: Renderable

    val bear: ComplexModel


    val lights = arrayListOf<PointLight>()
    var spotlight: SpotLight


    //scene setup
    init {
        staticShader = ShaderProgram("project/assets/shaders/tron_vert.glsl", "project/assets/shaders/tron_frag.glsl")

        //initial opengl state
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()

        val backfaceCullingEnable = false
        if (backfaceCullingEnable) {
            glEnable(GL_CULL_FACE); GLError.checkThrow()
            glFrontFace(GL_CCW); GLError.checkThrow()
            glCullFace(GL_BACK); GLError.checkThrow()
        } else {
            glDisable(GL_CULL_FACE); GLError.checkThrow()
            glFrontFace(GL_CCW); GLError.checkThrow()
            glCullFace(GL_BACK); GLError.checkThrow()
        }

        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()

        camera = TronCamera()
        camera.translate(Vector3f(-1.0f, 1.3f, 1.9f))

        myMap = MyMap(500, 500, 1f, camera, 6f, 18f,
            2f, 2, 0.8f, 0.5f)

        bike = ModelLoader.loadModel(
            ("project/assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj"), Math.toRadians(-90f), Math.toRadians(90f), 0f
        )
        bike?.scale(Vector3f(0.8f))


        tree = OakTree3()
        //tree.color = Vector3f(1f, 1f, 1f)
        tree.translate(Vector3f(25f, myMap.getHeight(25f, 25f) - 0.1f, 25f))

        sniper = Sniper()
        sniper.translate(Vector3f(5f, myMap.getHeight(5f, 5f) + 1, 5f))
        sniper.scale(Vector3f(0.5f))
        camera.parent = sniper


        bear = ComplexModel("project/assets/models/brownbear.obj")
        bear.scale(Vector3f(0.05f))

        lights.add(PointLight(Vector3f(0f, 0.75f, 0f), Vector3f(1f, 1f, 1f)))
        lights[0].parent = bike
        lights.add(PointLight(Vector3f(-5f, 1f, -5f), Vector3f(1f, 1f, 1f)))
        lights.add(PointLight(Vector3f(+5f, 1f, -5f), Vector3f(1f, 1f, 1f)))


        spotlight = SpotLight(
            Vector3f(), Vector3f(1f, 1f, 1f), Math.toRadians(10f), Math.toRadians(45f)
        )
        spotlight.translate(Vector3f(0f, 1f, -1.5f))
        spotlight.rotate(Math.toRadians(-20f), 0f, 0f)
        spotlight.parent = bike


        bike?.rotate(Math.toRadians(0f), Math.toRadians(225f), Math.toRadians(0f))


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

    fun render(dt: Float, time: Float) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        staticShader.use()


        staticShader.setUniform("ingameTime", myMap.myClock.ingameTime)

        staticShader.setUniform("sonnenaufgangUhrzeit", myMap.myClock.sonnenaufgangUhrzeit)
        staticShader.setUniform("sonnenuntergangUhrzeit", myMap.myClock.sonnenuntergangUhrzeit)
        staticShader.setUniform("fadeDauerIngameStunden", myMap.myClock.fadeDauerIngameStunden)

        staticShader.setUniform("ambientTag", myMap.ambientLightTagsueber)
        staticShader.setUniform("ambientNacht", myMap.ambientLightNachts)

        staticShader.setUniform("anzLichter", lights.size)
        camera.bind(staticShader)



        bear.render(staticShader)

        lights.forEachIndexed { index, it ->
            run {
                if (index == 0) {
                    it.color = rainbowColor(time).mul(0.3f)
                } else {
                    val formula = Math.sin(time) * 1.8f
                    if (formula > 0) {
                        it.color = Vector3f(0f, 1f, 0f)
                    } else {
                        it.color = Vector3f(1f, 0f, 0f)
                    }
                    it.translate(Vector3f(0f, 0f, -5 * dt).mul(formula))
                }
                it.bind(staticShader, index)
            }
        }

        spotlight.bind(staticShader, camera.getCalculateViewMatrix())

        myMap.render(staticShader)
        tree.render(staticShader)
        sniper.render(staticShader)
        bike?.color = rainbowColor(time)
        bike?.render(staticShader)

        myMap.renderSkybox()
    }

    fun updateBike(dt: Float, time: Float) {
//        if (window.getKeyState(GLFW_KEY_W))
//            bike?.translate(Vector3f(0f, 0f, -5 * dt))
//        if (window.getKeyState(GLFW_KEY_S))
//            bike?.translate(Vector3f(0f, 0f, 5 * dt))
//        if (window.getKeyState(GLFW_KEY_A))
//            bike?.rotate(0f, dt, 0f)
//        if (window.getKeyState(GLFW_KEY_D))
//            bike?.rotate(0f, -dt, 0f)
//
//
//        bike?.translate(
//            Vector3f(
//                0f, proceduralGround.getHeight(
//                    bike!!.getPosition().x,
//                    bike!!.getPosition().z
//                ) - bike!!.getPosition()!!.y, 0f
//            )
//        )
    }

    fun update(dt: Float, time: Float) {
        myMap.update(dt, time)

        if (window.getKeyState(GLFW_KEY_W)) sniper.translate(Vector3f(0f, 0f, -5 * dt))
        if (window.getKeyState(GLFW_KEY_S)) sniper.translate(Vector3f(0f, 0f, 5 * dt))
        if (window.getKeyState(GLFW_KEY_A)) sniper.translate(Vector3f(-5 * dt, 0f, 0f))
        if (window.getKeyState(GLFW_KEY_D)) sniper.translate(Vector3f(5 * dt, 0f, 0f))

        if (window.getKeyState(GLFW_KEY_I)) camera.translate(Vector3f(0f, 0f, -5 * dt))
        if (window.getKeyState(GLFW_KEY_K)) camera.translate(Vector3f(0f, 0f, 5 * dt))
        if (window.getKeyState(GLFW_KEY_J)) camera.rotate(0f, dt, 0f)
        if (window.getKeyState(GLFW_KEY_L)) camera.rotate(0f, -dt, 0f)


        sniper.translate(
            Vector3f(
                0f, myMap.getHeight(
                    sniper.getPosition().x, sniper.getPosition().z
                ) - sniper.getPosition()!!.y + 1, 0f
            )
        )
    }


    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {
        sniper.rotate(0f, (prevX - xpos).toFloat() * 0.002f, 0f)
        sniper.rotate((prevY - ypos).toFloat() * 0.002f, 0f, 0f)

        prevX = xpos
        prevY = ypos
    }

    fun cleanup() {}
}