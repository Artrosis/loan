package ru.predictor.loan.model

class Market{
    //Видимость
    var has by MutableStateDelegate(false)
    var money by MutableStateDelegate(0)
    var products by MutableStateDelegate(0)
    var price by MutableStateDelegate(0)

    fun tick() {
        if (!has) return
        money += 10
    }
}