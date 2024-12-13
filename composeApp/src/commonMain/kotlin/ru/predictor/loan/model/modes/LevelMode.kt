package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.Model

abstract class LevelMode{
    open val canMoveProductsFromManufactureToPeople = false
    open val canMoveProductsFromManufactureToMarket = false
    open val canTakeMoneyFromBank = false
    abstract val maxLevelPopulation: Int
    abstract var age: Age
    open val levelMessages = listOf<String>()
    
    open fun Model.initModel(){}
    abstract fun nextMode(): LevelMode
    open fun Model.takeProductsFromManufactureToPeople(){}
    abstract fun Model.takeProductsFromManufactureToMarket()
    abstract fun Model.takeProductsFromMarketToPeople()
    abstract fun Model.workOnManufacture()
    open fun Model.bankTick(){}
    open fun Model.peopleTick(){people.tick()}
    open fun Model.manufactureTick(){}
    open fun Model.marketTick(){}
    open fun Model.clickBank(){}
    open fun Model.clickManufacture(){
        tick()
    }
    open fun Model.peopleGiveMoney(){}
    open fun Model.manufactureGiveMoney(){}
    open fun Model.marketGiveMoney(){}
}