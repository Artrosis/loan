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
    val onLevelUp: () -> Unit,
){
    //Рост населения
    var population by MutableStateDelegate(0f)
    var maxLevelPopulation by MutableStateDelegate(0f)

    //Товары
    var products by MutableStateDelegate(0)
    
    var state by MutableStateDelegate(PeopleState.GOOD)
    
    var showMoney by MutableStateDelegate(false)
    var money by MutableStateDelegate(0)

    fun tick() {
        val needProducts = ceil(population).toInt()
        
        if (products >= needProducts) {
            products -= needProducts
            population *= 1.1f
            
            state = PeopleState.GOOD
        }
        else if (products in 1..needProducts) {
            products = 0
            state = PeopleState.HANGER
        }
        else {
            population *= 0.9f
            state = PeopleState.DYING_OUT
            
        }

        if (population < 1) {
            onDie()
            return
        }
        
        if (population >= maxLevelPopulation) {
            onLevelUp()
        }
    }
    
    private fun Float.format(): String{
        
        if (this == 0f) return "-"
        
        if (this.rem(1) == 0f) return "${this.toInt()}"

        if (this.rem(0.1f) == 0f) return "${this.roundToDecimals(1)}"      
        
        return "${this.roundToDecimals(2)}"
    }

    private fun Float.roundToDecimals(decimals: Int): Float {
        var dotAt = 1
        repeat(decimals) { dotAt *= 10 }
        val roundedValue = (this * dotAt).roundToInt()
        return (roundedValue / dotAt) + (roundedValue % dotAt).toFloat() / dotAt
    }
    
    fun populationProgress() = population / maxLevelPopulation
    fun populationText() = "${population.format()} / ${maxLevelPopulation.format()}"
}