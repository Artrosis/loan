package ru.predictor.loan.model

import androidx.compose.ui.BiasAlignment
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.DrawableResource
import ru.predictor.loan.model.modes.IndependentMode
import ru.predictor.loan.model.modes.LevelMode
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
        getAge = { levelMode.age },
    )

    val moneyToMarket = MoveableAnimation(
        market,
        { levelMode.moneyIcon },
        check = { checkMoneyFromBankToMarket() },
        isVisible = { canMarketTakeMoneyFromBank() },
        onFinishAction = { moveMoneyFromBankToMarket() }
    )

    val moneyToPeople = MoveableAnimation(
        people,
        { levelMode.moneyIcon },
        check = { checkMoneyFromBankToPeople() },
        isVisible = { canPeopleTakeMoneyFromBank() },
        onFinishAction = { moveMoneyFromBankToPeople() }
    )

    val moneyToManufacture = MoveableAnimation(
        manufacture,
        { levelMode.moneyIcon },
        check = { checkMoneyFromBankToManufacture() },
        isVisible = { canManufactureTakeMoneyFromBank() },
        onFinishAction = { moveMoneyFromBankToManufacture() }
    )

    val productsFromManufactureToPeople = MoveableAnimation(
        people,
        { levelMode.productIcon },
        check = { true },
        isVisible = {
            manufacture.products > 0
                    && levelMode.canMoveProductsFromManufactureToPeople
        },
        onFinishAction = { moveProductsFromManufactureToPeople() }
    )
    
    val productsToMarket = MoveableAnimation(
        market,
        { levelMode.productIcon },
        check = { checkMoveProductsFromManufactureToMarket() },
        isVisible = { showProductsFromManufactureToMarket() },
        onFinishAction = { moveProductsFromManufactureToMarket() }
    )
    
    val productsFromMarketToPeople = MoveableAnimation(
        people,
        { levelMode.productIcon },
        check = { checkMoveProductsFromMarketToPeople() },
        isVisible = { market.products > 0 },
        onFinishAction = { moveProductsFromMarketToPeople() }
    )

    val workPeople = MoveableAnimation(
        manufacture,
        { levelMode.workPeople },
        check = { checkMakeProducts() },
        isVisible = { true },
        onFinishAction = { makeProducts() }
    )
    
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
    )

    private val peopleHintAlignment = BiasAlignment(0.0f, 0.0f)
    private val progressHintAlignment = BiasAlignment(-0.6f, -0.5f)
    private val manufactureHintAlignment = BiasAlignment(0.0f, 0.0f)

    val hintQueue = mutableListOf(
        HintData(
            listOf(
                "Здесь будет основано новое поселение.",
                "Для жизни людям будут нужны продукты.",
                "Если продуктов достаточно, то население увеличивается.",
            ), peopleHintAlignment
        ),
        HintData(
            listOf(
                "Количество населения показано на этой шкале.",
                "Когда она достигнет максимума - вы перейдёте на следующий этап",

            ), progressHintAlignment
        ),
        HintData(
            listOf(
                "На начальном этапе всё необходимое для жизни люди берут из природы.",
                "Нажмите на иконку работника, чтобы отправить его добывать продукты.",
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
        initialization()*/
        
        messages.clear()
        hint.clear()
        hintQueue.clear()
    }
    
    fun canManufactureTakeMoneyFromBank(): Boolean{
        return levelMode.canTakeMoneyFromBank && !manufacture.hasCredit
    }
    fun canMarketTakeMoneyFromBank(): Boolean{
        return levelMode.canTakeMoneyFromBank && !market.hasCredit
    }
    fun canPeopleTakeMoneyFromBank(): Boolean{
        return levelMode.canTakeMoneyFromBank && !people.hasCredit
    }

    fun initialization() {
        levelMode.apply { initModel() }
    }

    private fun initNextAge() {
        levelMode = levelMode.nextMode()
        initialization()
    }

    private fun canInteract(): Boolean = !hint.isShow()

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

        if (hintQueue.isEmpty()) return

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
    
    fun moveProductsFromManufactureToPeople() {
        levelMode.apply {
            takeProductsFromManufactureToPeople()
        }
    }
    
    fun checkMoveProductsFromManufactureToMarket(): Boolean {
        levelMode.apply {
           return checkTakeProductsFromManufactureToMarket()
        } 
    }
    
    fun moveProductsFromManufactureToMarket() {
        levelMode.apply {
            takeProductsFromManufactureToMarket()
        }
    }
    
    fun checkMoveProductsFromMarketToPeople(): Boolean {
        levelMode.apply {
            return checkTakeProductsFromMarketToPeople()
        }
    }

    fun moveProductsFromMarketToPeople() {
        levelMode.apply {
            takeProductsFromMarketToPeople()
        }
    }
    
    fun checkMakeProducts(): Boolean {
        levelMode.apply {
            return checkClickManufacture()
        }  
    }
    
    fun makeProducts() {
        levelMode.apply {
            clickManufacture()
        }
    }

    private fun canInteractLevelMode(levelModeAction: LevelMode.() -> Unit) {
        if (!canInteract()) return

        levelMode.apply(levelModeAction)
    }

    fun checkMoneyFromBankToMarket(): Boolean {
        levelMode.apply {
            return checkMarketGiveMoney()
        }
    }

    fun checkMoneyFromBankToPeople(): Boolean {
        levelMode.apply {
            return checkPeopleGiveMoney()
        }
    }

    fun checkMoneyFromBankToManufacture(): Boolean {
        levelMode.apply {
            return checkManufactureGiveMoney()
        }
    }

    fun moveMoneyFromBankToMarket() {
        levelMode.apply {
            marketGiveMoney()
        }
    }

    fun moveMoneyFromBankToManufacture() {
        levelMode.apply {
            manufactureGiveMoney()
        }
    }

    fun moveMoneyFromBankToPeople() {
        levelMode.apply {
            peopleGiveMoney()
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
        val manufactureToPeopleHintAlignment = BiasAlignment(-0.0f, 0.0f)
        val manufactureToMarketHintAlignment = BiasAlignment(-0.0f, 0.0f)
        val marketToPeopleHintAlignment = BiasAlignment(-0.3f, -0.3f)
        val bankHintAlignment = BiasAlignment(0f, 0.8f)
        val bankHint2Alignment = BiasAlignment(0f, 0.8f)
    }
}