package cga.exercise.components.geometry

import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.*
import java.util.*

abstract class Quad(val amount: Int, val texture: Texture2D, myMap: MyMap) : Transformable() {

    val modelMatrizen = Array<Matrix4f>(amount*16) { Matrix4f() }


    var vao = 0
    private var vbo = 0
    private var ibo = 0
    private var indexcount = 0


    companion object {
        val instanceShader =
            ShaderProgram("project/assets/shaders/instance_vert.glsl", "project/assets/shaders/instance_frag.glsl")

        final val SIZE = 1f

        var vertexdata = floatArrayOf(
            // positions       // textureCords
            -SIZE, SIZE, 0.0f, 0.0f, 1.0f,
            -SIZE, -SIZE, 0.0f, 0.0f, 0.0f,
            SIZE, -SIZE, 0.0f, 1.0f, 0.0f,
            SIZE, SIZE, 0.0f, 1.0f, 1.0f
        )
        var indexdata = intArrayOf(
            0, 1, 2,
            2, 3, 0
        )
    }

    init {



        val rand = Random()
        for (matrix in modelMatrizen) {
            val x = (rand.nextFloat() * 100)
            val z = (rand.nextFloat() * 100)
//            println(matrix)
            matrix.translate(Vector3f(x, myMap.getHeight(x, z), z))
//            println(matrix)
//            println("  ")
            val scaleFactor = (rand.nextFloat() * 0.5f) + 0.8f
            matrix.scale(scaleFactor)

            val rotAngle = rand.nextFloat() * 360
            matrix.rotate(rotAngle, Vector3f(0f, 1f, 0f))
        }










        vao = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vao)

        vbo = GL15.glGenBuffers()
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)

        val matrixBuffer = BufferUtils.createFloatBuffer(amount)
        modelMatrizen.forEachIndexed { index, matrix4f -> matrix4f.get(index * 16, matrixBuffer) }

        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, matrixBuffer, GL15.GL_STREAM_DRAW)



        ibo = GL15.glGenBuffers()
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexdata, GL15.GL_STATIC_DRAW)
        indexcount = indexdata.size


        val floatSize: Int = 4
        val vec4fSize: Int = 4 * floatSize

//
//        //position
//        GL20.glEnableVertexAttribArray(0)
//        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * 4, 0)
//
//        //textureCords
//        GL20.glEnableVertexAttribArray(1)
//        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 2 * 4, 3)

        //transformation_matrix is the size of 4 * sizeof(vec4) and uses 4 vertrexattributes
        GL20.glEnableVertexAttribArray(0)
        GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 4 * vec4fSize, 0 * floatSize.toLong())

        GL20.glEnableVertexAttribArray(1)
        GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 4 * vec4fSize, 4 * floatSize.toLong())

        GL20.glEnableVertexAttribArray(2)
        GL20.glVertexAttribPointer(2, 4, GL11.GL_FLOAT, false, 4 * vec4fSize, 8 * floatSize.toLong())

        GL20.glEnableVertexAttribArray(3)
        GL20.glVertexAttribPointer(3, 4, GL11.GL_FLOAT, false, 4 * vec4fSize, 12 * floatSize.toLong())

        GL33.glVertexAttribDivisor(0, 1)
        GL33.glVertexAttribDivisor(1, 1)
        GL33.glVertexAttribDivisor(2, 1)
        GL33.glVertexAttribDivisor(3, 1)

        GL30.glBindVertexArray(0)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)
    }
}