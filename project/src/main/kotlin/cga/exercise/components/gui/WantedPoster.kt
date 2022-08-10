package cga.exercise.components.gui

import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f
import kotlin.random.Random

class WantedPoster : GuiElement(background, defaultPosition, defaultSize) {

    companion object {
        val background = Texture2D.invoke("project/assets/textures/wanted.png", true)
        val defaultSize = Vector2f(0.4f * 0.5f, 0.5f)
        val defaultPosition = Vector2f(1 - defaultSize.x, 1 - defaultSize.y)

        val firstAnimalPosition = Vector2f(-0.38f, 0.25f)
        val tierScale = Vector2f(0.5f, 0.22f)

        val firstNumberPosition = Vector2f(-0.25f, 0.55f)
        val numberScale = Vector2f(0.1f, 0.1f)

        val possibleAnimals = mutableListOf<Texture2D>(
            Texture2D.invoke("project/assets/animals/pictures/bear.png", true),
            Texture2D.invoke("project/assets/animals/pictures/deerFemale.png", true),
            Texture2D.invoke("project/assets/animals/pictures/deerMale.png", true),
            Texture2D.invoke("project/assets/animals/pictures/fox.png", true),
            Texture2D.invoke("project/assets/animals/pictures/hare.png", true),
            Texture2D.invoke("project/assets/animals/pictures/mountainLion.png", true),
            Texture2D.invoke("project/assets/animals/pictures/opossum.png", true),
            Texture2D.invoke("project/assets/animals/pictures/racoon.png", true),
            Texture2D.invoke("project/assets/animals/pictures/turkey.png", true)
        )

        val cross = Texture2D.invoke("project/assets/textures/cross.png", true)

    }

    var killCounter: Int = 0


    init {
        childrenHinzufuegen(possibleAnimals[Random.nextInt(possibleAnimals.size)])

        children.forEach { it.parent = this }
        //startkoordinaten von tier[0] und zahl[0] sind jeweils direkt oben definiert
        //kreuze zum durchstreichen
        children[1].translate(Vector2f(1.3f, 0f))
        children[2].translate(Vector2f(0f, -2.0f))
        children[3].translate(Vector2f(1.3f, -2.0f))
        children[4].translate(Vector2f(0f, -4.0f))
        children[5].translate(Vector2f(1.3f, -4.0f))

        //tiere
        children[7].translate(Vector2f(1.3f, 0f))
        children[8].translate(Vector2f(0f, -2.0f))
        children[9].translate(Vector2f(1.3f, -2.0f))
        children[10].translate(Vector2f(0f, -4.0f))
        children[11].translate(Vector2f(1.3f, -4.0f))

        //zahlen
        children[13].translate(Vector2f(2f, 0.0f))
        children[14].translate(Vector2f(4f, 0.0f))

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
                Texture2D.invoke("project/assets/textures/numbers/0.png", false),
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
                Texture2D.invoke("project/assets/textures/numbers/6.png", false),
                Vector2f(firstNumberPosition),
                Vector2f(numberScale)
            )
        )
    }

    fun hit() {
        if (killCounter in 0 until 6) {
            children[killCounter].enable()
            killCounter++
        }
    }


}