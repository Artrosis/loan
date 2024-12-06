package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.MutableStateDelegate

class FinishMode: LevelMode() {
    override val maxLevelPopulation = 3000
    override var age by MutableStateDelegate(Age.FINISH)

    override fun nextMode(): LevelMode {
        return this
    }
}