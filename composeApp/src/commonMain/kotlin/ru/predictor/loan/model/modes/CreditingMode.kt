package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.Model
import ru.predictor.loan.utils.MutableStateDelegate

class CreditingMode: IndustryMode() {
    override val maxLevelPopulation = 30000
    override var age by MutableStateDelegate(Age.CREDITING)
    override val canTakeMoneyFromBank = true
    override val levelMessages = listOf(
        "На этом этапе Банк решил давать деньги только в кредит под 8%"
    )

    override fun Model.initModel() {
        //У банка появился ссудный процент
        bank.showLoanInterest = true
        
        //У всех появились кредиты и платежи по ним
        people.showCredit = true
        manufacture.showCredit = true
        market.showCredit = true
    }

    override fun nextMode(): LevelMode {
        return FinishMode()
    }

    override fun Model.clickBank(){}

    private val giveMoneyCount = 1000f
    override fun Model.peopleGiveMoney(){
        people.apply {
            money += giveMoneyCount
            credit += giveMoneyCount * (1 + bank.loanInterest.toFloat() / 100f)
            payment += 90
        }
    }
    
    override fun Model.manufactureGiveMoney(){
        manufacture.apply {
            money += giveMoneyCount
            credit += giveMoneyCount * (1 + bank.loanInterest.toFloat() / 100f)
            payment += 90
        }
    }
    
    override fun Model.marketGiveMoney(){
        market.apply {
            money += giveMoneyCount
            credit += giveMoneyCount * (1 + bank.loanInterest.toFloat() / 100f)
            payment += 90
        }  
    }

    override fun Model.peopleTick(){
        people.apply { 
            tick()
            money -= payment            
        }        
    }
    override fun Model.manufactureTick(){
        manufacture.money -= manufacture.payment
    }
    override fun Model.marketTick(){
        market.money -= market.payment
    }
    override fun Model.bankTick() {}
}