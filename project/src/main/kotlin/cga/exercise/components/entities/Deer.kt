package cga.exercise.components.entities

import cga.exercise.components.map.MyMap
import cga.exercise.movementai.JumpMovementAI
import cga.exercise.movementai.MovementAI
import cga.framework.ModelLoader

class Deer(myMap: MyMap) : Entity(ModelLoader.loadModel(filepath), myMap) {

    val movementAI: JumpMovementAI = JumpMovementAI(this)

    override val movementSpeed: Float = 5f

    companion object {
        val filepath = "project/assets/animals/deerFemale.obj"

    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        movementAI.update(dt, time)
    }


}