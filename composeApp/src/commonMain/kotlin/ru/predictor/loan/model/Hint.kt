package ru.predictor.loan.model

import androidx.compose.ui.Alignment
import ru.predictor.loan.utils.MutableStateDelegate

class Hint(
    var onConfirm: () -> Unit
) {
    var disable by MutableStateDelegate(false)
    var message by MutableStateDelegate(listOf<String>())
    var alignment by MutableStateDelegate(Alignment.Center)
    var buttonText by MutableStateDelegate("Понятно")

    fun confirm() {
        onConfirm()
    }

    fun clear() {
        message = listOf()
    }

    fun isShow(): Boolean = !(disable || message.isEmpty())
}
