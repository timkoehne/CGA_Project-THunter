package cga.exercise.components.entities.animals

import cga.exercise.components.entities.Entity
import cga.exercise.components.map.MyMap
import cga.exercise.components.entities.movementcontroller.JumpMovementAI
import cga.exercise.components.texture.Texture2D
import cga.framework.ModelLoader

class Turkey(myMap: MyMap) : Entity(ModelLoader.loadModel(filepath), myMap, hitbox) {

    override val movementController: JumpMovementAI = JumpMovementAI(this)

    override val movementSpeed: Float = 3f
    override val weight = 0.3f
    override val jumpSpeed = 4f

    companion object {
        val filepath = "project/assets/animals/turkey.obj"
        private val hitbox = "project/assets/animals/turkeycube.obj"
        val image = Texture2D.invoke("project/assets/animals/pictures/turkey.png", true)

    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        movementController.update(dt, time)
    }


}