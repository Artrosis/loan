package ru.predictor.loan.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.datetime.Clock
import ru.predictor.loan.model.modes.IndependentMode
import ru.predictor.loan.model.modes.LevelMode
import kotlin.reflect.KProperty
import kotlin.time.Duration.Companion.days

class MutableStateDelegate<T>(value: T) {
    
    private val state: MutableState<T> = mutableStateOf(value)
    
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return state.value
    }
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        state.value = value
    }
}

enum class Age(val caption: String){
    INDEPENDENT("Самообеспечение"),
    BARTER("Натуральный обмен"),
    INDUSTRY("Индустриализация"),
    FINISH("Конец")
}

class Model {
    var time by MutableStateDelegate(Clock.System.now())
    
    var levelMode by MutableStateDelegate<LevelMode>(IndependentMode())

    val people = People(
        getMaxLevelPopulation = { levelMode.maxLevelPopulation.toFloat() },
        onDie = {
            messages.messages = listOf("Население вымерло")
            messages.buttonText = "Начать заново"
            messages.onNext = {next()}
        }
    )

    private fun initNextAge() {
        levelMode = levelMode.nextMode()
        levelMode.initModel(this)
    }

    val market = Market()
    val manufacture = Manufacture(
        onClick = { if (canInteract()) work() },
        onGetPopulation = { return@Manufacture people.population.toInt() },
        getAge = {return@Manufacture levelMode.age},
    )

    private fun canInteract(): Boolean = messages.messages.isEmpty()

    private fun work(){ 
        tick()
    }    
    
    val bank = Bank()    
    val messages = Messages{
        next()
    }
    
    fun next(){
        messages.messages = listOf()
        levelMode.initModel(this)
    }
    
    fun tick() {
        time += 1.days        
        market.tick()
        bank.tick()
        manufacture.tick()        
        people.tick()

        if (checkLevelUp()) levelUp()
    }

    private fun checkLevelUp(): Boolean = people.population >= levelMode.maxLevelPopulation

    private fun levelUp() {
        initNextAge()
        messages.messages = listOf("Вы перешли на этап: ${levelMode.age.caption}")
        messages.buttonText = "Продолжить"
        messages.onNext = {
            messages.messages = listOf()
        }
    }

    fun takeProductsFromManufactureToPeople() {  
        
        if (!canInteract()) return
        
        people.food += manufacture.takeProducts()
    }
}