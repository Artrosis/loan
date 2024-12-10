package ru.predictor.loan.model

import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.forest
import loaninterest.composeapp.generated.resources.forge
import loaninterest.composeapp.generated.resources.manuf

class Manufacture(
    val onClick: () -> Unit,
    val getAge: () -> Age,
){    
    var products by MutableStateDelegate(0)
    var showMoney by MutableStateDelegate(false)
    var money by MutableStateDelegate(0)
    var showPrice by MutableStateDelegate(false)
    var price by MutableStateDelegate(0)
    var showSalary by MutableStateDelegate(false)
    var salary by MutableStateDelegate(0)

    private val forestIcon = Res.drawable.forest
    private val forgeIcon = Res.drawable.forge
    private val manufactureIcon = Res.drawable.manuf
    
    fun getIcon() = when(getAge()) {
        Age.INDEPENDENT -> forestIcon
        Age.BARTER -> forgeIcon
        Age.INDUSTRY -> manufactureIcon
        Age.FINISH -> manufactureIcon
    }

    fun nextAddProduct(workersCount: Int) = workersCount * 2

    fun click() {
        onClick()
    }

    fun takeProducts(): Int {
        val result = products
        products = 0

        return result
    }
}