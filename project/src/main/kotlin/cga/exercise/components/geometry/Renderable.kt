package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f

open class Renderable(var meshes: MutableList<Mesh>) : Transformable(), IRenderable {

    var color = Vector3f()

    override fun render(shaderProgram: ShaderProgram) {
        shaderProgram.setUniform("model_matrix", getWorldModelMatrix(), false)
        shaderProgram.setUniform("fragColor", color)
        for (mesh in meshes) {
            mesh.render(shaderProgram)
        }
    }
}