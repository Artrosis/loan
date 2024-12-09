package ru.predictor.loan.model

import loaninterest.composeapp.generated.resources.*
import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.market

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

    private val marketIcon = Res.drawable.market
    private val shopIcon = Res.drawable.shop

    fun getIcon() = when(getAge()) {
        Age.INDEPENDENT -> throw Exception("На этапе Самообеспечение нет рынка")
        Age.BARTER -> marketIcon
        Age.INDUSTRY -> shopIcon
        Age.FINISH -> shopIcon
    }

    fun takeProducts(): Int {
        val result = products
        products = 0

        return result
    }
}