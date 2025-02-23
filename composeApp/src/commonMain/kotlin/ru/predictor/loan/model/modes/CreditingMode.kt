package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.Credit
import ru.predictor.loan.model.Model
import ru.predictor.loan.utils.MutableStateDelegate

open class CreditingMode: IndustryMode() {
    override val maxLevelPopulation = 30000
    override var age by MutableStateDelegate(Age.CREDITING)
    override val canTakeMoneyFromBank = true
    override val showBankMoney = false
    override val levelMessages = listOf(
        "На этом этапе Банк решил давать деньги только в кредит под 8%"
    )

    override fun Model.initModel() {
        //У банка появился ссудный процент
        bank.showLoanInterest = true
        bank.showMoney = false
        
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
        if (people.hasCredit()){
            messages.apply {
                lines = listOf("Сперва закройте прошлый кредит")
                buttonText = "Понятно"
                onNext = {clear()}
                closeDismiss = true
            }
        }
        else {
            people.addCredit(makeCredit())
        }

    }
    
    override fun Model.manufactureGiveMoney(){

        if (manufacture.hasCredit()){
            messages.apply {
                lines = listOf("Сперва закройте прошлый кредит")
                buttonText = "Понятно"
                onNext = {clear()}
                closeDismiss = true
            }
        }
        else {
            manufacture.addCredit(makeCredit())
        }

    }
    
    override fun Model.marketGiveMoney(){

        if (market.hasCredit()){
            messages.apply {
                lines = listOf("Сперва закройте прошлый кредит")
                buttonText = "Понятно"
                onNext = {clear()}
                closeDismiss = true
            }
        }
        else {
            market.addCredit(makeCredit())
        }

    }
  
    override fun Model.bankTick() {}
}