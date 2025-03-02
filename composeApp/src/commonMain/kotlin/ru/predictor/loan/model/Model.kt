package ru.predictor.loan.model

import androidx.compose.ui.BiasAlignment
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.DrawableResource
import ru.predictor.loan.model.modes.*
import ru.predictor.loan.utils.MutableStateDelegate

enum class Age(val caption: String) {
    INDEPENDENT("Самообеспечение"),
    BARTER("Натуральный обмен"),
    INDUSTRY("Индустриализация"),
    CREDITING("Кредитование"),
    FINISH("Конец")
}

class Model : CheckMobile() {
    var date by MutableStateDelegate(LocalDate(2000, 1, 1))

    var levelMode by MutableStateDelegate<LevelMode>(IndependentMode())

    val people = People(
        onDie = {
            messages.lines = listOf("Население вымерло")
            messages.buttonText = "Начать заново"
            messages.onNext = {
                levelMode = IndependentMode()
                initialization()
            }
        },
        getAge = { levelMode.age }
    )

    val market = Market(
        getAge = { levelMode.age },
    )
    val manufacture = Manufacture(
        onClick = {
            canInteractLevelMode {
                clickManufacture()
            }
        },
        getAge = { levelMode.age },
        canInteract = { canInteract() }
    )

    var movedProductsFromManufactureToPeople by MutableStateDelegate(false)
    var movedProductsFromMarketToPeople by MutableStateDelegate(false)
    var movedProductsFromManufactureToMarket by MutableStateDelegate(false)

    var movedMoneyFromBankToMarket by MutableStateDelegate(false)
    var movedMoneyFromBankToManufacture by MutableStateDelegate(false)
    var movedMoneyFromBankToPeople by MutableStateDelegate(false)
    
    var movedMoneyFromBankToAll by MutableStateDelegate(false)
    var isAnimatedMoveMoney by MutableStateDelegate(false)

    val bank = Bank(
        onClick = {
            canInteractLevelMode {
                clickBank()
            }
        },
        getAge = { levelMode.age },
        getMoneyCount = ::countMoney,
        getProductsData = {
            mutableMapOf(
                "manufacture.products" to manufacture.products,
                "people.food" to people.products,
                "market.products" to market.products
            )
        },
        allPayments = { people.payment + manufacture.payment + market.payment}
    )

    private val peopleHintAlignment = BiasAlignment(0.6f, 0.3f)
    private val progressHintAlignment = BiasAlignment(0f, -0.6f)
    private val manufactureHintAlignment = BiasAlignment(-0.8f, 0.3f)

    val hintQueue = mutableListOf(
        HintData(
            listOf(
                "В этом живописном месте у реки будет основано новое поселение.",
                "Для жизни людям будут нужны продукты. Если Продуктов достаточно,",
                "то население увеличивается. Каждый ваш клик по иконке человечка",
                "рядом с поселением, будет расходовать Продукты.",
            ), peopleHintAlignment
        ),
        HintData(
            listOf(
                "Шкала показывает количество населения. Чтобы перейти на следующий этап,",
                "нужно увеличить количество населения, показанное на шкале. По мере",
                "увеличения населения меняются экономические отношения между людьми.",
            ), progressHintAlignment
        ),
        HintData(
            listOf(
                "На начальном этапе всё необходимое для жизни люди берут из природы.",
                "Нажмите на иконку Лесоруба, чтобы отправить Людей трудиться.",
            ), manufactureHintAlignment
        ),
    )

    val messages = Messages {
        startMessage()
    }

    val hint = Hint {
        clearHint()
        nextHint()
    }

    init {
        initialization()

        /*messages.clear()
        levelMode = IndependentMode()
        initialization()
        manufacture.products = 13
        
        levelMode = BarterMode()
        initialization()

        levelMode = IndustryMode()
        initialization()
        
        levelMode = CreditingMode()
        initialization()
        
        messages.clear()
        hint.clear()
        hintQueue.clear()*/
    }
    
    fun canManufactureTakeMoneyFromBank(): Boolean{
        return levelMode.canTakeMoneyFromBank && !manufacture.hideMoveMoney
    }
    fun canMarketTakeMoneyFromBank(): Boolean{
        return levelMode.canTakeMoneyFromBank && !market.hideMoveMoney
    }
    fun canPeopleTakeMoneyFromBank(): Boolean{
        return levelMode.canTakeMoneyFromBank && !people.hideMoveMoney
    }

    fun initialization() {
        levelMode.apply { initModel() }
    }

    private fun initNextAge() {
        levelMode = levelMode.nextMode()
        initialization()
    }

    private fun canInteract(): Boolean = !hint.isShow() && !movedProductsFromManufactureToPeople

    private fun countMoney(): Double {
        return bank.money +
                manufacture.money +
                people.money +
                market.money
    }

    private fun clearHint() {
        hint.clear()
    }

    private fun startMessage() {
        messages.lines = listOf()
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
        date = date.incrementMonth()
        levelMode.apply { workOnManufacture() }

        levelMode.apply {
            bankTick()
            peopleTick()
            manufactureTick()
            marketTick()
        }

        if (checkLevelUp()) levelUp()
    }

