package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

/**
 * Creates a Mesh object from vertexdata, intexdata and a given set of vertex attributes
 *
 * @param vertexdata plain float array of vertex data
 * @param indexdata  index data
 * @param attributes vertex attributes contained in vertex data
 * @throws Exception If the creation of the required OpenGL objects fails, an exception is thrown
 *
 * Created by Fabian on 16.09.2017.
 */
open class Mesh(vertexdata: FloatArray, indexdata: IntArray, attributes: Array<VertexAttribute>, private val material: Material? = null) {
    //private data

    private var vao = 0
    private var vbo = 0
    private var ibo = 0
    private var indexcount = 0

    var useLinesInstead = false

    init {

        // setup vao
        vao = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vao)

        //setup vbo
        vbo = GL15.glGenBuffers()
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexdata, GL15.GL_STATIC_DRAW)

        //ibo
        ibo = GL15.glGenBuffers()
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexdata, GL15.GL_STATIC_DRAW)
        indexcount = indexdata.size

        //vertex attribute
        for (num in attributes.indices) {
            GL20.glEnableVertexAttribArray(num)
            GL20.glVertexAttribPointer( //vbo in vao speichern
                num, attributes[num].n, attributes[num].type, false, attributes[num].stride, attributes[num].offset.toLong()
            )
        }

        //unbind for safety
        GL30.glBindVertexArray(0)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
        //TODO ibo entbinden
    }

    /**
     * renders the mesh
     */
    fun render(shaderProgram: ShaderProgram) {
        GL30.glBindVertexArray(vao)

        material?.bind(shaderProgram)

        if (useLinesInstead)
            GL11.glDrawElements(GL11.GL_LINES, indexcount, GL11.GL_UNSIGNED_INT, 0)
        else
            GL11.glDrawElements(GL11.GL_TRIANGLES, indexcount, GL11.GL_UNSIGNED_INT, 0)
        GL30.glBindVertexArray(0)
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    fun cleanup() {
        if (ibo != 0) GL15.glDeleteBuffers(ibo)
        if (vbo != 0) GL15.glDeleteBuffers(vbo)
        if (vao != 0) GL30.glDeleteVertexArrays(vao)
    }
}