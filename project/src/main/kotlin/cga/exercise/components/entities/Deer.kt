package cga.exercise.components.entities

import cga.exercise.components.map.MyMap
import cga.exercise.components.entities.movementai.JumpMovementAI
import cga.framework.ModelLoader
import org.joml.Random

class Deer(myMap: MyMap) : Entity(ModelLoader.loadModel(filepath[random.nextInt(2)]), myMap, hitbox) {

    val movementAI: JumpMovementAI = JumpMovementAI(this)

    override val movementSpeed: Float = 5f

    companion object {
        val random = Random()
        val filepath = listOf("project/assets/animals/deerFemale.obj", "project/assets/animals/deerMale.obj")
        private val hitbox = "project/assets/animals/femaledeercube.obj"
    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        movementAI.update(dt, time)
    }
}