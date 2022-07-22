package cga.exercise.components.geometry.skybox

import cga.exercise.components.shader.ShaderProgram
import org.lwjgl.opengl.*


class SkyboxRenderer(val skybox: Skybox) {


    companion object {

        val multiplier = 5f
        var vertices = mutableListOf<Float>(
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            -1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,

            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,

            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,

            -1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,

            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f
        ).toFloatArray()
    }

    private var vao = 0
    private var vbo = 0

    init {
        // setup vao
        vao = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vao)

        //setup vbo
        vbo = GL15.glGenBuffers()
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW)

        GL20.glEnableVertexAttribArray(0)
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 4, 0)

        GL30.glBindVertexArray(0)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)

    }

    fun bind(shaderProgram: ShaderProgram) {

        GL30.glBindVertexArray(vao)
        GL20.glEnableVertexAttribArray(0)

        GL30.glActiveTexture(GL13.GL_TEXTURE5)
        GL30.glBindTexture(GL30.GL_TEXTURE_CUBE_MAP, skybox.texID)
        shaderProgram.setUniform("skybox", 5)

        GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, vertices.size)

    }

}


