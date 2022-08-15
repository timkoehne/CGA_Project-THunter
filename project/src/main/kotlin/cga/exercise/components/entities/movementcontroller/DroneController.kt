package cga.exercise.components.entities.movementcontroller

import cga.exercise.components.entities.Drone
import cga.exercise.components.entities.Entity
import cga.framework.GameWindow
import org.lwjgl.glfw.GLFW

class DroneController(entity: Drone) : MovementController(entity) {

    override fun inputControl(dt: Float, time: Float, window: GameWindow) {

        if ((entity as Drone).animationTrait.state == DroneAnimationTrait.State.Open) {

            if (window.getKeyState(GLFW.GLFW_KEY_W)) moveForward(dt)
            if (window.getKeyState(GLFW.GLFW_KEY_A)) entity.rotate(0f, 2.3f * dt, 0f)
            if (window.getKeyState(GLFW.GLFW_KEY_S)) moveBack(dt)
            if (window.getKeyState(GLFW.GLFW_KEY_D)) entity.rotate(0f, -2.3f * dt, 0f)

            if (window.getKeyState(GLFW.GLFW_KEY_SPACE)) moveUp(dt)
            if (window.getKeyState(GLFW.GLFW_KEY_LEFT_SHIFT)) moveDown(dt)
        }
    }

}
