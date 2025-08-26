package ru.predictor.loan

import loaninterest.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.w3c.dom.Audio

@OptIn(ExperimentalResourceApi::class)
actual class AudioPlayer actual constructor(actualPath: String) {
    private val audio = Audio()

    init {
        // Указываем путь к файлу (может быть URL)
        audio.src = Res.getUri(actualPath)
        audio.load()
    }

    actual fun pause() = audio.pause()

    actual fun stop() {
        audio.pause()
        audio.currentTime = 0.0
    }

    actual val isPlaying: Boolean
        get() = !audio.paused && audio.readyState >= 2 // HAVE_CURRENT_DATA

    actual suspend fun play() {
        audio.play()
    }
}
