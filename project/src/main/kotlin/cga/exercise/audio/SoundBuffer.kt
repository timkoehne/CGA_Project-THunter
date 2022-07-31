package cga.exercise.audio

import org.lwjgl.openal.AL10
import org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.libc.LibCStdlib.free

class SoundBuffer(val filepath: String) {

    var bufferID: Int = -1

    init {

        try {
            bufferID = AL10.alGenBuffers()
            val stack = stackPush()
            //Allocate space to store return information from the function
            val channelsBuffer = stack.mallocInt(1)
            val sampleRateBuffer = stack.mallocInt(1)

            val rawAudioBuffer = stb_vorbis_decode_filename(filepath, channelsBuffer, sampleRateBuffer)
                ?: throw Exception("cannot load soundfile")

            //Retreive the extra information that was stored in the buffers by the function
            val channels = channelsBuffer.get(0)
            val sampleRate = sampleRateBuffer.get(0)

            var format = -1
            if (channels == 1) {
                format = AL10.AL_FORMAT_MONO16;
            } else if (channels == 2) {
                format = AL10.AL_FORMAT_STEREO16;
            }

            AL10.alBufferData(bufferID, format, rawAudioBuffer, sampleRate)
            free(rawAudioBuffer)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cleanUp() {
        if(bufferID != -1) AL10.alDeleteBuffers(bufferID)
    }
}