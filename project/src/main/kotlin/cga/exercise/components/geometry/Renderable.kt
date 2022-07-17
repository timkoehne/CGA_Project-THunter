package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4fStack
import org.joml.Vector3f

class Renderable(var meshes: MutableList<Mesh>) : Transformable(), IRenderable {

    var color = Vector3f()

    override fun render(shaderProgram: ShaderProgram) {
        //println("Modelmatrix: " + getModelMatrix())
        shaderProgram.setUniform("model_matrix", getModelMatrix(), false)
        shaderProgram.setUniform("fragColor", color)
        for (mesh in meshes) {
            mesh.render(shaderProgram)
        }
    }
}