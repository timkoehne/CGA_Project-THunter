package cga.exercise.components.entities

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.game.Scene
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.Math
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW

class EntityManager(val camera: TronCamera, val scene: Scene) {
    private var animals: MutableList<Entity>
    private var cabins: MutableList<Entity>

    var sniper: Sniper
    var drone: Drone

    var testReh: Deer


    init {


        testReh = Deer(scene.myMap)

        animals = mutableListOf(
            Entity(ModelLoader.loadModel("project/assets/animals/bear.obj", 0f, Math.toRadians(180f), 0f), scene.myMap),
            Entity(
                ModelLoader.loadModel("project/assets/animals/deerFemale.obj", 0f, Math.toRadians(180f), 0f),
                scene.myMap
            ),
            Entity(
                ModelLoader.loadModel("project/assets/animals/deerMale.obj", 0f, Math.toRadians(180f), 0f),
                scene.myMap
            ),
            Entity(ModelLoader.loadModel("project/assets/animals/fox.obj", 0f, Math.toRadians(180f), 0f), scene.myMap),
            Entity(
                ModelLoader.loadModel("project/assets/animals/gecko.obj", 0f, Math.toRadians(180f), 0f),
                scene.myMap
            ),
            Entity(ModelLoader.loadModel("project/assets/animals/hare.obj", 0f, Math.toRadians(180f), 0f), scene.myMap),
            Entity(
                ModelLoader.loadModel("project/assets/animals/mountainLion.obj", 0f, Math.toRadians(180f), 0f),
                scene.myMap
            ),
            Entity(
                ModelLoader.loadModel("project/assets/animals/opossum.obj", 0f, Math.toRadians(180f), 0f),
                scene.myMap
            ),
            Entity(
                ModelLoader.loadModel("project/assets/animals/racoon.obj", 0f, Math.toRadians(180f), 0f),
                scene.myMap
            ),
            Entity(
                ModelLoader.loadModel("project/assets/animals/turkey.obj", 0f, Math.toRadians(180f), 0f),
                scene.myMap
            )
        )
        animals.forEachIndexed { index, renderable ->
            renderable.translate(Vector3f(index * 3f, scene.myMap.getHeight(index * 3f, 20f) + 1, 20f))
        }

        cabins = mutableListOf(
            Entity(ModelLoader.loadModel("project/assets/cabins/lowPolyLogCabin.obj", 0f, 0f, 0f), scene.myMap)
        )
        cabins.forEachIndexed { index, renderable ->
            renderable.translate(Vector3f(index * 20f, scene.myMap.getHeight(index * 20f, 60f), 60f))
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

        testReh.render(shaderProgram)


        drone.render(shaderProgram)
        animals.forEach { it.render(shaderProgram) }
        cabins.forEach { it.render(shaderProgram) }
        sniper.render(shaderProgram)
    }

    fun update(window: GameWindow, dt: Float, time: Float) {
        drone.update(dt, time)

        testReh.update(dt, time)


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
        animals.forEach { it.cleanUp() }
        cabins.forEach { it.cleanUp() }
        sniper.cleanUp()
        drone.cleanUp()
    }


}