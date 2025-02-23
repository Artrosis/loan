package ru.predictor.loan.model

import loaninterest.composeapp.generated.resources.*
import loaninterest.composeapp.generated.resources.Res
import ru.predictor.loan.utils.MutableStateDelegate

class Bank(
    val onClick: () -> Unit,
    val getAge: () -> Age,
    val getMoneyCount: () -> Double,
    val getProductsData: () -> Map<String, Int>,
): ViewObject(){
    //Видимость
    var has by MutableStateDelegate(false)

    var editSettings by MutableStateDelegate(false)
    var loanSize by MutableStateDelegate(10000)
    
    //Ссудный процент
    var showLoanInterest by MutableStateDelegate(false)
    var loanInterest by MutableStateDelegate(8)
    
    var money by MutableStateDelegate(0.0)
    var showMoney by MutableStateDelegate(true)

    fun emmitMoney() {        
        val productCount = getProductsData().values.sum()
        
        val needMoney = productCount - getMoneyCount()
        
        if (needMoney < 0) throw Exception("На текущем этапе количество товаров, а следовательно и денег в экономике всегда увеличивается.")
        
        money += needMoney
    }

    fun getIcon() = when (getAge()) {
        Age.INDEPENDENT -> throw Exception("На этапе Самообеспечение нет банка")
        Age.BARTER -> throw Exception("На этапе Бартера нет банка")
        Age.INDUSTRY -> Res.drawable.level_3_bank
        Age.CREDITING -> Res.drawable.level_4_bank
        Age.FINISH -> Res.drawable.level_3_bank
    }

    fun distributeMoney() {
        onClick()
    }
}