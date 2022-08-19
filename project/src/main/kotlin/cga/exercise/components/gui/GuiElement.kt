package cga.exercise.components.gui

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.*

open class GuiElement(
    var texID: Int?,
    position: Vector2f = Vector2f(),
    scale: Vector2f = Vector2f(0.3f, 0.3f)
) : Transformable() {


    constructor(
        texture: Texture2D,
        position: Vector2f = Vector2f(),
        scale: Vector2f = Vector2f(0.3f, 0.3f)
    ) : this(
        texture.texID,
        position,
        scale
    )

    companion object {
        val squareVertices = floatArrayOf(
            -1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            -1.0f, -1.0f, 0.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 0.0f, 1.0f, 0.0f
        )
    }

    var vao = 0
    var vbo = 0
    var isShowing: Boolean = true
    var children = mutableListOf<GuiElement>()


    init {
        translate(position)
        scale(scale)

        if (vao == 0) {
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
        GL30.glBindVertexArray(vao)

        if (texID != null) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0)
            GL11.glBindTexture(GL20.GL_TEXTURE_2D, texID!!)
        }

        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4)
        GL30.glBindVertexArray(0)
    }

    fun translate(deltaPos: Vector2f) {
        translate(Vector3f(deltaPos.x, deltaPos.y, 0f))
    }

    fun scale(scale: Vector2f) {
        scale(Vector3f(scale.x, scale.y, 1f))
    }

    open fun enable() {
        isShowing = true
    }

    open fun disable() {
        isShowing = false
    }

    open fun toggle() {
        isShowing = !isShowing
    }

    open fun update(dt: Float, time: Float) {

    }

    open fun render(shaderProgram: ShaderProgram) {
        if (isShowing) {

            children.forEach { it.render(shaderProgram) }

            if (texID != null) {
                GL13.glActiveTexture(GL13.GL_TEXTURE0)
                GL11.glBindTexture(GL20.GL_TEXTURE_2D, texID!!)
                shaderProgram.setUniform("model_matrix", getWorldModelMatrix(), false)
                GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4)
            }
        }
    }


}