package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.HintData
import ru.predictor.loan.model.Model
import ru.predictor.loan.model.Model.Companion.manufactureToPeopleHintAlignment
import ru.predictor.loan.model.PeopleState
import ru.predictor.loan.utils.MutableStateDelegate

class IndependentMode() : LevelMode() {
    override val canMoveProductsFromManufactureToPeople = true
    override val maxLevelPopulation = 30
    override var age by MutableStateDelegate(Age.INDEPENDENT)

    override fun Model.initModel(){
        market.has = false
        bank.has = false
        people.showMoney = false
        manufacture.showMoney = false
        market.showMoney = false
        bank.showLoanInterest = false
        people.showCredit = false
        manufacture.showCredit = false
        market.showCredit = false
        bank.infinityMoney = false

        people.population = 10f
        people.products = 20
        people.money = 0.0
        people.clearCredits()
        people.state = PeopleState.GOOD

        manufacture.products = 0
        manufacture.money = 0.0
        manufacture.clearCredits()

        market.products = 0
        market.money = 0.0
        market.clearCredits()

        manufacture.onFirstGetProducts += {
            hintQueue.add(
                HintData(
                    listOf(
                        "Люди потрудились и проголодались.",
                        "Нажми на стрелку, чтобы перенести к ним продукты.",
                    ), manufactureToPeopleHintAlignment
                ),
            )
            nextHint()
        }
    }

    override fun nextMode(): LevelMode {
        return BarterMode()
    }

    override fun Model.takeProductsFromManufactureToMarket() {
        market.products += manufacture.takeProducts()
    }

    override fun Model.takeProductsFromMarketToPeople() {}

    override fun Model.takeProductsFromManufactureToPeople(){
        people.products += manufacture.takeProducts()
        people.checkFood()
    }

    override fun Model.workOnManufacture() {
        manufacture.apply {
            products += nextAddProduct(people.population.toInt())
        }
    }
}
