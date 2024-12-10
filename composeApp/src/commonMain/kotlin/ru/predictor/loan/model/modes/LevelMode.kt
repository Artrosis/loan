package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.Model

abstract class LevelMode{
    open val canMoveProductsFromManufactureToPeople = false
    open val canMoveProductsFromManufactureToMarket = false
    open val canMoveMoneyFromBankToPeople = false
    abstract val maxLevelPopulation: Int
    abstract var age: Age
    
    open fun initModel(model: Model){}
    abstract fun nextMode(): LevelMode
    open fun takeProductsFromManufactureToPeople(gameModel: Model){}
    abstract fun takeProductsFromManufactureToMarket(gameModel: Model)
    abstract fun takeProductsFromMarketToPeople(gameModel: Model)
    open fun takeMoveMoneyFromBankToPeople(gameModel: Model){}
}