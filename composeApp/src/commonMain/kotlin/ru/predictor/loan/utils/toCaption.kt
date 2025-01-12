package ru.predictor.loan.utils

fun Int.toCaption(): String {
    var value = this

    while (value > 999) {
        value /= 1000
    }

    return value.toString() + this.findPostfix()
}

fun Double.toCaption(): String {    
    return this.toInt().toCaption()
}

private fun Int.findPostfix(): String {
    if (this > 999_999) {
        return "M"
    }
    
    if (this > 999) {
        return "K"
    }

    return ""
}