package ru.predictor.loan.model

import androidx.compose.ui.graphics.Color
import ru.predictor.loan.utils.MutableStateDelegate
import kotlin.math.ceil

enum class PeopleState(val color: Color){
    GOOD(Color(0xA0FFFFFF)), 
    HANGER(Color(0xA0FFFF00)), 
    DYING_OUT(Color(0xA0FF0000))
}

class People(
    val onDie: () -> Unit,
): Creditor(){
    //Рост населения
    var population by MutableStateDelegate(0f)

    //Еда
    var food by MutableStateDelegate(0)
    
    var state by MutableStateDelegate(PeopleState.GOOD)

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

    return "${(this / 1).toInt()}"
}