package cga.exercise.components.gui

import cga.exercise.components.entities.animals.*
import cga.exercise.components.texture.Texture2D
import cga.exercise.game.Scene
import org.joml.Vector2f
import kotlin.random.Random

class WantedPoster : GuiElement(background, defaultPosition, defaultSize) {

    companion object {
        val background = Texture2D.invoke("project/assets/textures/wanted.png", true)
        val defaultSize = Vector2f(0.18f, 0.4f)
        val defaultPosition = Vector2f(1 - defaultSize.x, 1 - defaultSize.y)

        val firstAnimalPosition = Vector2f(-0.42f, 0.25f)
        val tierScale = Vector2f(0.4f, 0.23f)

        val firstNumberPosition = Vector2f(-0.25f, 0.55f)
        val numberScale = Vector2f(0.1f, 0.1f)

        var youWinSound = Scene.audioMaster.createAudioSource("project/assets/sounds/you_win.ogg")
        var correctKillSound = Scene.audioMaster.createAudioSource("project/assets/sounds/correct_kill.ogg")
        var paperscrollSound = Scene.audioMaster.createAudioSource("project/assets/sounds/paperscroll.ogg")

        val possibleAnimals = mutableListOf<String>(
            "Bear",
            "Deer",
            "Fox",
            "Hare",
            "MountainLion",
            "Opossum",
            "Raccoon",
            "Turkey"
        )

        val possibleAnimalTextures = mutableListOf<Texture2D>(
            Bear.image,
            Deer.image,
            Fox.image,
            Hare.image,
            MountainLion.image,
            Opossum.image,
            Raccoon.image,
            Turkey.image
        )

        val possibleNumbers = mutableListOf(
            Texture2D.invoke("project/assets/textures/numbers/0.png", true),
            Texture2D.invoke("project/assets/textures/numbers/1.png", true),
            Texture2D.invoke("project/assets/textures/numbers/2.png", true),
            Texture2D.invoke("project/assets/textures/numbers/3.png", true),
            Texture2D.invoke("project/assets/textures/numbers/4.png", true),
            Texture2D.invoke("project/assets/textures/numbers/5.png", true),
            Texture2D.invoke("project/assets/textures/numbers/6.png", true)
        )

        val cross = Texture2D.invoke("project/assets/textures/cross.png", true)

        val killList = HashMap<String, Int>()

        init {
            youWinSound.setVolume(0.2f)
            correctKillSound.setVolume(0.5f)
            paperscrollSound.setVolume(0.1f)
        }


    }

    val targetAnimal = Random.nextInt(possibleAnimalTextures.size)
    val killCounter: Int
        get() {
//            println(killList[possibleAnimals[targetAnimal]] ?: 0)
            return killList[possibleAnimals[targetAnimal]] ?: 0
        }
    var lastKillCounter = 0

    init {
        childrenHinzufuegen(possibleAnimalTextures[targetAnimal])
        children.forEach { it.parent = this }
        //startkoordinaten von tier[0] und zahl[0] sind jeweils direkt oben definiert
        //kreuze zum durchstreichen
        children[1].translate(Vector2f(2.1f, 0f))
        children[2].translate(Vector2f(0f, -2.0f))
        children[3].translate(Vector2f(2.1f, -2.0f))
        children[4].translate(Vector2f(0f, -4.0f))
        children[5].translate(Vector2f(2.1f, -4.0f))

        //tiere
        children[7].translate(Vector2f(2.1f, 0f))
        children[8].translate(Vector2f(0f, -2.0f))
        children[9].translate(Vector2f(2.1f, -2.0f))
        children[10].translate(Vector2f(0f, -4.0f))
        children[11].translate(Vector2f(2.1f, -4.0f))

        //zahlen
        children[13].translate(Vector2f(2f, 0.0f))
        children[14].translate(Vector2f(4f, 0.0f))

        println("selected hunting target ${possibleAnimals[targetAnimal]}")
    }

    override fun toggle() {
        paperscrollSound.play()
        super.toggle()
    }

    fun childrenHinzufuegen(tierTexture: Texture2D) {
        children.add(GuiElement(cross, firstAnimalPosition, tierScale))
        children.add(GuiElement(cross, firstAnimalPosition, tierScale))
        children.add(GuiElement(cross, firstAnimalPosition, tierScale))
        children.add(GuiElement(cross, firstAnimalPosition, tierScale))
        children.add(GuiElement(cross, firstAnimalPosition, tierScale))
        children.add(GuiElement(cross, firstAnimalPosition, tierScale))
        for (child in children) child.disable()

        children.add(GuiElement(tierTexture, firstAnimalPosition, tierScale))
        children.add(GuiElement(tierTexture, firstAnimalPosition, tierScale))
        children.add(GuiElement(tierTexture, firstAnimalPosition, tierScale))
        children.add(GuiElement(tierTexture, firstAnimalPosition, tierScale))
        children.add(GuiElement(tierTexture, firstAnimalPosition, tierScale))
        children.add(GuiElement(tierTexture, firstAnimalPosition, tierScale))

        children.add(
            GuiElement(
                possibleNumbers[0],
                Vector2f(firstNumberPosition), Vector2f(numberScale)
            )
        )
        children.add(
            GuiElement(
                Texture2D.invoke("project/assets/textures/numbers/slash.png", false),
                Vector2f(firstNumberPosition),
                Vector2f(numberScale)
            )
        )
        children.add(
            GuiElement(
                possibleNumbers[6],
                Vector2f(firstNumberPosition),
                Vector2f(numberScale)
            )
        )
    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)

        if (killCounter != lastKillCounter) {
            if (killCounter in 0 .. 6) {

                correctKillSound.play()

                children[killCounter - 1].enable()
                children[12].texID = possibleNumbers[killCounter].texID
                lastKillCounter = killCounter

                if(killCounter == 6){
                    youWinSound.play()
                }



            }
        }
//        killList.forEach { (t, u) -> println("$t wurde bisher $u getoetet") }


    }


}