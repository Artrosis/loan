package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.Credit
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
    
    private fun Model.makeCredit():Credit {
        return Credit(bank.loanSize.toDouble(), bank.loanInterest)
    }

    override fun Model.peopleGiveMoney(){
        people.addCredit(makeCredit())
    }
    
    override fun Model.manufactureGiveMoney(){
        manufacture.addCredit(makeCredit())
    }
    
    override fun Model.marketGiveMoney(){
        market.addCredit(makeCredit()) 
    }
  
    override fun Model.bankTick() {}
}