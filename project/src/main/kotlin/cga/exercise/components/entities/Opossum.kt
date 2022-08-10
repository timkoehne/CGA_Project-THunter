package cga.exercise.components.entities

import cga.exercise.components.map.MyMap
import cga.exercise.components.entities.movementai.MovementAI
import cga.framework.ModelLoader

class Opossum(myMap: MyMap) : Entity(ModelLoader.loadModel(filepath), myMap) {

    val movementAI = MovementAI(this)

    override val movementSpeed: Float = 3f
    override val jumpSpeed = 4f

    companion object {
        val filepath = "project/assets/animals/opossum.obj"

    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        movementAI.update(dt, time)
    }


}