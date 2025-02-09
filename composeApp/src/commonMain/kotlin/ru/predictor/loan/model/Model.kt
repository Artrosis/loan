package ru.predictor.loan.model

import androidx.compose.ui.BiasAlignment
import kotlinx.datetime.LocalDate
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
        }
    )

    private val peopleHintAlignment = BiasAlignment(0.6f, 0.3f)
    private val progressHintAlignment = BiasAlignment(0f, -0.6f)
    private val manufactureHintAlignment = BiasAlignment(-0.8f, 0.3f)

    val hintQueue = mutableListOf(
        HintData(
            listOf(
                "У нас есть люди.",
                "Для жизни им нужны продукты.",
                "Продукты расходуются каждый день.",
                "Если продуктов достаточно, то население увеличивается.",
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

    fun moveProductsFromManufactureToPeople() = canInteractLevelMode {
        movedProductsFromManufactureToPeople = true
    }

    fun finishedMoveProductsFromManufactureToPeople() {
        levelMode.apply {
            takeProductsFromManufactureToPeople()
        }
        movedProductsFromManufactureToPeople = false
    }

    fun moveProductsFromManufactureToMarket() = canInteractLevelMode {
        takeProductsFromManufactureToMarket()
    }

    fun moveProductsFromMarketToPeople() = canInteractLevelMode {
        takeProductsFromMarketToPeople()
    }

    private fun canInteractLevelMode(levelModeAction: LevelMode.() -> Unit) {
        if (!canInteract()) return

        levelMode.apply(levelModeAction)
    }

    fun marketTakeMoney() = canInteractLevelMode {
        marketGiveMoney()
    }

    fun manufactureTakeMoney() = canInteractLevelMode {
        manufactureGiveMoney()
    }

    fun peopleTakeMoney() = canInteractLevelMode {
        peopleGiveMoney()
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