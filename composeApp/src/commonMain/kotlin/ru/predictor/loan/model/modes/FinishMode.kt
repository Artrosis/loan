package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.Model
import ru.predictor.loan.utils.MutableStateDelegate

class FinishMode: CreditingMode() {
    override var age by MutableStateDelegate(Age.FINISH)

    override fun nextMode(): LevelMode {
        return this
    }

    override fun Model.takeProductsFromManufactureToMarket() {}
    override fun Model.takeProductsFromMarketToPeople() {}
    override fun Model.workOnManufacture() {}
}