package cga.exercise.components.entities

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.entities.animals.*
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.game.Scene
import cga.framework.GameWindow
import org.joml.Random
import org.joml.Vector3f

class EntityManager(val camera: TronCamera, val scene: Scene) {

    var character: Character
    var drone: Drone

    var animals: MutableList<Entity> = mutableListOf()

    var player: Entity
    val random = Random()

    companion object {

        val animalMinSpawnDistance = 8
        val animalMaxSpawnDistance = 25

        val bullets = mutableListOf<Bullet>()

    }

    init {

        character = Character(scene.myMap)
        findSpawnpoint(character, 0, 5)


        drone = Drone(scene.myMap)
        findSpawnpoint(drone, 0,5)


//        camera.parent = character
        player = character

        for (i in 0..3) {
            val animal = Bear(scene.myMap)
            animals.add(animal)
            findSpawnpoint(animal, animalMinSpawnDistance, animalMaxSpawnDistance)
        }
        for (i in 0..10) {
            val animal = Deer(scene.myMap)
            animals.add(animal)
            findSpawnpoint(animal, animalMinSpawnDistance, animalMaxSpawnDistance)
        }
        for (i in 0..5) {
            val animal = Fox(scene.myMap)
            animals.add(animal)
            findSpawnpoint(animal, animalMinSpawnDistance, animalMaxSpawnDistance)
        }
        for (i in 0..10) {
            val animal = Gecko(scene.myMap)
            animals.add(animal)
            findSpawnpoint(animal, animalMinSpawnDistance, animalMaxSpawnDistance)
        }
        for (i in 0..10) {
            val animal = Hare(scene.myMap)
            animals.add(animal)
            findSpawnpoint(animal, animalMinSpawnDistance, animalMaxSpawnDistance)
        }
        for (i in 0..4) {
            val animal = MountainLion(scene.myMap)
            animals.add(animal)
            findSpawnpoint(animal, animalMinSpawnDistance, animalMaxSpawnDistance)
        }
        for (i in 0..4) {
            val animal = Opossum(scene.myMap)
            animals.add(animal)
            findSpawnpoint(animal, animalMinSpawnDistance, animalMaxSpawnDistance)
        }
        for (i in 0..4) {
            val animal = Raccoon(scene.myMap)
            animals.add(animal)
            findSpawnpoint(animal, animalMinSpawnDistance, animalMaxSpawnDistance)
        }
        for (i in 0..5) {
            val animal = Turkey(scene.myMap)
            animals.add(animal)
            findSpawnpoint(animal, animalMinSpawnDistance, animalMaxSpawnDistance)
        }


    }


    fun switchPlayer() {
        if (player == character) {
            player = drone
            character.isFirstPersonView = false
        } else {
            player = character
            character.isFirstPersonView = true
        }


    }


    fun render(dt: Float, time: Float, shaderProgram: ShaderProgram) {
        shaderProgram.use()

        scene.setNeededUniforms(shaderProgram)

        animals.forEach { it.render(shaderProgram) }

        drone.render(shaderProgram)

        character.render(shaderProgram)
        bullets.forEach { it.render(shaderProgram) }
    }

    fun update(window: GameWindow, dt: Float, time: Float) {
        drone.update(dt, time)
        camera.setPosition(character.getPosition().add(TronCamera.cameraOffset))

        animals.forEach {
            it.update(dt, time)

            if (!it.alive || it.getPosition().sub(player.getPosition()).length() > 2 * animalMaxSpawnDistance) {
                it.alive = true
                findSpawnpoint(it, animalMinSpawnDistance, animalMaxSpawnDistance)
//                println("moved ${it.javaClass.simpleName} to ${it.getPosition()}")
            }


        }

        character.update(dt, time)
        bullets.forEach {
            it.update(dt, time)
            if (it.movementController!!.distanceTraveled!!.length() > Bullet.maxDistance) {
                it.alive = false
            }
        }
        bullets.removeIf { t -> !t.alive }


        player.movementController?.inputControl(dt, time, window)

//        if (window.getKeyState(GLFW.GLFW_KEY_LEFT_SHIFT)) sniper.translate(Vector3f(0f, -5 * dt, 0f))
    }

    fun findSpawnpoint(entity: Entity, minDistance: Int, maxDistance: Int) {

        var pos = Vector3f()
        var rand1: Float
        var rand2: Float

        do {
            rand1 = (random.nextFloat() - 0.5f) * 2f
            rand2 = (random.nextFloat() - 0.5f) * 2f
            pos.x =
                rand1 * (maxDistance - minDistance) + if (rand1 < 0) -minDistance else +minDistance
            pos.z =
                rand2 * (maxDistance - minDistance) + if (rand2 < 0) -minDistance else +minDistance
            pos.y = entity.myMap.getHeight(pos.x, pos.z)

            entity.setPosition(entity.getPosition().add(pos))
        } while (entity.movementCollision())
    }


    fun cleanUp() {
        character.cleanUp()
        drone.cleanUp()
    }


}