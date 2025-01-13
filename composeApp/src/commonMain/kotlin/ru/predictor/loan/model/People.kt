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
    var products by MutableStateDelegate(0)
    
    var state by MutableStateDelegate(PeopleState.GOOD)

    var editSettings by MutableStateDelegate(false)

    var populationRate by MutableStateDelegate(1.1f)

    override fun tick() {
        super.tick()
        val needFood = ceil(population).toInt()
        
        if (products >= needFood) {
            products -= needFood
            population *= populationRate
        }
        else if (products in 1..< needFood) {
            products = 0
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
            products >= needFood -> PeopleState.GOOD
            products in 1..<needFood -> PeopleState.HANGER
            else -> PeopleState.DYING_OUT
        }
    }
}

fun Float.format(): String{
    if (this == 0f) return "-"

    return "${(this / 1).toInt()}"
}