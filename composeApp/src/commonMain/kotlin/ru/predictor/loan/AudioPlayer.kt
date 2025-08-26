package ru.predictor.loan

expect class AudioPlayer(actualPath: String) {
    suspend fun play()
    fun pause()
    fun stop()
    val isPlaying: Boolean
}