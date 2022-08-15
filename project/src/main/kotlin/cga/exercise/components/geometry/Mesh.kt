package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL11.GL_POLYGON_MODE
import kotlin.math.min

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
open class Mesh(
    var vertexdata: FloatArray,
    var indexdata: IntArray,
    attributes: Array<VertexAttribute>,
    var material: Material? = null
) {
    private var vao = 0
    private var vbo = 0
    private var ibo = 0
    private var indexcount = 0

    private var minVertexPositions: Vector3f? = null
    private var maxVertexPositions: Vector3f? = null

    init {
        vao = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vao)

        vbo = GL15.glGenBuffers()
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexdata, GL15.GL_STATIC_DRAW)

        ibo = GL15.glGenBuffers()
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexdata, GL15.GL_STATIC_DRAW)
        indexcount = indexdata.size

        for (num in attributes.indices) {

            GL20.glEnableVertexAttribArray(num)
            GL20.glVertexAttribPointer(
                num,
                attributes[num].n,
                attributes[num].type,
                false,
                attributes[num].stride,
                attributes[num].offset.toLong()
            )
        }

        GL30.glBindVertexArray(0)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    fun uploadVertexData() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexdata, GL15.GL_STREAM_DRAW)
    }

    fun uploadIndexData() {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexdata, GL15.GL_STATIC_DRAW)
    }

    /**
     * renders the mesh
     */
    fun render(shaderProgram: ShaderProgram) {
        GL30.glBindVertexArray(vao)
        material?.bind(shaderProgram)
        GL11.glDrawElements(GL11.GL_TRIANGLES, indexcount, GL11.GL_UNSIGNED_INT, 0)
        GL30.glBindVertexArray(0)
    }

    fun render(shaderProgram: ShaderProgram, texID: Int) {
        GL30.glBindVertexArray(vao)

        GL13.glActiveTexture(GL13.GL_TEXTURE2)
        GL11.glBindTexture(GL20.GL_TEXTURE_2D, texID)
        shaderProgram.setUniform("diff", 2)
        GL11.glDrawElements(GL11.GL_TRIANGLES, indexcount, GL11.GL_UNSIGNED_INT, 0)
        GL30.glBindVertexArray(0)
    }

    fun minVertex(): Vector3f {

        if(minVertexPositions == null){
            minVertexPositions = Vector3f(minX(), minY(), minZ())
        }
        return minVertexPositions!!
    }

    private fun minX(): Float {
        var minValue = vertexdata[0]
        vertexdata.forEachIndexed { index, value -> if (index % 8 == 0 && value < minValue) minValue = value }
        return minValue
    }

    private fun minY(): Float {
        var minValue = vertexdata[1]
        vertexdata.forEachIndexed { index, value -> if (index % 8 == 1 && value < minValue) minValue = value }
        return minValue
    }

    private fun minZ(): Float {
        var minValue = vertexdata[2]
        vertexdata.forEachIndexed { index, value -> if (index % 8 == 2 && value < minValue) minValue = value }
        return minValue
    }

    fun maxVertex(): Vector3f {
        if(maxVertexPositions == null){
            maxVertexPositions = Vector3f(maxX(), maxY(),maxZ())
        }
        return maxVertexPositions!!
    }

    private fun maxX(): Float {
        var maxValue = vertexdata[0]
        vertexdata.forEachIndexed { index, value -> if (index % 8 == 0 && value > maxValue) maxValue = value }
        return maxValue
    }

    private fun maxY(): Float {
        var maxValue = vertexdata[1]
        vertexdata.forEachIndexed { index, value -> if (index % 8 == 1 && value > maxValue) maxValue = value }
        return maxValue
    }

    private fun maxZ(): Float {
        var maxValue = vertexdata[2]
        vertexdata.forEachIndexed { index, value -> if (index % 8 == 2 && value > maxValue) maxValue = value }
        return maxValue
    }


    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    fun cleanUp() {
        if (ibo != 0) GL15.glDeleteBuffers(ibo)
        if (vbo != 0) GL15.glDeleteBuffers(vbo)
        if (vao != 0) GL30.glDeleteVertexArrays(vao)
    }

    companion object {

        fun renderLines() {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE)
        }

        fun renderTriangles() {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL)
        }


        val stride: Int = 8 * 4 //sizeOf(float) = 4
        val attrPos = VertexAttribute(3, GL11.GL_FLOAT, stride, 0) //position
        val attrTC = VertexAttribute(2, GL11.GL_FLOAT, stride, 3 * 4) //textureCoordinate
        val attrNorm = VertexAttribute(3, GL11.GL_FLOAT, stride, 5 * 4) //normalval
        val vertexAttributes = arrayOf<VertexAttribute>(attrPos, attrTC, attrNorm)
    }


}