package ru.predictor.loan.model

class Bank{
    //Видимость
    var has by MutableStateDelegate(false)
    
    //Ссудный процент
    var loanInterest by MutableStateDelegate(1.0)
    
    var money by MutableStateDelegate(0)

    fun tick() {
        if (!has) return
        money += 10
    }
}