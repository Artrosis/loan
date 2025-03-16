package ru.predictor.loan.model.modes

import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.level_1_wood
import loaninterest.composeapp.generated.resources.money
import loaninterest.composeapp.generated.resources.woodcutter_out
import ru.predictor.loan.model.Age
import ru.predictor.loan.model.Model

abstract class LevelMode{
    open val canMoveProductsFromManufactureToPeople = false
    open val canMoveProductsFromManufactureToMarket = false
    open val canTakeMoneyFromBank = false
    open val showBankMoney = false
    abstract val maxLevelPopulation: Int
    abstract var age: Age
    open val levelMessages = listOf<String>()
    open val moneyIcon = Res.drawable.money
    open val productIcon = Res.drawable.level_1_wood
    open val workPeople = Res.drawable.woodcutter_out
    
    open fun Model.initModel(){}
    abstract fun nextMode(): LevelMode
    open fun Model.takeProductsFromManufactureToPeople(){}
    abstract fun Model.takeProductsFromManufactureToMarket()
    abstract fun Model.takeProductsFromMarketToPeople()
    abstract fun Model.workOnManufacture()
    open fun Model.bankTick(){}
    open fun Model.peopleTick(){people.tick()}
    open fun Model.manufactureTick(){manufacture.tick()}
    open fun Model.marketTick(){market.tick()}
    open fun Model.clickBank(){}
    open fun Model.clickManufacture(){
        tick()
    }
    open fun Model.peopleGiveMoney(){}
    open fun Model.manufactureGiveMoney(){}
    open fun Model.marketGiveMoney(){}
}