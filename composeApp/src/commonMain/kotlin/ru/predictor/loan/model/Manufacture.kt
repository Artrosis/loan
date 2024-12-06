package ru.predictor.loan.model

import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.compose_multiplatform
import loaninterest.composeapp.generated.resources.manuf

class Manufacture(
    val onClick: () -> Unit,
    val onGetPopulation: () -> Int,
    val getAge: () -> Age,
){    
    var products by MutableStateDelegate(0)
    var showMoney by MutableStateDelegate(false)
    var money by MutableStateDelegate(0)
    var showPrice by MutableStateDelegate(false)
    var price by MutableStateDelegate(0)
    var showSalary by MutableStateDelegate(false)
    var salary by MutableStateDelegate(0)

    private val forestIcon = Res.drawable.manuf
    private val forgeIcon = Res.drawable.compose_multiplatform
    private val manufactureIcon = Res.drawable.manuf
    
    fun getIcon() = when(getAge()) {
        Age.INDEPENDENT -> forestIcon
        Age.BARTER -> forgeIcon
        Age.INDUSTRY -> manufactureIcon
        Age.FINISH -> manufactureIcon
    }

    fun tick() {
        products += nextAddProduct()
    }

    fun nextAddProduct() = onGetPopulation() * 2

    fun click() {
        onClick()
    }

    fun takeProducts(): Int {
        val result = products
        products = 0

        return result
    }
}