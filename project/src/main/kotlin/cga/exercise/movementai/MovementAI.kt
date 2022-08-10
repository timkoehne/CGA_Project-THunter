package cga.exercise.movementai

import cga.exercise.components.entities.Entity
import cga.exercise.components.geometry.Transformable
import org.joml.Matrix4f
import org.joml.Random
import org.joml.Vector3f

open class MovementAI(val entity: Entity) {

    private val random: Random = Random()

    private val maxDistance = 10f
    private val maxRotationAngle = 90.0 //in a cone infront of the entity
    private val maxSleepDuration = 2f

    private var rotationGoal: Float = 0f
    private var walkingDistance: Float = 0f
    private var sleepDuration: Float = 0f


    open fun update(dt: Float, time: Float) {

        //find new goal
        if (walkingDistance in -0.1..0.1) {
            rotationGoal = ((random.nextFloat() - 0.5f) * 2) * Math.toRadians(maxRotationAngle).toFloat()
            walkingDistance = random.nextFloat() * maxDistance
            sleepDuration = random.nextFloat() * maxSleepDuration
        }

        if (sleepDuration > 0) {
            onSleep(dt, time)
        } else {
            //rotate towards goal
            if (rotationGoal > Math.toRadians(359.0) || rotationGoal < Math.toRadians(1.0)) {
                onMove(dt, time)
            } else {
                //move towards goal
                onRotate(dt, time)
            }
        }
    }

    open fun onMove(dt: Float, time: Float) {
        entity.translate(Vector3f(0f, 0f, entity.movementSpeed * dt))
        walkingDistance -= entity.movementSpeed * dt

    }

    open fun onRotate(dt: Float, time: Float) {
        entity.rotate(0f, Math.toRadians((entity.movementSpeed * 50 * dt).toDouble()).toFloat(), 0f)
        rotationGoal -= Math.toRadians((entity.movementSpeed * 50 * dt).toDouble()).toFloat()

    }

    open fun onSleep(dt: Float, time: Float) {
        sleepDuration -= dt
    }

}