package cga.exercise.components.gui

import cga.exercise.components.texture.Texture2D
import cga.exercise.game.Scene
import org.joml.Vector2f
import org.joml.Vector3f

class Timer(var timeInSekunden: Float) : GuiElement(background, defaultPosition, defaultSize) {

    companion object {

        val background = Texture2D.invoke("project/assets/textures/clock.png", true)
        val defaultSize = Vector2f(0.15f, 0.1f)
        val defaultPosition = Vector2f(-1 + defaultSize.x, -1 + defaultSize.y)

        val firstNumberPosition = Vector2f(-2.3f, 0f)
        val numberScale = Vector2f(0.4f, 1.5f)
        var youLooseSound = Scene.audioMaster.createAudioSource("project/assets/sounds/you_lose.ogg")

        init {
            youLooseSound.setVolume(0.1f)
        }

        val numberTextures = mutableListOf<Texture2D>(
            Texture2D.invoke("project/assets/textures/clock/0.png", true),
            Texture2D.invoke("project/assets/textures/clock/1.png", true),
            Texture2D.invoke("project/assets/textures/clock/2.png", true),
            Texture2D.invoke("project/assets/textures/clock/3.png", true),
            Texture2D.invoke("project/assets/textures/clock/4.png", true),
            Texture2D.invoke("project/assets/textures/clock/5.png", true),
            Texture2D.invoke("project/assets/textures/clock/6.png", true),
            Texture2D.invoke("project/assets/textures/clock/7.png", true),
            Texture2D.invoke("project/assets/textures/clock/8.png", true),
            Texture2D.invoke("project/assets/textures/clock/9.png", true)
        )

        val colon = GuiElement(Texture2D.invoke("project/assets/textures/clock/colon.png", true))
    }

    private var pauseTimer = false

    init {
        children.add(GuiElement(numberTextures[0]))
        children.add(GuiElement(numberTextures[0]))
        children.add(colon)
        children.add(GuiElement(numberTextures[0]))
        children.add(GuiElement(numberTextures[0]))

        children.forEachIndexed { index, guiElement ->
            guiElement.parent = this
            guiElement.translate(Vector2f(firstNumberPosition.x + 0.8f * index, firstNumberPosition.y))
            guiElement.scale(numberScale)
        }


    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)

        if (!pauseTimer) {
            timeInSekunden -= dt
        }
        displayTime()


    }

    fun displayTime() {
        if (timeInSekunden > 0) {
            var time = timeInSekunden  // 620
            val digit0 = (time / 600).toInt() // display 1
            time %= 600 // time ist 20
            val digit1 = (time / 60).toInt() //display 0
            time %= 60 // time ist immernoch 20
            val digit2 = (time / 10).toInt() //display 2
            time %= 10 // time ist 0
            val digit3 = time.toInt() //display0

            children[0].texID = numberTextures[digit0].texID
            children[1].texID = numberTextures[digit1].texID
            children[3].texID = numberTextures[digit2].texID
            children[4].texID = numberTextures[digit3].texID
        }
    }

    fun pause(changeTo: Boolean = !pauseTimer) {

        if (pauseTimer != changeTo && timeInSekunden <= 0f) {
            youLooseSound.play()
        }
//        println(pauseTimer)
        pauseTimer = changeTo
    }


}