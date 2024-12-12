package ru.predictor.loan.model

import ru.predictor.loan.utils.MutableStateDelegate

class Messages(
    var onNext: () -> Unit
) {
    var messages by MutableStateDelegate(listOf(
        "Привет!",
        "Это игра про ссудный процент и его влияние на экономику."
    ))
    
    fun clear(){
        messages = listOf()
    }
    
    var buttonText by MutableStateDelegate("Начать")

    fun next() {
        onNext()
    }
}