package cga.exercise.components.entities

import cga.exercise.components.entities.movementcontroller.JumpMovementAI
import cga.exercise.components.map.MyMap
import cga.exercise.components.texture.Texture2D
import cga.framework.ModelLoader

class Hare(myMap: MyMap) : Entity(ModelLoader.loadModel(filepath), myMap, hitbox) {

    override val movementController = JumpMovementAI(this)

    override val movementSpeed: Float = 4f
    override val weight = 0.5f

    companion object {
        val filepath = "project/assets/animals/hare.obj"
        private val hitbox = "project/assets/animals/harecube.obj"
        val image = Texture2D.invoke("project/assets/animals/pictures/hare.png", true)

    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        movementController.update(dt, time)
    }


}