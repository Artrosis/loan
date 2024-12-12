package ru.predictor.loan.model

import androidx.compose.ui.BiasAlignment
import kotlinx.datetime.Clock
import ru.predictor.loan.model.modes.IndependentMode
import ru.predictor.loan.model.modes.LevelMode
import ru.predictor.loan.utils.MutableStateDelegate
import kotlin.time.Duration.Companion.days

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
            messages.onNext = {
                levelMode = IndependentMode()
                levelMode.initModel(this)
            }
        }
    )

    val market = Market(
        getAge = { levelMode.age },
    )
    val manufacture = Manufacture(
        onClick = 
        {
            if (!canInteract()) return@Manufacture
            
            levelMode.apply { 
                clickManufacture()
            }
        },
        getAge = { levelMode.age },
    )

    val bank = Bank(
        onClick = {
            if (canInteract()) levelMode.clickBank(this)
        },
        getMoneyCount = ::countMoney,
        getProductsData = {
            mutableMapOf(
                "manufacture.products" to manufacture.products,
                "people.food" to people.food,
                "market.products" to market.products
            )
        }
    )

    private val peopleHintAlignment = BiasAlignment(0.6f, 0.3f)
    private val progressHintAlignment = BiasAlignment(0f, -0.6f)
    private val manufactureHintAlignment = BiasAlignment(-0.8f, 0.3f)

    val hintQueue = mutableListOf(
        HintData(
            listOf(
                "У нас есть люди.",
                "Для жизни им нужна еда.",
                "Еда расходуется каждый день.",
                "Если еды достаточно, то население увеличивается.",
            ), peopleHintAlignment
        ),
        HintData(
            listOf(
                "Шкала показывает количество населения.",
                "При росте населения меняются экономические взаимоотношения между людьми.",
            ), progressHintAlignment
        ),
        HintData(
            listOf(
                "На начальном этапе всё необходимое для жизни люди берут из природы.",
                "Нажмите на иконку с деревом, чтобы отправить людей трудиться.",
            ), manufactureHintAlignment
        ),
    )

    val messages = Messages{
        startMessage()
    }

    val hint = Hint{
        clearHint()
        nextHint()
    }
    
    init{
        levelMode.initModel(this)
    }

    private fun initNextAge() {
        levelMode = levelMode.nextMode()
        levelMode.initModel(this)
    }

    private fun canInteract(): Boolean = hint.disable || hint.message.isEmpty()

    private fun countMoney(): Int {
        return bank.money + 
                manufacture.money + 
                people.money + 
                market.money
    }

    private fun clearHint() {
        hint.clear()
    }

    private fun startMessage(){
        messages.messages = listOf()
        nextHint()
    }

    fun nextHint() {
        
        if (hintQueue.size == 0) return
        
        val nextHint = hintQueue.first()
        
        hint.message = nextHint.message
        hint.alignment = nextHint.alignment

        hintQueue.remove(nextHint)
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
        messages.apply {
            val showMessage = mutableListOf("Вы перешли на этап: ${levelMode.age.caption}")
            showMessage.addAll(levelMode.levelMessages)
            
            messages = showMessage            
            buttonText = "Продолжить"
            onNext = {
                messages = listOf()
                nextHint()
            }
        }
    }

    fun takeProductsFromManufactureToPeople() {
        if (!canInteract()) return
        
        levelMode.takeProductsFromManufactureToPeople(this)
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

    companion object {
        val manufactureToPeopleHintAlignment = BiasAlignment(-0.4f, 0.6f)
        val manufactureToMarketHintAlignment = BiasAlignment(-0.4f, 0.2f)
        val marketToPeopleHintAlignment = BiasAlignment(0.6f, 0.2f)
        val bankHintAlignment = BiasAlignment(0f, 0.8f)
    }
}