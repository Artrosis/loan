package ru.predictor.loan

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import loaninterest.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import java.io.ByteArrayInputStream
import javax.sound.sampled.*
import javax.sound.sampled.AudioSystem.getAudioInputStream

actual class AudioPlayer actual constructor(val actualPath: String) {
    private var playing = false
    private var sdLine: SourceDataLine? = null


    @OptIn(ExperimentalResourceApi::class)
    actual suspend fun play() {
        withContext(Dispatchers.IO) {
            launch {
                playing = true

                val bytes = Res.readBytes(actualPath)
                val inputStream = getAudioInputStream(ByteArrayInputStream(bytes))
                val outFormat: AudioFormat = getOutFormat(inputStream.format)

                sdLine = AudioSystem.getLine(DataLine.Info(SourceDataLine::class.java, outFormat)) as SourceDataLine

                sdLine?.apply {
                    open(outFormat)
                    start()
                    stream(getAudioInputStream(outFormat, inputStream), this)
                }
            }
        }
    }

    private fun getOutFormat(inFormat: AudioFormat): AudioFormat {
        val ch = inFormat.channels
        val rate = inFormat.sampleRate
        return AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false)
    }

    private fun stream(`in`: AudioInputStream, line: SourceDataLine) {
        val buffer = ByteArray(65536)
        var n = 0
        while (n != -1 && playing) {
            line.write(buffer, 0, n)
            n = `in`.read(buffer, 0, buffer.size)
        }
    }

    actual fun pause() {
        if (playing) {
            playing = false
            sdLine?.stop()
        }
    }

    actual fun stop() {

        sdLine?.apply {
            if (isRunning || playing) {
                playing = false
                sdLine?.drain()
                sdLine?.stop()
            }
        }

    }

    actual val isPlaying: Boolean
        get() = sdLine?.isRunning ?: false
}