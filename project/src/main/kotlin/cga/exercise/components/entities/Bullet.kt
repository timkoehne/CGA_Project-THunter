package cga.exercise.components.entities

import cga.exercise.collision.AABB
import cga.exercise.components.entities.traits.GravityTrait
import cga.exercise.components.map.MyMap
import cga.framework.ModelLoader

class Bullet(myMap: MyMap) : Entity(ModelLoader.loadModel(filepath), myMap, hitbox) {

    override val gravityTrait = null

    companion object {
        val filepath = "project/assets/character/bullet.obj"
        val hitbox = "project/assets/character/bullet hitbox.obj"

        val maxDistance = 50

    }


    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        moveForward(dt)
    }


}