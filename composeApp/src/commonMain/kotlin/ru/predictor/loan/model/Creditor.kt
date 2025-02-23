package ru.predictor.loan.model

import ru.predictor.loan.utils.MutableStateDelegate

open class Creditor: ViewObject(){
    var showCredit by MutableStateDelegate(false)
    private var credits by MutableStateDelegate(mutableListOf<Credit>())

    var credit by MutableStateDelegate(0)
    var payment by MutableStateDelegate(0)

    var hideMoveMoney by MutableStateDelegate(false)

    var showMoney by MutableStateDelegate(false)
    var money by MutableStateDelegate(0.0)
    
    fun addCredit(newCredit: Credit){

        money += newCredit.value

        credits.add(newCredit)
        
        updateCreditData()
    }

    fun hasCredit() = credits.isNotEmpty()

    private fun updateCreditData() {
        credit = credits.sumOf { it.loan }.toInt()
        payment = credits.sumOf { it.payment }.toInt()
    }

    open fun tick(){
        credits.forEach { 
            it.tick()
            
            money -= it.payment
        }

        credits.removeAll { 
            it.loan <= 0
        }

        updateCreditData()
    }
}

class Credit(val value: Double, percent: Int, period: Int = 1) {

    //Вся сумма долга
    var loan = value * (1 + period * percent.toFloat() / 100.0)
    //Ежемесячный платёж
    val payment = loan / (period * 12)

    fun tick(){
        loan -= payment
    }
}
