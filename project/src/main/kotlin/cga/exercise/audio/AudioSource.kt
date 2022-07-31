package cga.exercise.audio

import org.lwjgl.openal.AL10


class AudioSource(bufferID: Int) {

    private var sourceID: Int = -1

    init {
        sourceID = AL10.alGenSources()
        AL10.alSourcei(sourceID, AL10.AL_BUFFER, bufferID)
        AL10.alSourcePlay(sourceID)
    }

    fun play() {
        AL10.alSourcePlay(sourceID)
    }

    fun stop() {
        AL10.alSourceStop(sourceID)
    }

    fun cleanUp() {
        if (sourceID != -1) {
            stop()
            AL10.alDeleteBuffers(sourceID)
        }
    }


}