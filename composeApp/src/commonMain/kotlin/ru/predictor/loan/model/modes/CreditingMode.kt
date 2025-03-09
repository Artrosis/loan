package ru.predictor.loan.model.modes

import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.woodcutter_back
import ru.predictor.loan.model.Age
import ru.predictor.loan.model.Credit
import ru.predictor.loan.model.Creditor
import ru.predictor.loan.model.HintData
import ru.predictor.loan.model.Model
import ru.predictor.loan.utils.MutableStateDelegate

open class CreditingMode: IndustryMode() {
    override val maxLevelPopulation = 5000
    override var age by MutableStateDelegate(Age.CREDITING)
    override val canTakeMoneyFromBank = true
    override val showBankMoney = false
    override val levelMessages = listOf(
        " ",
        "Это дополнительных этап.",
        "На этом этапе банк решил изменить свои основные функции:",
        "- деньги теперь он эмитирует только под ссудные проценты;",
        "- он больше не следит за равенством денежной и товарной массы.",
        "Банк может печатать деньги бесконечно - были бы желающие взять кредиты.",
        " ",
        "Посмотрим, к чему это приведёт...",
    )
    
    override val moneyIcon = Res.drawable.woodcutter_back

    override fun Model.initModel() {
        //У банка появился ссудный процент
        bank.showLoanInterest = true
        bank.infinityMoney = true
        
        //У всех появились кредиты и платежи по ним
        people.showCredit = true
        manufacture.showCredit = true
        market.showCredit = true

        hintQueue.add(
            HintData(
                listOf(
                    "Для получения кредита, кликните на иконку денег в направлении должника.",
                    "При этом у должника появляется параметры ДОЛГ(ПЛАТЁЖ)",
                    "",
                    "По умолчанию кредит выдаётся в размере 10 000 под 8% на 12 ходов.",
                    "Ходы отсчитываются, когда население работает.",
                ), Model.bankHint2Alignment
            ),
        )
        nextHint()
    }

    override fun nextMode(): LevelMode {
        return FinishMode()
    }

    override fun Model.clickBank(){}
    
    private fun Model.makeCredit():Credit {
        return Credit(bank.loanSize.toDouble(), bank.loanInterest)
    }

    fun Model.creditorGiveMoney(creditor: Creditor){
        if (creditor.checkCredit()){
            messages.apply {
                lines = listOf("Сперва закройте прошлый кредит")
                messages.buttonText = "Начать заново"
                buttonText = "Понятно"
                onNext = {clear()}
                closeDismiss = true
            }
        }
        else {
            creditor.addCredit(makeCredit())
        }
    }

    override fun Model.peopleGiveMoney() = creditorGiveMoney(people)
    
    override fun Model.manufactureGiveMoney() = creditorGiveMoney(manufacture)
    
    override fun Model.marketGiveMoney() = creditorGiveMoney(market)
  
    override fun Model.bankTick() {}
}