    private fun LocalDate.incrementMonth(): LocalDate {
        var year = this.year
        var monthNumber = this.monthNumber

        if (monthNumber == 12) {
            monthNumber = 1
            year++
        } else {
            monthNumber++
        }

        return LocalDate(year, monthNumber, this.dayOfMonth)
    }

    private fun checkLevelUp(): Boolean = people.population >= levelMode.maxLevelPopulation

    private fun levelUp() {
        initNextAge()
        messages.apply {
            val showMessage = mutableListOf("Вы перешли на этап: ${levelMode.age.caption}")
            showMessage.addAll(levelMode.levelMessages)

            lines = showMessage
            buttonText = "Продолжить"
            onNext = {
                lines = listOf()
                nextHint()
            }
        }
    }

    fun aboutLevel() {
        messages.apply {
            val showMessage = mutableListOf("Вы на этапе: ${levelMode.age.caption}")
            showMessage.addAll(levelMode.levelMessages)

            lines = showMessage
            buttonText = "Продолжить"
            onNext = {
                lines = listOf()
                nextHint()
            }
        }
    }

    fun moveProductsFromManufactureToPeople() = canInteractLevelMode {
        movedProductsFromManufactureToPeople = true
    }

    fun moveMoneyFromBankToManufacture() = canInteractLevelMode {
        movedMoneyFromBankToManufacture = true
    }

    fun moveMoneyFromBankToPeople() = canInteractLevelMode {
        movedMoneyFromBankToPeople = true
    }

    fun moveMoneyFromBankToMarket() = canInteractLevelMode {
        movedMoneyFromBankToMarket = true
    }
    
    fun finishedMoveProductsFromManufactureToPeople() {
        levelMode.apply {
            takeProductsFromManufactureToPeople()
        }
        movedProductsFromManufactureToPeople = false
    }

    fun moveProductsFromManufactureToMarket() = canInteractLevelMode {
        movedProductsFromManufactureToMarket = true
    }
    
    suspend fun finishedMoveProductsFromManufactureToMarket() = coroutineScope {
        levelMode.apply {
            takeProductsFromManufactureToMarket()
        }
        movedProductsFromManufactureToMarket = false
        manufacture.hideProductsToMarket = true

        launch {
            delay(100L)
            manufacture.hideProductsToMarket = false
        }
    }

    fun moveProductsFromMarketToPeople() = canInteractLevelMode {
        movedProductsFromMarketToPeople = true
    }

    fun finishedMoveProductsFromMarketToPeople() {
        levelMode.apply {
            takeProductsFromMarketToPeople()
        }
        movedProductsFromMarketToPeople = false
    }


    private fun canInteractLevelMode(levelModeAction: LevelMode.() -> Unit) {
        if (!canInteract()) return

        levelMode.apply(levelModeAction)
    }

    suspend fun finishedMoveMoneyFromBankToMarket() = coroutineScope {
        canInteractLevelMode {
            movedMoneyFromBankToMarket = false
            market.hideMoveMoney = true
            marketGiveMoney()

            launch {
                delay(100L)
                market.hideMoveMoney = false
            }
        }
    } 

    suspend fun finishedMoveMoneyFromBankToManufacture() = coroutineScope {
        canInteractLevelMode {
            movedMoneyFromBankToManufacture = false
            manufacture.hideMoveMoney = true
            manufactureGiveMoney()

            launch {
                delay(100L)
                manufacture.hideMoveMoney = false
            }
        }
    } 

    suspend fun finishedMoveMoneyFromBankToPeople() = coroutineScope {
        canInteractLevelMode {
            movedMoneyFromBankToPeople = false
            people.hideMoveMoney = true
            peopleGiveMoney()

            launch {
                delay(100L)
                people.hideMoveMoney = false
            }
        }
    }

    fun populationProgress() = people.population / levelMode.maxLevelPopulation.toFloat()
    fun populationText() = "Население: ${people.population.format()} (из ${levelMode.maxLevelPopulation})"
    fun snowBankMoney(): Boolean {
        return levelMode.showBankMoney && !movedMoneyFromBankToAll && bank.money >= 3
    }
    
    fun startDistributeMoney(){
        movedMoneyFromBankToAll = true
        isAnimatedMoveMoney = true
    }
    fun finishedMoneyFromBankToAll(){        
        movedMoneyFromBankToAll = false
        bank.distributeMoney()
    }

    fun moneyIcon(): DrawableResource {
        return levelMode.moneyIcon
    }

    fun showProductsFromManufactureToMarket(): Boolean {
        return manufacture.products > 0
                && levelMode.canMoveProductsFromManufactureToMarket
                && !manufacture.hideProductsToMarket
    }

    companion object {
        val manufactureToPeopleHintAlignment = BiasAlignment(-0.4f, 0.6f)
        val manufactureToMarketHintAlignment = BiasAlignment(-0.4f, 0.2f)
        val marketToPeopleHintAlignment = BiasAlignment(0.6f, 0.2f)
        val bankHintAlignment = BiasAlignment(0f, 0.8f)
    }
}