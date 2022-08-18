package cga.exercise.components.gui

import cga.exercise.components.texture.Texture2D
import cga.exercise.game.Scene
import org.joml.Vector2f

class ControlDisplay : GuiElement(texture, Vector2f(), Vector2f(1f, 1f)) {

    companion object {

        val texture = Texture2D.invoke("project/assets/textures/steuerung.png", true)
        var floorboardSound = Scene.audioMaster.createAudioSource("project/assets/sounds/floorboard.ogg")

        init {
            floorboardSound.setVolume(0.1f)
        }
    }

    override fun toggle() {
        floorboardSound.play()
        super.toggle()
    }

}