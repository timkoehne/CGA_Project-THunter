package cga.exercise.components.entities.movementcontroller

import cga.exercise.components.entities.Entity
import cga.framework.GameWindow
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW

open class MovementController(val entity: Entity) {

    val distanceTraveled = Vector3f()

    private fun move(direction: Vector3f): Boolean {
        distanceTraveled.add(direction)

        entity.translate(direction)

        val collisionDetected = entity.movementCollision()
        if (collisionDetected) {
            entity.translate(direction.mul(-1f))
        }
        return collisionDetected
    }

    open fun update(dt: Float, time: Float) {

    }

    fun moveForward(dt: Float): Boolean {
        val direction = Vector3f(0f, 0f, -entity.movementSpeed * dt)

        return move(direction)
    }

    fun moveLeft(dt: Float): Boolean {
        val direction = Vector3f(-entity.movementSpeed * dt, 0f, 0f)
        return move(direction)
    }

    fun moveRight(dt: Float): Boolean {
        val direction = Vector3f(entity.movementSpeed * dt, 0f, 0f)
        return move(direction)
    }

    fun moveBack(dt: Float): Boolean {
        val direction = Vector3f(0f, 0f, entity.movementSpeed * dt)
        return move(direction)
    }

    fun moveDown(dt: Float): Boolean {
        val direction = Vector3f(0f, -entity.movementSpeed * dt, 0f)
        return move(direction)
    }

    fun moveUp(dt: Float): Boolean {
        val direction = Vector3f(0f, entity.movementSpeed * dt, 0f)
        return move(direction)
    }

    open fun inputControl(dt: Float, time: Float, window: GameWindow) {
        if (window.getKeyState(GLFW.GLFW_KEY_W)) moveForward(dt)
        if (window.getKeyState(GLFW.GLFW_KEY_A)) moveLeft(dt)
        if (window.getKeyState(GLFW.GLFW_KEY_S)) moveBack(dt)
        if (window.getKeyState(GLFW.GLFW_KEY_D)) moveRight(dt)
        if (window.getKeyState(GLFW.GLFW_KEY_SPACE)) entity.jump()
    }

}