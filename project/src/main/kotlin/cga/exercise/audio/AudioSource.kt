package cga.exercise.audio

import org.lwjgl.openal.AL10
import org.lwjgl.openal.AL10.AL_FALSE
import org.lwjgl.openal.AL10.AL_TRUE


class AudioSource(bufferID: Int) {

    private var sourceID: Int = -1

    init {
        sourceID = AL10.alGenSources()
        AL10.alSourcei(sourceID, AL10.AL_BUFFER, bufferID)
    }

    fun play() {
        AL10.alSourcePlay(sourceID)
    }

    fun stop() {
        AL10.alSourceStop(sourceID)
    }

    fun turnOnLooping(){
        AL10.alSourcei(sourceID, AL10.AL_LOOPING, AL_TRUE)
    }
    fun turnOffLooping(){
        AL10.alSourcei(sourceID, AL10.AL_LOOPING, AL_FALSE)
    }

    fun cleanUp() {
        if (sourceID != -1) {
            stop()
            AL10.alDeleteBuffers(sourceID)
        }
    }


}