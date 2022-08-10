package cga.exercise.components.gui

import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.exercise.game.Scene
import cga.framework.GameWindow
import org.joml.Vector2f
import org.lwjgl.opengl.*

class GuiRenderer(
    scene: Scene,
    private val window: GameWindow,
    private val guiElements: MutableList<GuiElement> = mutableListOf()
) {

    val sniperScope: GuiElement
    val crosshair: GuiElement
    val youWin: GuiElement
    val youLoose: GuiElement
    val wantedPoster: WantedPoster
    val wantedPosterButton: GuiElement
    val ammoAnzeige: AmmoAnzeige
    val timer: Timer

    companion object {
        val squareVertices = floatArrayOf(
            -1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            -1.0f, -1.0f, 0.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 0.0f, 1.0f, 0.0f
        )
    }

    val guiShader: ShaderProgram =
        ShaderProgram("project/assets/shaders/gui_vert.glsl", "project/assets/shaders/gui_frag.glsl")
    var vao = 0
    var vbo = 0

    init {
        vao = GL30.glGenVertexArrays()
        vbo = GL15C.glGenBuffers()
        GL30.glBindVertexArray(vao)
        GL30.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL30.glBufferData(GL15.GL_ARRAY_BUFFER, squareVertices, GL15C.GL_STATIC_DRAW)
        GL30.glEnableVertexAttribArray(0)
        GL30.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5 * 4, 0)
        GL30.glEnableVertexAttribArray(1)
        GL30.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 5 * 4, 12)


        val sunView = GuiElement(
            scene.shadowRenderer.depthMap,
            Vector2f(-1 + (0.2f / 1.77f), 0.9f),
            Vector2f(0.2f / scene.camera.aspect, 0.2f)
        )
        guiElements.add(sunView)

        crosshair = GuiElement(
            Texture2D.invoke("project/assets/textures/crosshair2.png", true),
            Vector2f(),
            Vector2f(0.1f / scene.camera.aspect, 0.1f)
        )
        guiElements.add(crosshair)


        sniperScope = GuiElement(
            Texture2D.invoke("project/assets/textures/scope.png", true),
            Vector2f(0f, 0f),
            Vector2f(2f / scene.camera.aspect, 1f)
        )
        sniperScope.disable()
        guiElements.add(sniperScope)

        wantedPoster = WantedPoster()
        guiElements.add(wantedPoster)
        wantedPoster.disable()

        wantedPosterButton = GuiElement(
            Texture2D.invoke("project/assets/textures/rolle.png", true),
            Vector2f(1 - 0.15f, 1 - 0.15f),
            Vector2f(0.15f / scene.camera.aspect, 0.15f)
        )
        guiElements.add(wantedPosterButton)

        timer = Timer(300f)
        guiElements.add(timer)


        youWin = GuiElement(Texture2D.invoke("project/assets/textures/win.png", true))
        youWin.translate(Vector2f(0f, 1.5f))
        youWin.scale(Vector2f(1f, 0.7f))
        youWin.disable()
        guiElements.add(youWin)

        youLoose = GuiElement(Texture2D.invoke("project/assets/textures/loose.png", true))
        youLoose.translate(Vector2f(0f, 1.5f))
        youLoose.scale(Vector2f(1f, 0.7f))
        youLoose.disable()
        guiElements.add(youLoose)

        ammoAnzeige = AmmoAnzeige()
        guiElements.add(ammoAnzeige)


    }

    fun update(dt: Float, time: Float) {
        guiElements.forEach { it.update(dt, time) }

        if (timer.timeInSekunden <= 0) {
            youLoose.enable()
        }

        if (wantedPoster.killCounter >= 6) {
            youWin.enable()
        }


    }

    fun render() {
        guiShader.use()
        GL30.glBindVertexArray(vao)
        for (element in guiElements) {
            element.render(guiShader)
        }

        GL30.glBindVertexArray(0)
    }

    fun cleanUp() {
        if (vbo != 0) GL15.glDeleteBuffers(vbo)
        if (vao != 0) GL30.glDeleteVertexArrays(vao)
    }


}