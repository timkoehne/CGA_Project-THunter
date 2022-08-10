package cga.exercise.components.entities

import cga.exercise.components.entities.movementai.JumpMovementAI
import cga.exercise.components.map.MyMap
import cga.exercise.components.entities.movementai.MovementAI
import cga.framework.ModelLoader

class MountainLion(myMap: MyMap) : Entity(ModelLoader.loadModel(filepath), myMap) {

    val movementAI = JumpMovementAI(this)

    override val movementSpeed: Float = 5f
    override val jumpSpeed = 2f
    override val weight = 0.5f

    companion object {
        val filepath = "project/assets/animals/mountainLion.obj"

    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        movementAI.update(dt, time)
    }


}