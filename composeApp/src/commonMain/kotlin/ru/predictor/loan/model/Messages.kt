package ru.predictor.loan.model

import ru.predictor.loan.utils.MutableStateDelegate

class Messages(
    var onNext: () -> Unit
) {
    var lines by MutableStateDelegate(listOf(
        "Привет!",
        " ",
        "Эта игра покажет зачем обществу нужны деньги, банки и что будет,",
        "если банк будет выдавать деньги только под проценты.",
        " ",
        " ",
        "Для отзывов: https://t.me/c/2363429679/769",
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