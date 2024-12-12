package ru.predictor.loan.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlin.reflect.KProperty

open class MutableStateDelegate<T>(value: T) {
    
    private val state: MutableState<T> = mutableStateOf(value)
    
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return state.value
    }
    open operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        state.value = value
    }
}