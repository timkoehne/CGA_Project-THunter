package cga.exercise.components.entities

import cga.exercise.components.map.MyMap
import cga.exercise.components.entities.movementcontroller.JumpMovementAI
import cga.exercise.components.texture.Texture2D
import cga.framework.ModelLoader

class Bear(myMap: MyMap) : Entity(ModelLoader.loadModel(filepath), myMap, hitbox) {

    override val movementController: JumpMovementAI = JumpMovementAI(this)

    override val movementSpeed: Float = 3f
    override val weight = 1.5f
    override val jumpSpeed = 4f

    companion object {



        val filepath = "project/assets/animals/bear.obj"
        private val hitbox = "project/assets/animals/bearcube.obj"
        val image = Texture2D.invoke("project/assets/animals/pictures/bear.png", true)

    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        movementController.update(dt, time)
    }


}