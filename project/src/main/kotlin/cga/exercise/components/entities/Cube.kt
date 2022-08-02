package cga.exercise.components.entities

import cga.exercise.components.shader.ShaderProgram
import cga.framework.ModelLoader

class Cube() : Entity(ModelLoader.loadModel(filepath, 0f, 0f, 0f)) {

    override fun render(shaderProgram: ShaderProgram) {
        super.render(shaderProgram)
    }

    companion object {
        val filepath = "project/assets/models/cube.obj"
    }
}