package cga.exercise.components.entities

import cga.exercise.components.map.MyMap
import cga.exercise.components.entities.movementcontroller.JumpMovementAI
import cga.framework.ModelLoader

class Gecko(myMap: MyMap) : Entity(ModelLoader.loadModel(filepath), myMap) {

    override val movementController: JumpMovementAI = JumpMovementAI(this)

    override val movementSpeed: Float = 2.5f
    override val jumpSpeed = 2.5f

    companion object {
        val filepath = "project/assets/animals/gecko.obj"

    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        movementController.update(dt, time)
    }


}