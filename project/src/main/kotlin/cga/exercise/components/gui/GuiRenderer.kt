package cga.exercise.components.gui

import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.exercise.game.Scene
import cga.framework.GameWindow
import org.joml.Vector2f
import org.lwjgl.opengl.*

class GuiRenderer(private val window: GameWindow, private val guiElements: MutableList<GuiElement> = mutableListOf()) {

    val aspectRatio: Float
        get() = window.windowHeight.toFloat() / window.windowWidth

    companion object {
        val squareVertices = floatArrayOf(
            -1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            -1.0f, -1.0f, 0.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 0.0f, 1.0f, 0.0f
        )
    }

    val guiShader: ShaderProgram
    var vao = 0
    var vbo = 0

    init {
        guiShader = ShaderProgram("project/assets/shaders/gui_vert.glsl", "project/assets/shaders/gui_frag.glsl")
        vao = GL30.glGenVertexArrays()
        vbo = GL15C.glGenBuffers()
        GL30.glBindVertexArray(vao)
        GL30.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL30.glBufferData(GL15.GL_ARRAY_BUFFER, squareVertices, GL15C.GL_STATIC_DRAW)
        GL30.glEnableVertexAttribArray(0)
        GL30.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5 * 4, 0)
        GL30.glEnableVertexAttribArray(1)
        GL30.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 5 * 4, 12)
    }

    fun addElement(guiElement: GuiElement) {
        guiElements.add(guiElement)
    }

    fun render() {
        guiShader.use()
        GL30.glBindVertexArray(vao)
        for (element in guiElements) {
            element.render(guiShader)
        }

        GL30.glBindVertexArray(0)
    }

    fun cleanUp(){
        if (vbo != 0) GL15.glDeleteBuffers(vbo)
        if (vao != 0) GL30.glDeleteVertexArrays(vao)
    }


}