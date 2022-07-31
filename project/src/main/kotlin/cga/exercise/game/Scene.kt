package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.*
import cga.exercise.components.entities.Cube
import cga.exercise.components.entities.Drone
import cga.exercise.components.entities.Sniper
import cga.exercise.components.gui.GuiElement
import cga.exercise.components.gui.GuiRenderer
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.shadows.ShadowRenderer
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.Math
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL11.*


/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(val window: GameWindow) {

    private var prevX: Double = window.windowWidth / 2.0
    private var prevY: Double = window.windowHeight / 2.0

    private val staticShader: ShaderProgram
    private val debugShader: ShaderProgram
    val camera: TronCamera
    var bike: Renderable? = null

    val guiRenderer = GuiRenderer(window)

    val myMap: MyMap

    var sniper: Renderable? = null
    lateinit var trees: MutableList<Renderable?>
    lateinit var animals: MutableList<Renderable?>
    lateinit var cabins: MutableList<Renderable?>

    lateinit var dekostuff: MutableList<Renderable?>


    lateinit var drone: Drone

    lateinit var cube: Cube

    val lights = arrayListOf<PointLight>()
    lateinit var spotlight: SpotLight

    val shadowRenderer: ShadowRenderer

    //scene setup
    init {
        staticShader = ShaderProgram("project/assets/shaders/tron_vert.glsl", "project/assets/shaders/tron_frag.glsl")
        debugShader = ShaderProgram("project/assets/shaders/debug_vert.glsl", "project/assets/shaders/debug_frag.glsl")

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLError.checkThrow()

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

        shadowRenderer = ShadowRenderer(this)

        myMap = MyMap(
            500, 500, 1f, camera, 6f, 18f, 2f, 2, 0.8f, 0.6f
        )

        initializeEntities()
        initilizeLights()


        guiRenderer.addElement(
            GuiElement(
                Texture2D.invoke("project/assets/textures/dirt.png", false), Vector2f(0.0f), Vector2f(0.5f)
            )
        )


    }

    fun initilizeLights() {
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
    }

    fun initializeEntities() {
        cube = Cube()
        cube.translate(shadowRenderer.sunPos.sub(cube.getPosition()))

        bike = ModelLoader.loadModel(
            ("project/assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj"), Math.toRadians(-90f), Math.toRadians(90f), 0f
        )
        bike?.scale(Vector3f(0.8f))
        bike?.rotate(Math.toRadians(0f), Math.toRadians(225f), Math.toRadians(0f))


        trees = mutableListOf(
            ModelLoader.loadModel("project/assets/trees/tree1.1.obj", 0f, 0f, 0f),
            ModelLoader.loadModel("project/assets/trees/tree1.1rot.obj", 0f, 0f, 0f),
            ModelLoader.loadModel("project/assets/trees/tree1.obj", 0f, 0f, 0f),
            ModelLoader.loadModel("project/assets/trees/tree1rot.obj", 0f, 0f, 0f),
            ModelLoader.loadModel("project/assets/trees/tree5.obj", 0f, 0f, 0f),
            ModelLoader.loadModel("project/assets/trees/tree6.obj", 0f, 0f, 0f),
            ModelLoader.loadModel("project/assets/trees/tree9.obj", 0f, 0f, 0f),
            ModelLoader.loadModel("project/assets/trees/tree10.obj", 0f, 0f, 0f),
            ModelLoader.loadModel("project/assets/trees/tree11.obj", 0f, 0f, 0f)
        )
        trees.forEachIndexed { index, renderable ->
            renderable?.translate(Vector3f(index * 5f, myMap.getHeight(index * 5f, 30f), 30f))
        }

        dekostuff = mutableListOf(
            ModelLoader.loadModel("project/assets/deko/bigStone.obj", 0f, 0f, 0f),
            ModelLoader.loadModel("project/assets/deko/brownMushroom.obj", 0f, 0f, 0f),
            ModelLoader.loadModel("project/assets/deko/etwasKleinererStein.obj", 0f, 0f, 0f),
            ModelLoader.loadModel("project/assets/deko/grassBueschel.obj", 0f, 0f, 0f),
            ModelLoader.loadModel("project/assets/deko/redMushroom.obj", 0f, 0f, 0f),
            ModelLoader.loadModel("project/assets/deko/stock.obj", 0f, 0f, 0f)
        )
        dekostuff.forEachIndexed { index, renderable ->
            renderable?.translate(Vector3f(index * 5f, myMap.getHeight(index * 5f, 15f)+1f, 15f))
        }



        animals = mutableListOf(
            ModelLoader.loadModel("project/assets/animals/bear.obj", 0f, Math.toRadians(180f), 0f),
            ModelLoader.loadModel("project/assets/animals/deerFemale.obj", 0f, Math.toRadians(180f), 0f),
            ModelLoader.loadModel("project/assets/animals/deerMale.obj", 0f, Math.toRadians(180f), 0f),
            ModelLoader.loadModel("project/assets/animals/fox.obj", 0f, Math.toRadians(180f), 0f),
            ModelLoader.loadModel("project/assets/animals/gecko.obj", 0f, Math.toRadians(180f), 0f),
            ModelLoader.loadModel("project/assets/animals/hare.obj", 0f, Math.toRadians(180f), 0f),
            ModelLoader.loadModel("project/assets/animals/mountainLion.obj", 0f, Math.toRadians(180f), 0f),
            ModelLoader.loadModel("project/assets/animals/opossum.obj", 0f, Math.toRadians(180f), 0f),
            ModelLoader.loadModel("project/assets/animals/racoon.obj", 0f, Math.toRadians(180f), 0f),
            ModelLoader.loadModel("project/assets/animals/turkey.obj", 0f, Math.toRadians(180f), 0f)
        )
        animals.forEachIndexed { index, renderable ->
            renderable?.translate(Vector3f(index * 3f, myMap.getHeight(index * 3f, 20f) + 1, 20f))
        }

        cabins = mutableListOf(
            ModelLoader.loadModel("project/assets/cabins/logCabin.obj", 0f, 0f, 0f),
            ModelLoader.loadModel("project/assets/cabins/lowPolyLogCabin.obj", 0f, 0f, 0f)
        )
        cabins.forEachIndexed { index, renderable ->
            renderable?.translate(Vector3f(index * 20f, myMap.getHeight(index * 20f, 60f), 60f))
        }

        drone = Drone()
//        drone = ModelLoader.loadModel("project/assets/drone/drone.obj", 0f, 0f, 0f)
        drone.translate(Vector3f(5f, myMap.getHeight(5f, 5f) + 1f, 5f))

        sniper = Sniper()
        sniper?.translate(Vector3f(5f, myMap.getHeight(5f, 5f) + 1, 5f))
        sniper?.scale(Vector3f(0.5f))
        camera.parent = sniper
        cube.parent = sniper
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

        shadowRenderer.render(dt, time)

        staticShader.use()
        staticShader.setUniform("depthMap", 3)
        staticShader.setUniform("viewPos", camera.getPosition())
        staticShader.setUniform("sunPos", shadowRenderer.sunPos)
        staticShader.setUniform("sunSpaceMatrix", shadowRenderer.sunSpaceMatrix, false)

        camera.bind(staticShader)
        myMap.render(staticShader)
        cube.render(staticShader)
        renderEntities(dt, time, staticShader)
//        renderLights(dt, time, staticShader)

        myMap.renderSkybox()

//        guiRenderer.render()

//        debugShader.use()
//        renderSquare(shadowRenderer.depthMap)

    }

    fun renderSquare(texID: Int) {
        var squareVAO = 0
        var squareVBO = 0
        if (squareVAO == 0) {
            val squareVertices = floatArrayOf(
                -1.0f,
                1.0f,
                0.0f,
                0.0f,
                1.0f,
                -1.0f,
                -1.0f,
                0.0f,
                0.0f,
                0.0f,
                1.0f,
                1.0f,
                0.0f,
                1.0f,
                1.0f,
                1.0f,
                -1.0f,
                0.0f,
                1.0f,
                0.0f
            )
            squareVAO = GL30.glGenVertexArrays()
            squareVBO = GL15C.glGenBuffers()
            GL30.glBindVertexArray(squareVAO)
            GL30.glBindBuffer(GL15.GL_ARRAY_BUFFER, squareVBO)
            GL30.glBufferData(GL15.GL_ARRAY_BUFFER, squareVertices, GL15C.GL_STATIC_DRAW)
            GL30.glEnableVertexAttribArray(0)
            GL30.glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * 4, 0)
            GL30.glEnableVertexAttribArray(1)
            GL30.glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * 4, 12)
        }
        GL30.glBindVertexArray(squareVAO)
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, texID)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
        GL30.glBindVertexArray(0)
    }

    fun renderLights(dt: Float, time: Float, shaderProgram: ShaderProgram) {
        shaderProgram.setUniform("anzLichter", lights.size)
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
                it.bind(shaderProgram, index)
            }
        }

        spotlight.bind(shaderProgram, camera.getCalculateViewMatrix())
    }

    fun renderEntities(dt: Float, time: Float, shaderProgram: ShaderProgram) {
        shaderProgram.use()

        drone.render(shaderProgram)
        trees.forEach { it?.render(shaderProgram) }
        dekostuff.forEach { it?.render(shaderProgram) }
        animals.forEach { it?.render(shaderProgram) }
        cabins.forEach { it?.render(shaderProgram) }
        sniper?.render(shaderProgram)
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

        drone.update(dt, time)

        if (window.getKeyState(GLFW_KEY_N)) drone.open(time)
        if (window.getKeyState(GLFW_KEY_M)) drone.close(time)

        if (window.getKeyState(GLFW_KEY_W)) sniper?.translate(Vector3f(0f, 0f, -5 * dt))
        if (window.getKeyState(GLFW_KEY_S)) sniper?.translate(Vector3f(0f, 0f, 5 * dt))
        if (window.getKeyState(GLFW_KEY_A)) sniper?.translate(Vector3f(-5 * dt, 0f, 0f))
        if (window.getKeyState(GLFW_KEY_D)) sniper?.translate(Vector3f(5 * dt, 0f, 0f))

        if (window.getKeyState(GLFW_KEY_SPACE)) sniper?.translate(Vector3f(0f, 5 * dt, 0f))
        if (window.getKeyState(GLFW_KEY_LEFT_SHIFT)) sniper?.translate(Vector3f(0f, -5 * dt, 0f))

        if (window.getKeyState(GLFW_KEY_I)) camera.translate(Vector3f(0f, 0f, -5 * dt))
        if (window.getKeyState(GLFW_KEY_K)) camera.translate(Vector3f(0f, 0f, 5 * dt))
        if (window.getKeyState(GLFW_KEY_J)) camera.rotate(0f, dt, 0f)
        if (window.getKeyState(GLFW_KEY_L)) camera.rotate(0f, -dt, 0f)

        if (window.getKeyState(GLFW_KEY_0)) Mesh.renderTriangles()
        if (window.getKeyState(GLFW_KEY_P)) Mesh.renderLines()


//        sniper?.translate(
//            Vector3f(
//                0f, myMap.getHeight(
//                    sniper!!.getPosition().x, sniper!!.getPosition().z
//                ) - sniper!!.getPosition()!!.y + 1, 0f
//            )
//        )
    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {
        sniper?.rotate(0f, (prevX - xpos).toFloat() * 0.002f, 0f)
        sniper?.rotate((prevY - ypos).toFloat() * 0.002f, 0f, 0f)

        prevX = xpos
        prevY = ypos
    }

    fun cleanup() {}
}