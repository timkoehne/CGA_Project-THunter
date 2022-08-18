package cga.exercise.components.entities.animals

import cga.exercise.components.entities.Entity
import cga.exercise.components.map.MyMap
import cga.exercise.components.entities.movementcontroller.JumpMovementAI
import cga.exercise.components.texture.Texture2D
import cga.framework.ModelLoader
import org.joml.Random

class Deer(myMap: MyMap) : Entity(ModelLoader.loadModel(filepath[random.nextInt(2)]), myMap, hitbox) {

    override val movementController: JumpMovementAI = JumpMovementAI(this)

    override val movementSpeed: Float = 5f

    companion object {
        val random = Random()
        val filepath = listOf("project/assets/animals/deerFemale.obj", "project/assets/animals/deerMale.obj")
        private val hitbox = "project/assets/animals/femaledeercube.obj"
        val image = Texture2D.invoke("project/assets/animals/pictures/deerFemale.png", true)
    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        movementController.update(dt, time)
    }
}