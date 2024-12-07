package ru.predictor.loan.model

import androidx.compose.ui.Alignment

class Hint(
    var onConfirm: () -> Unit
) {
    var disable by MutableStateDelegate(false)
    
    var message by MutableStateDelegate(listOf<String>())

    var alignment by MutableStateDelegate(Alignment.Center)

    var buttonText by MutableStateDelegate("Понял")

    fun confirm() {
        onConfirm()
    }

    fun clear() {
        message = listOf()
    }
}
