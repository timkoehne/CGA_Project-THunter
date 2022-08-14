package cga.exercise.components.entities

import cga.exercise.components.map.MyMap
import cga.framework.ModelLoader

class Sniper(myMap: MyMap) : Entity(listOf(ModelLoader.loadModel(filepath, 0f, 0f, 0f)), myMap) {

    override val height = 1.6f

    companion object {
        val filepath = "project/assets/sniper/sniper.obj"
    }

}