package ru.predictor.loan.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.datetime.Clock
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

enum class Age(val caption: String, val maxLevelPopulation: Int){
    INDEPENDENT("Самообеспечение", 30),
    BARTER("Натуральный обмен", 300),
    INDUSTRY("Индустриализация", 3000),
    FINISH("Конец", 3000)
}

class Model {
    var time by MutableStateDelegate(Clock.System.now())
    
    var age by MutableStateDelegate(Age.INDEPENDENT)

    val people = People(
        onLevelUp = 
        {
            messages.messages = listOf("Вы перешли на этап: ${getNextAge().caption}")
            messages.buttonText = "Продолжить"
            messages.onNext = {
                messages.messages = listOf()
                initNextAge()                
            }                    
        },
        onDie = {
            messages.messages = listOf("Население вымерло")
            messages.buttonText = "Начать заново"
            messages.onNext = {next()}
        }
    )

    private fun initNextAge() {
        
        val nextAge = getNextAge()

        age = nextAge
        people.maxLevelPopulation = age.maxLevelPopulation.toFloat()
    }

    private fun getNextAge(): Age = when (age) {
        Age.INDEPENDENT -> Age.BARTER
        Age.BARTER -> Age.INDUSTRY
        Age.INDUSTRY -> Age.FINISH
        Age.FINISH -> Age.FINISH
    }

    val market = Market()
    val manufacture = Manufacture(
        onClick = { if (canInteract()) work() },
        onGetPopulation = { return@Manufacture people.population.toInt() },
        getAge = {return@Manufacture age},
    )

    private fun canInteract(): Boolean = messages.messages.isEmpty()

    private fun work(){ 
        tick()
    }    
    
    val bank = Bank()    
    val messages = Messages{
        next()
    }
    
    private fun start()
    {
        people.population = 3f
        people.maxLevelPopulation = 30f
        people.food = 6
        
        manufacture.products = 0
    }
    
    fun next(){
        messages.messages = listOf()
        start()
    }
    
    fun tick() {
        time += 1.days        
        market.tick()
        bank.tick()
        manufacture.tick()        
        people.tick()
    }

    fun takeProductsFromManufactureToPeople() {  
        
        if (!canInteract()) return
        
        people.food += manufacture.takeProducts()
    }
}