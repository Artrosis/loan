package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.Model

abstract class LevelMode{
    abstract val maxLevelPopulation: Int
    abstract var age: Age
    
    open fun initModel(model: Model){}
    abstract fun nextMode(): LevelMode
}