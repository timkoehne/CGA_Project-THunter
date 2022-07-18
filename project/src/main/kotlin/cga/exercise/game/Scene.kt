package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.*
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.*
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

    var ground: Renderable

    val lights = arrayListOf<PointLight>()
    var spotlight: SpotLight


    //scene setup
    init {
        staticShader = ShaderProgram("project/assets/shaders/tron_vert.glsl", "project/assets/shaders/tron_frag.glsl")

        //initial opengl state
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()

        val backfaceCullingEnable = true
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


        bike = ModelLoader.loadModel(
            ("project/assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj"),
            Math.toRadians(-90f),
            Math.toRadians(90f),
            0f
        )
        bike?.scale(Vector3f(0.8f))

        val proceduralGround = ProceduralGround.createGround(10,10, 1f)
        ground = Renderable(mutableListOf(proceduralGround))
        //bike?.translate(Vector3f(0f, proceduralGround.getHeight(25, 25), 0f))
        ground.color = Vector3f(0f, 1f, 0f)

        lights.add(PointLight(Vector3f(0f, 0.75f, 0f), Vector3f(1f, 1f, 1f)))
        lights[0].parent = bike
        lights.add(PointLight(Vector3f(-5f, 1f, -5f), Vector3f(1f, 1f, 1f)))
        lights.add(PointLight(Vector3f(+5f, 1f, -5f), Vector3f(1f, 1f, 1f)))


        spotlight = SpotLight(
            Vector3f(),
            Vector3f(1f, 1f, 1f),
            Math.toRadians(10f),
            Math.toRadians(45f)
        )
        spotlight.preTranslate(Vector3f(0f, 1f, -1.5f))
        spotlight.rotate(Math.toRadians(-20f), 0f, 0f)
        spotlight.parent = bike



        camera = TronCamera()
        //camera.rotate(Math.toRadians(-35f), 0f, 0f)
        camera.translate(Vector3f(0f, 2f, 4.0f))
        camera.parent = bike
    }

    fun rainbowColor(t: Float): Vector3f {
        return when {
            t % (6 * Math.PI) < 1.0 * Math.PI -> Vector3f(Math.abs(Math.sin(t)), 0f, 0f) //rot
            t % (6 * Math.PI) < 2.0 * Math.PI -> Vector3f(Math.abs(Math.sin(t)), Math.abs(Math.sin(t) / 2), 0f) //orange
            t % (6 * Math.PI) < 3.0 * Math.PI -> Vector3f(Math.abs(Math.sin(t)), Math.abs(Math.sin(t)), 0f) //gelb
            t % (6 * Math.PI) < 4.0 * Math.PI -> Vector3f(0f, Math.abs(Math.sin(t)), 0f) //gruen
            t % (6 * Math.PI) < 5.0 * Math.PI -> Vector3f(0f, 0f, Math.abs(Math.sin(t))) //blau
            t % (6 * Math.PI) < 6.0 * Math.PI -> Vector3f(Math.abs(Math.sin(t)), 0f, Math.abs(Math.sin(t))) //lila
            else -> Vector3f(Math.abs(Math.sin(t)), Math.abs(Math.sin(t)), Math.abs(Math.sin(t))) //sonst weiss
        }
    }

    fun render(dt: Float, t: Float) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        staticShader.use()

        staticShader.setUniform("anzLichter", lights.size)
        camera.bind(staticShader)


        lights.forEachIndexed { index, it ->
            run {
                if (index == 0) {
                    it.color = rainbowColor(t).mul(0.3f)
                } else {
                    val formula = Math.sin(t) * 1.8f
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

        ground.render(staticShader)
        bike?.color = rainbowColor(t)
        bike?.render(staticShader)
    }

    fun update(dt: Float, t: Float) {
        if (window.getKeyState(GLFW_KEY_W))
            bike?.translate(Vector3f(0f, 0f, -5 * dt))
        if (window.getKeyState(GLFW_KEY_S))
            bike?.translate(Vector3f(0f, 0f, 5 * dt))
        if (window.getKeyState(GLFW_KEY_A))
            bike?.rotate(0f, dt, 0f)
        if (window.getKeyState(GLFW_KEY_D))
            bike?.rotate(0f, -dt, 0f)
    }


    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {

        bike?.let {
//            camera.rotateAroundPoint(0f, (prevX - xpos).toFloat() * 0.002f, 0f, it.getPosition())
            camera.rotateAroundPoint(0f, (prevX - xpos).toFloat() * 0.002f, 0f, camera.getPosition())
//            camera.rotateAroundPoint(
//                0f, (prevX - xpos).toFloat() * 0.002f, 0f,
//                Vector3f(it.getWorldPosition().x, camera.getWorldPosition().y, it.getWorldPosition().z)
//            )
        }


        prevX = xpos
        prevY = ypos

    }

    fun cleanup() {}
}