package ru.predictor.loan.model

class Messages(var onNext: () -> Unit) {
    var messages by MutableStateDelegate(listOf(
        "Привет!",
        "Это игра про ссудный процент."
    ))
    
    fun clear(){
        messages = listOf()
    }
    
    var buttonText by MutableStateDelegate("Начать")

    fun next() {
        onNext()
    }
}