package ru.predictor.loan.model.modes

import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.level_2_products
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
        "Кроме того, ассортимент продуктов стал более разнообразным.",
        "",
        "Для обмена товарами было организовано специальное место — ярмарка.",
    )

    private val manufactureToMarketHint = HintData(
        listOf(
            "Отправьте произведённые продукты на ярмарку.",
            "Для этого нажмите на иконку рядом с заводом.",
        ), manufactureToMarketHintAlignment
    )
    override val productIcon = Res.drawable.level_2_products
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
                        "Теперь оправьте продукты к домам",
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