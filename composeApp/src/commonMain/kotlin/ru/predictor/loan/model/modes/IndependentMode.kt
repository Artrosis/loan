package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.Model
import ru.predictor.loan.model.MutableStateDelegate

class IndependentMode() : LevelMode() {
    override val canMoveProductsFromManufactureToPeople = true
    override val maxLevelPopulation = 30
    override var age by MutableStateDelegate(Age.INDEPENDENT)

    override fun initModel(model: Model){
        
        model.people.population = 3f
        model.people.food = 6

        model.manufacture.products = 0
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