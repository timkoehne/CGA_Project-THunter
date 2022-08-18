package cga.exercise.components.entities.animals

import cga.exercise.components.entities.Entity
import cga.exercise.components.map.MyMap
import cga.exercise.components.entities.movementcontroller.BasicMovementAI
import cga.exercise.components.texture.Texture2D
import cga.framework.ModelLoader

class Opossum(myMap: MyMap) : Entity(ModelLoader.loadModel(filepath), myMap, hitbox) {

    override val movementController = BasicMovementAI(this)

    override val movementSpeed: Float = 3f
    override val jumpSpeed = 4f

    companion object {
        val filepath = "project/assets/animals/opossum.obj"
        private val hitbox = "project/assets/animals/opossumcube.obj"
        val image = Texture2D.invoke("project/assets/animals/pictures/opossum.png", true)

    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        movementController.update(dt, time)
    }


}