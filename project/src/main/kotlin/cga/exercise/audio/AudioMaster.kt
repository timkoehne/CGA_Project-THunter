package cga.exercise.audio

import org.lwjgl.openal.*
import org.lwjgl.system.MemoryUtil


class AudioMaster {
    private val soundBufferList = mutableListOf<SoundBuffer>()
    private val audioSourcesList = mutableListOf<AudioSource>()

    private val defaultDeviceName: String? = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER)
    private var device: Long = -1L
    private var context: Long = -1L


    init {
        device = ALC10.alcOpenDevice(defaultDeviceName)

        val attributes = intArrayOf(0)
        context = ALC10.alcCreateContext(device, attributes)
        ALC10.alcMakeContextCurrent(context)

        val alcCapabilities = ALC.createCapabilities(device)
        val alCapabilities = AL.createCapabilities(alcCapabilities)
    }

    fun createAudioSource(filepath: String): AudioSource {
        var bufferID: Int = -1
        for (soundBuffer in soundBufferList) {
            if (soundBuffer.filepath == filepath) {
                bufferID = soundBuffer.bufferID
            }
        }

        if (bufferID == -1) {
            val newBuffer = SoundBuffer(filepath)
            soundBufferList.add(newBuffer)
            bufferID = newBuffer.bufferID
        }

        return AudioSource(bufferID)
    }

    fun cleanUp() {
        for (soundBuffer in soundBufferList) {
            soundBuffer.cleanUp()
        }
        for (audioSource in audioSourcesList) {
            audioSource.cleanUp()
        }
        ALC10.alcMakeContextCurrent(MemoryUtil.NULL)
        if (context != -1L) ALC10.alcDestroyContext(context)
        if (device != -1L) ALC10.alcCloseDevice(device)
    }


}