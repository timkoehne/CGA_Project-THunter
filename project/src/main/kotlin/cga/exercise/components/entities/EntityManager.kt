package cga.exercise.components.entities

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.game.Scene
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.Math
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW

class EntityManager(val camera: TronCamera, val scene: Scene) {

    var sniper: Sniper
    var drone: Drone

    var testReh: MutableList<Entity> = mutableListOf()

    init {

        for (i in 0..50) {
            testReh.add(Hare(scene.myMap))
        }

        drone = Drone(scene.myMap)
        drone.translate(Vector3f(5f, scene.myMap.getHeight(5f, 5f) + 1f, 5f))

        sniper = Sniper(scene.myMap)
//        sniper.translate(Vector3f(5f, myMap.getHeight(5f, 5f) + 1, 5f))
        sniper.scale(Vector3f(0.5f))
        camera.parent = sniper
    }

    fun getPlayer(): Entity {
        return sniper
    }

    fun render(dt: Float, time: Float, shaderProgram: ShaderProgram) {
        shaderProgram.use()

        scene.setNeededUniforms(shaderProgram)

        testReh.forEach { it.render(shaderProgram) }


        drone.render(shaderProgram)
        sniper.render(shaderProgram)
    }

    fun update(window: GameWindow, dt: Float, time: Float) {
        drone.update(dt, time)

        testReh.forEach { it.update(dt, time) }


        sniper.update(dt, time)



        if (window.getKeyState(GLFW.GLFW_KEY_N)) drone.open(time)
        if (window.getKeyState(GLFW.GLFW_KEY_M)) drone.close(time)

        if (window.getKeyState(GLFW.GLFW_KEY_W)) sniper.translate(Vector3f(0f, 0f, -5 * dt))
        if (window.getKeyState(GLFW.GLFW_KEY_S)) sniper.translate(Vector3f(0f, 0f, 5 * dt))
        if (window.getKeyState(GLFW.GLFW_KEY_A)) sniper.translate(Vector3f(-5 * dt, 0f, 0f))
        if (window.getKeyState(GLFW.GLFW_KEY_D)) sniper.translate(Vector3f(5 * dt, 0f, 0f))

        if (window.getKeyState(GLFW.GLFW_KEY_SPACE)) sniper.jump(dt, time)
//        if (window.getKeyState(GLFW.GLFW_KEY_LEFT_SHIFT)) sniper.translate(Vector3f(0f, -5 * dt, 0f))


    }

    fun cleanUp() {
        sniper.cleanUp()
        drone.cleanUp()
    }


}