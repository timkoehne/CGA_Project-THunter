package cga.exercise.components.entities

import cga.exercise.components.map.MyMap
import cga.exercise.components.entities.movementcontroller.BasicMovementAI
import cga.exercise.components.texture.Texture2D
import cga.framework.ModelLoader

class Raccoon(myMap: MyMap) : Entity(ModelLoader.loadModel(filepath), myMap, hitbox) {

    override val movementController = BasicMovementAI(this)

    override val movementSpeed: Float = 3f
    override val jumpSpeed = 4f

    companion object {
        val filepath = "project/assets/animals/raccoon.obj"
        private val hitbox = "project/assets/animals/raccooncube.obj"
        val image = Texture2D.invoke("project/assets/animals/pictures/raccoon.png", true)

    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        movementController.update(dt, time)
    }


}