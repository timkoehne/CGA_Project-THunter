package cga.exercise.components.entities

import cga.exercise.components.geometry.ComplexModel
import cga.exercise.components.shader.ShaderProgram

class Cube() : ComplexModel(filepath) {

    override fun render(shaderProgram: ShaderProgram) {
            super.render(shaderProgram)
    }

    companion object {
        val filepath = "project/assets/models/cube.obj"
    }
}