package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.MutableStateDelegate

class IndustryMode: LevelMode() {
    override val maxLevelPopulation = 3000
    override var age by MutableStateDelegate(Age.INDUSTRY)

    override fun nextMode(): LevelMode {
        return FinishMode()
    }
}