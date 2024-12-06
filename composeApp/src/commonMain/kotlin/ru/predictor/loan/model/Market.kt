package ru.predictor.loan.model

import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.compose_multiplatform
import loaninterest.composeapp.generated.resources.manuf

class Market(
    val getAge: () -> Age,
){
    //Видимость
    var has by MutableStateDelegate(false)
    var showMoney by MutableStateDelegate(false)
    var money by MutableStateDelegate(0)
    var products by MutableStateDelegate(0)
    var showPrice by MutableStateDelegate(false)
    var price by MutableStateDelegate(0)

    fun tick() {
        if (!has) return
        money += 10
    }

    private val marketIcon = Res.drawable.manuf
    private val shopIcon = Res.drawable.compose_multiplatform

    fun getIcon() = when(getAge()) {
        Age.INDEPENDENT -> throw Exception("На этапе Самообеспечение нет рынка")
        Age.BARTER -> marketIcon
        Age.INDUSTRY -> shopIcon
        Age.FINISH -> shopIcon
    }
}