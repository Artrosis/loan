package ru.predictor.loan

actual class AudioPlayer actual constructor(actualPath: String) {
    actual suspend fun play() {
    }

    actual fun pause() {
    }

    actual fun stop() {
    }

    actual val isPlaying: Boolean
        get() = false
}