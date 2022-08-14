package cga.exercise.components.entities

import cga.exercise.components.map.MyMap
import cga.exercise.components.entities.movementai.JumpMovementAI
import cga.exercise.components.entities.movementai.MovementAI
import cga.framework.ModelLoader

class Bear(myMap: MyMap) : Entity(ModelLoader.loadModel(filepath), myMap, hitbox) {

    val movementAI: JumpMovementAI = JumpMovementAI(this)

    override val movementSpeed: Float = 3f
    override val weight = 1.5f
    override val jumpSpeed = 4f

    companion object {
        val filepath = "project/assets/animals/bear.obj"
        private val hitbox = "project/assets/animals/bearcube.obj"

    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        movementAI.update(dt, time)
    }


}