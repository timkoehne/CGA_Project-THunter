package cga.exercise.components.geometry

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL31
import java.util.Random

class GrassInstance(amount: Int, private val myMap: MyMap) : Quad(amount, texure, myMap) {

    companion object {
        val texure = Texture2D.invoke("project/assets/textures/quadGrass.png", false)


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
    }

    fun render(camera: TronCamera) {
        instanceShader.use()

        texture.bind(0)

        camera.bind(instanceShader)
        instanceShader.setUniform("image", 0)
        for (model in modelMatrizen) {
            GL30.glBindVertexArray(vao)
            GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, indexdata.size, GL11.GL_UNSIGNED_INT, 0, amount)
        }
    }


}