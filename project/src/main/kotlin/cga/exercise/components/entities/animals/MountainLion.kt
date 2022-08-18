package cga.exercise.components.entities.animals

import cga.exercise.components.entities.Entity
import cga.exercise.components.entities.movementcontroller.JumpMovementAI
import cga.exercise.components.map.MyMap
import cga.exercise.components.texture.Texture2D
import cga.framework.ModelLoader

class MountainLion(myMap: MyMap) : Entity(ModelLoader.loadModel(filepath), myMap, hitbox) {

    override val movementController = JumpMovementAI(this)

    override val movementSpeed: Float = 5f
    override val jumpSpeed = 2f
    override val weight = 0.5f

    companion object {
        val filepath = "project/assets/animals/mountainLion.obj"
        private val hitbox = "project/assets/animals/mountainLioncube.obj"
        val image = Texture2D.invoke("project/assets/animals/pictures/mountainLion.png", true)

    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        movementController.update(dt, time)
    }


}