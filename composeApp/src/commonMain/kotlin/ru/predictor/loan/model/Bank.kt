package ru.predictor.loan.model

import ru.predictor.loan.utils.MutableStateDelegate

class Bank(
    val onClick: () -> Unit,
    val getMoneyCount: () -> Int,
    val getProductsData: () -> Map<String, Int>,
){
    //Видимость
    var has by MutableStateDelegate(false)
    
    //Ссудный процент
    var showLoanInterest by MutableStateDelegate(false)
    var loanInterest by MutableStateDelegate(1.0)
    
    var money by MutableStateDelegate(0)

    fun emmitMoney() {        
        val productCount = getProductsData().values.sum()
        
        val needMoney = productCount - getMoneyCount()
        
        if (needMoney < 0) throw Exception("На текущем этапе количество товаров, а следовательно и денег в экономике всегда увеличивается.")
        
        money += needMoney
    }

    fun click() {
        onClick()
    }
}