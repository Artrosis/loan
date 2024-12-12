package ru.predictor.loan.utils

import kotlin.reflect.KProperty

class ObservableStateDelegate<T>(
    value: T,
    val onSet: (newValue: T) -> Unit,
): MutableStateDelegate<T>(value) {
    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        super.setValue(thisRef, property, value)
        onSet(value)
    }
}