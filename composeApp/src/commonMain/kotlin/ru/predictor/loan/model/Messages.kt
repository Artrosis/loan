package ru.predictor.loan.model

import androidx.compose.ui.Alignment

class Messages(var onNext: () -> Unit) {
    var messages by MutableStateDelegate(listOf(
        "Привет!",
        "Это игра про ссудный процент."
    ))
    
    var messagesAlignment by MutableStateDelegate(Alignment.Center)
    
    var buttonText by MutableStateDelegate("Начать")

    fun next() {
        onNext()
    }
}