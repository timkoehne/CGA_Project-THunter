package cga.exercise.components.entities

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.game.Scene
import cga.framework.GameWindow
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW

class EntityManager(val camera: TronCamera, val scene: Scene) {

    var character: Character
    var drone: Drone

    var testReh: MutableList<Entity> = mutableListOf()
    var testFox: Entity = Fox(scene.myMap)

    var player: Entity


    init {
        for (i in 0..50) {
            testReh.add(Raccoon(scene.myMap))
        }

        testFox.rotate(0f, Math.toRadians(90.0).toFloat(), 0f)


        drone = Drone(scene.myMap)
        drone.translate(Vector3f(5f, scene.myMap.getHeight(5f, 5f) + 1f, 5f))

        character = Character(scene.myMap)
        character.translate(Vector3f(5f, scene.myMap.getHeight(5f, 5f) + 2, 5f))
        camera.parent = character

        player = character


    }

    fun switchPlayer() {
        if (player == character) {
            player = drone
            character.isFirstPersonView = false
//            camera.parent = drone
        } else {
            player = character
            character.isFirstPersonView = true
//            camera.parent = sniper
        }


    }


    fun render(dt: Float, time: Float, shaderProgram: ShaderProgram) {
        shaderProgram.use()

        scene.setNeededUniforms(shaderProgram)

        testReh.forEach { it.render(shaderProgram) }
        testFox.render(shaderProgram)


        drone.render(shaderProgram)

        character.render(shaderProgram)


    }

    fun update(window: GameWindow, dt: Float, time: Float) {
        drone.update(dt, time)

        testReh.forEach { it.update(dt, time) }
        testFox.update(dt, time)


        character.update(dt, time)

        player.movementControl(dt, time, window)

        if (window.getKeyState(GLFW.GLFW_KEY_N)) drone.open(time)
        if (window.getKeyState(GLFW.GLFW_KEY_M)) drone.close(time)

//        if (window.getKeyState(GLFW.GLFW_KEY_LEFT_SHIFT)) sniper.translate(Vector3f(0f, -5 * dt, 0f))


    }

    fun cleanUp() {
        character.cleanUp()
        drone.cleanUp()
    }


}