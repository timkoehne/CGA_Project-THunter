package cga.exercise.components.entities

import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram
import cga.framework.ModelLoader

class Cube(myMap: MyMap) : Entity(ModelLoader.loadModel(filepath, 0f, 0f, 0f), myMap) {

    override fun render(shaderProgram: ShaderProgram) {
        super.render(shaderProgram)
    }

    companion object {
        val filepath = "project/assets/models/cube.obj"
    }
}