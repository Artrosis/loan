package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.HintData
import ru.predictor.loan.model.Model
import ru.predictor.loan.model.Model.Companion.manufactureToPeopleHintAlignment
import ru.predictor.loan.utils.MutableStateDelegate

class IndependentMode() : LevelMode() {
    override val canMoveProductsFromManufactureToPeople = true
    override val maxLevelPopulation = 30
    override var age by MutableStateDelegate(Age.INDEPENDENT)

    override fun Model.initModel(){
        people.population = 3f
        people.food = 6

        manufacture.products = 0

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
        people.food += manufacture.takeProducts()
        people.checkFood()
    }

    override fun Model.workOnManufacture() {
        manufacture.apply {
            products += nextAddProduct(people.population.toInt())
        }
    }
}