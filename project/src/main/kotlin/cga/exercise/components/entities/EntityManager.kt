package cga.exercise.components.entities

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.map.MyMap
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.Math
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW

class EntityManager(val camera: TronCamera, val myMap: MyMap) {
    private var trees: MutableList<Entity>
    private var animals: MutableList<Entity>
    private var cabins: MutableList<Entity>
    private var dekostuff: MutableList<Entity>

    var sniper: Sniper
    var drone: Drone

    init {

        trees = mutableListOf(
            Entity(ModelLoader.loadModel("project/assets/trees/tree1.1.obj", 0f, 0f, 0f)),
            Entity(ModelLoader.loadModel("project/assets/trees/tree1.1rot.obj", 0f, 0f, 0f)),
            Entity(ModelLoader.loadModel("project/assets/trees/tree1.obj", 0f, 0f, 0f)),
            Entity(ModelLoader.loadModel("project/assets/trees/tree1rot.obj", 0f, 0f, 0f)),
            Entity(ModelLoader.loadModel("project/assets/trees/tree5.obj", 0f, 0f, 0f)),
            Entity(ModelLoader.loadModel("project/assets/trees/tree6.obj", 0f, 0f, 0f)),
            Entity(ModelLoader.loadModel("project/assets/trees/tree9.obj", 0f, 0f, 0f)),
            Entity(ModelLoader.loadModel("project/assets/trees/tree10.obj", 0f, 0f, 0f)),
            Entity(ModelLoader.loadModel("project/assets/trees/tree11.obj", 0f, 0f, 0f))
        )
        trees.forEachIndexed { index, renderable ->
            renderable.translate(Vector3f(index * 5f, myMap.getHeight(index * 5f, 30f), 30f))
        }

        dekostuff = mutableListOf(
            Entity(ModelLoader.loadModel("project/assets/deko/bigStone.obj", 0f, 0f, 0f)),
            Entity(ModelLoader.loadModel("project/assets/deko/brownMushroom.obj", 0f, 0f, 0f)),
            Entity(ModelLoader.loadModel("project/assets/deko/etwasKleinererStein.obj", 0f, 0f, 0f)),
            Entity(ModelLoader.loadModel("project/assets/deko/grassBueschel.obj", 0f, 0f, 0f)),
            Entity(ModelLoader.loadModel("project/assets/deko/redMushroom.obj", 0f, 0f, 0f)),
            Entity(ModelLoader.loadModel("project/assets/deko/stock.obj", 0f, 0f, 0f))
        )
        dekostuff.forEachIndexed { index, renderable ->
            renderable.translate(Vector3f(index * 5f, myMap.getHeight(index * 5f, 15f) + 1f, 15f))
        }


        animals = mutableListOf(
            Entity(ModelLoader.loadModel("project/assets/animals/bear.obj", 0f, Math.toRadians(180f), 0f)),
            Entity(ModelLoader.loadModel("project/assets/animals/deerFemale.obj", 0f, Math.toRadians(180f), 0f)),
            Entity(ModelLoader.loadModel("project/assets/animals/deerMale.obj", 0f, Math.toRadians(180f), 0f)),
            Entity(ModelLoader.loadModel("project/assets/animals/fox.obj", 0f, Math.toRadians(180f), 0f)),
            Entity(ModelLoader.loadModel("project/assets/animals/gecko.obj", 0f, Math.toRadians(180f), 0f)),
            Entity(ModelLoader.loadModel("project/assets/animals/hare.obj", 0f, Math.toRadians(180f), 0f)),
            Entity(ModelLoader.loadModel("project/assets/animals/mountainLion.obj", 0f, Math.toRadians(180f), 0f)),
            Entity(ModelLoader.loadModel("project/assets/animals/opossum.obj", 0f, Math.toRadians(180f), 0f)),
            Entity(ModelLoader.loadModel("project/assets/animals/racoon.obj", 0f, Math.toRadians(180f), 0f)),
            Entity(ModelLoader.loadModel("project/assets/animals/turkey.obj", 0f, Math.toRadians(180f), 0f))
        )
        animals.forEachIndexed { index, renderable ->
            renderable.translate(Vector3f(index * 3f, myMap.getHeight(index * 3f, 20f) + 1, 20f))
        }

        cabins = mutableListOf(
            Entity(ModelLoader.loadModel("project/assets/cabins/lowPolyLogCabin.obj", 0f, 0f, 0f))
        )
        cabins.forEachIndexed { index, renderable ->
            renderable.translate(Vector3f(index * 20f, myMap.getHeight(index * 20f, 60f), 60f))
        }

        drone = Drone()
        drone.translate(Vector3f(5f, myMap.getHeight(5f, 5f) + 1f, 5f))

        sniper = Sniper()
//        sniper.translate(Vector3f(5f, myMap.getHeight(5f, 5f) + 1, 5f))
        sniper.scale(Vector3f(0.5f))
        camera.parent = sniper
    }

    fun getPlayer(): Entity {
        return sniper
    }

    fun render(dt: Float, time: Float, shaderProgram: ShaderProgram) {
        shaderProgram.use()

        drone.render(shaderProgram)
        trees.forEach { it.render(shaderProgram) }
        dekostuff.forEach { it.render(shaderProgram) }
        animals.forEach { it.render(shaderProgram) }
        cabins.forEach { it.render(shaderProgram) }
        sniper.render(shaderProgram)
    }

    fun update(window: GameWindow, dt: Float, time: Float) {
        drone.update(dt, time)

        trees.forEach { it.rotate(0f, dt, 0f) }

        if (window.getKeyState(GLFW.GLFW_KEY_N)) drone.open(time)
        if (window.getKeyState(GLFW.GLFW_KEY_M)) drone.close(time)

        if (window.getKeyState(GLFW.GLFW_KEY_W)) sniper.translate(Vector3f(0f, 0f, -5 * dt))
        if (window.getKeyState(GLFW.GLFW_KEY_S)) sniper.translate(Vector3f(0f, 0f, 5 * dt))
        if (window.getKeyState(GLFW.GLFW_KEY_A)) sniper.translate(Vector3f(-5 * dt, 0f, 0f))
        if (window.getKeyState(GLFW.GLFW_KEY_D)) sniper.translate(Vector3f(5 * dt, 0f, 0f))

//        if (window.getKeyState(GLFW.GLFW_KEY_SPACE)) sniper.translate(Vector3f(0f, 5 * dt, 0f))
//        if (window.getKeyState(GLFW.GLFW_KEY_LEFT_SHIFT)) sniper.translate(Vector3f(0f, -5 * dt, 0f))

        sniper.translate(
            Vector3f(
                0f, myMap.getHeight(
                    sniper!!.getPosition().x, sniper!!.getPosition().z
                ) - sniper!!.getPosition()!!.y + 1, 0f
            )
        )


    }

    fun cleanUp() {
        trees.forEach { it.cleanUp() }
        animals.forEach { it.cleanUp() }
        cabins.forEach { it.cleanUp() }
        dekostuff.forEach { it.cleanUp() }
        sniper.cleanUp()
        drone.cleanUp()
    }


}