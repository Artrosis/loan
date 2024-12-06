package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.MutableStateDelegate

class BarterMode: LevelMode() {
    override val maxLevelPopulation = 300
    override var age by MutableStateDelegate(Age.BARTER)

    override fun nextMode(): LevelMode {
        return IndustryMode()
    }
}