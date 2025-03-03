package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.HintData
import ru.predictor.loan.model.Model
import ru.predictor.loan.model.Model.Companion.manufactureToMarketHintAlignment
import ru.predictor.loan.model.Model.Companion.marketToPeopleHintAlignment
import ru.predictor.loan.utils.MutableStateDelegate

class BarterMode: LevelMode() {
    override val maxLevelPopulation = 50
    override var age by MutableStateDelegate(Age.BARTER)
    override val canMoveProductsFromManufactureToMarket = true
    override val levelMessages = listOf(
        " ",
        "Число людей значительно возросло. ",
        "Среди них появились специалисты в различных областях.",
        "Кроме того, ассортимент Продуктов стал более разнообразным.",
        "",
        "Для обмена товарами было организовано специальное место — ярмарка.",
    )

    private val manufactureToMarketHint = HintData(
        listOf(
            "Отправь произведённые Продукты на ярмарку.",
            "Для этого нажми на иконку рядом с Заводом.",
        ), manufactureToMarketHintAlignment
    )
    
    override fun Model.initModel() {
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
                        "Теперь оправь Продукты к домам",
                    ), marketToPeopleHintAlignment
                ),
            )
            nextHint()
        }
    }

    override fun nextMode(): LevelMode {
        return IndustryMode()
    }

    override fun Model.takeProductsFromManufactureToMarket() {
        IndependentMode().apply { takeProductsFromManufactureToMarket()} 
    }

    override fun Model.takeProductsFromMarketToPeople() {
        people.products += market.takeProducts()
        people.checkFood()
    }

    override fun Model.workOnManufacture() {
        manufacture.apply {
            products += nextAddProduct(people.population.toInt())
        }
    }
}