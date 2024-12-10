package ru.predictor.loan.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.BiasAlignment
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

    val market = Market(
        getAge = { levelMode.age },
    )
    val manufacture = Manufacture(
        onClick = { if (canInteract()) work() },
        getAge = { levelMode.age },
    )

    private fun canInteract(): Boolean = hint.message.isEmpty()

    private fun work(){ 
        tick()
    }    
    
    val bank = Bank(
        getMoneyCount = ::countMoney,
        getProductsData = {
            mutableMapOf(
                "manufacture.products" to manufacture.products,
                "people.food" to people.food,
                "market.products" to market.products
                )
        }
    )

    private fun countMoney(): Int {
        return bank.money + 
                manufacture.money + 
                people.money + 
                market.money
    }
    
    val messages = Messages{
        next()
    }
    
    val hint = Hint{
        clearHint()
    }

    private fun clearHint() {
        hint.clear()
    }

    fun next(){
        messages.messages = listOf()
        levelMode.initModel(this)
        onStart()
    }
    
    private val peopleHintAlignment = BiasAlignment(0.6f, 0.3f)

    private fun onStart() {
        hint.message = listOf(
            "Это население",
            "Ему нужна еда для жизни и роста"
        )
        hint.alignment = peopleHintAlignment
    }

    fun tick() {
        time += 1.days 
        levelMode.workOnManufacture(this)       
        people.tick()
        if (bank.has) {
            bank.emmitMoney()
        }

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
        
        levelMode.takeProductsFromManufactureToPeople(this)
    }
    
    fun takeMoveMoneyFromBankToPeople(){
        if (!canInteract()) return

        levelMode.takeMoveMoneyFromBankToPeople(this)
    }

    fun takeProductsFromManufactureToMarket() {

        if (!canInteract()) return
        
        levelMode.takeProductsFromManufactureToMarket(this)
    }
    
    fun takeProductsFromMarketToPeople(){
        if (!canInteract()) return
        
        levelMode.takeProductsFromMarketToPeople(this)
    }

    fun populationProgress() = people.population / levelMode.maxLevelPopulation.toFloat()
    fun populationText() = "${people.population.format()} / ${levelMode.maxLevelPopulation}"
}