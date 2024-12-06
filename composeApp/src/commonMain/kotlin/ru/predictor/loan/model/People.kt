package ru.predictor.loan.model

import androidx.compose.ui.graphics.Color
import kotlin.math.ceil
import kotlin.math.roundToInt

enum class PeopleState(val color: Color){
    GOOD(Color(0xA0FFFFFF)), 
    HANGER(Color(0xA0FFFF00)), 
    DYING_OUT(Color(0xA0FF0000))
}

class People(
    val onDie: () -> Unit,
){
    //Рост населения
    var population by MutableStateDelegate(0f)

    //Еда
    var food by MutableStateDelegate(0)
    
    var state by MutableStateDelegate(PeopleState.GOOD)
    
    var showMoney by MutableStateDelegate(false)
    var money by MutableStateDelegate(0)

    fun tick() {
        val needFood = ceil(population).toInt()
        
        if (food >= needFood) {
            food -= needFood
            population *= 1.1f
        }
        else if (food in 1..< needFood) {
            food = 0
        }
        else {
            population *= 0.9f
        }

        if (population < 1) {
            onDie()
            return
        }
        checkFood()
    }

    fun checkFood() {
        val needFood = ceil(population).toInt()
        state = when {
            food >= needFood -> PeopleState.GOOD
            food in 1..<needFood -> PeopleState.HANGER
            else -> PeopleState.DYING_OUT
        }
    }
}

fun Float.format(): String{
    if (this == 0f) return "-"

    if (this.rem(1) == 0f) return "${this.toInt()}"

    if (this.rem(0.1f) == 0f) return "${this.roundToDecimals(1)}"

    return "${this.roundToDecimals(2)}"
}

fun Float.roundToDecimals(decimals: Int): Float {
    var dotAt = 1
    repeat(decimals) { dotAt *= 10 }
    val roundedValue = (this * dotAt).roundToInt()
    return (roundedValue / dotAt) + (roundedValue % dotAt).toFloat() / dotAt
}