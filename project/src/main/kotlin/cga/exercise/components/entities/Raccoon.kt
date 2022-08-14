package cga.exercise.components.entities

import cga.exercise.components.map.MyMap
import cga.exercise.components.entities.movementai.MovementAI
import cga.framework.ModelLoader

class Raccoon(myMap: MyMap) : Entity(ModelLoader.loadModel(filepath), myMap, hitbox) {

    val movementAI = MovementAI(this)

    override val movementSpeed: Float = 3f
    override val jumpSpeed = 4f

    companion object {
        val filepath = "project/assets/animals/raccoon.obj"
        private val hitbox = "project/assets/animals/raccooncube.obj"

    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        movementAI.update(dt, time)
    }


}