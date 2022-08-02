package cga.exercise.components.entities

import cga.framework.ModelLoader

class Sniper : Entity(listOf(ModelLoader.loadModel(filepath, 0f, 0f, 0f))) {

    companion object {
        val filepath = "project/assets/sniper/sniper.obj"
    }

    override fun update(dt: Float, time: Float) {

    }
}