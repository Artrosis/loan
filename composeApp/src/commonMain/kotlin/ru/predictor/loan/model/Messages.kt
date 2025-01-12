package ru.predictor.loan.model

import ru.predictor.loan.utils.MutableStateDelegate

class Messages(
    var onNext: () -> Unit
) {
    var lines by MutableStateDelegate(listOf(
        "Привет!",
        "Это игра про ссудный процент и его влияние на экономику."
    ))

    var closeDismiss by MutableStateDelegate(false)
    
    fun clear(){
        closeDismiss = false
        lines = listOf()
    }
    
    var buttonText by MutableStateDelegate("Начать")

    fun next() {
        onNext()
    }


}