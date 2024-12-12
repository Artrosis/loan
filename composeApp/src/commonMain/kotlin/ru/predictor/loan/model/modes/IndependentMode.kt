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

    override fun initModel(model: Model){
        model.apply {
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
    }

    override fun nextMode(): LevelMode {
        return BarterMode()
    }

    override fun takeProductsFromManufactureToMarket(gameModel: Model) {
        gameModel.market.products += gameModel.manufacture.takeProducts()
    }

    override fun takeProductsFromMarketToPeople(gameModel: Model) {}

    override fun takeProductsFromManufactureToPeople(gameModel: Model){
        gameModel.people.food += gameModel.manufacture.takeProducts()
        gameModel.people.checkFood()
    }

    override fun workOnManufacture(gameModel: Model) {
        gameModel.manufacture.apply {
            products += nextAddProduct(gameModel.people.population.toInt())
        }
    }
}