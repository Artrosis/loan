package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.HintData
import ru.predictor.loan.model.Model
import ru.predictor.loan.model.Model.Companion.manufactureToMarketHintAlignment
import ru.predictor.loan.model.Model.Companion.marketToPeopleHintAlignment
import ru.predictor.loan.utils.MutableStateDelegate

class BarterMode: LevelMode() {
    override val maxLevelPopulation = 300
    override var age by MutableStateDelegate(Age.BARTER)
    override val canMoveProductsFromManufactureToMarket = true
    override val levelMessages = listOf("На этом этапе появляется специализация.", "Люди производят разные товары и обмениваются этими товарами на ярмарке.")

    private val manufactureToMarketHint = HintData(
        listOf(
            "Отправим произведённые товары на ярмарку.",
        ), manufactureToMarketHintAlignment
    )
    
    override fun initModel(model: Model) {
        model.apply {
            market.has = true
            
            if (manufacture.products > 0) {
                hintQueue.add(manufactureToMarketHint)
                nextHint()
            }
            else {
                manufacture.isShowTakeProducts = false

                manufacture.onFirstGetProducts.clear()
                manufacture.onFirstGetProducts += {
                    hintQueue.add(manufactureToMarketHint)
                    nextHint()
                }
            }

            market.onFirstGetProducts.clear()
            market.onFirstGetProducts += {
                hintQueue.add(
                    HintData(
                        listOf(
                            "Нажми стрелочку, чтобы люди обменялись товарами.",
                        ), marketToPeopleHintAlignment
                    ),
                )
                nextHint()
            }
        }
    }

    override fun nextMode(): LevelMode {
        return IndustryMode()
    }

    override fun takeProductsFromManufactureToMarket(gameModel: Model) {
        IndependentMode().takeProductsFromManufactureToMarket(gameModel)
    }

    override fun takeProductsFromMarketToPeople(gameModel: Model) {
        gameModel.people.food += gameModel.market.takeProducts()
        gameModel.people.checkFood()
    }

    override fun workOnManufacture(gameModel: Model) {
        gameModel.manufacture.apply {
            products += nextAddProduct(gameModel.people.population.toInt())
        }
    }
}