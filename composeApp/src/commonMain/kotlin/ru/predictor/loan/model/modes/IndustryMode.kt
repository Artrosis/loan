package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.HintData
import ru.predictor.loan.model.Model
import ru.predictor.loan.utils.MutableStateDelegate

open class IndustryMode: LevelMode() {
    override val maxLevelPopulation = 300
    override var age by MutableStateDelegate(Age.INDUSTRY)
    override val canMoveProductsFromManufactureToMarket = true
    override val showBankMoney = true
    override val levelMessages = listOf(
        " ",
        "Население продолжает расти и, для облегчения процесса обмена, люди придумали деньги",
        "В нашей игре функция Банка будет заключаться в эмиссии - ",
        "создании и распределении новых денежных единиц.",
        "Кроме того, Банк будет вести учёт денежной массы и следить за тем,",
        "чтобы количество денег соответствовало объёму товаров на рынке.",
    )
    
    override fun Model.initModel() {        
        BarterMode().apply { initModel() }

        //Появился банк
        bank.has = true

        //У всех появились деньги
        people.showMoney = true
        manufacture.showMoney = true
        market.showMoney = true

        //Банк напечатал денег под товары в магазине
        bank.emmitMoney()

        hintQueue.add(
            HintData(
                listOf(
                    "Клинки на монетки, чтобы раздать всем деньги для начала обмена.",
                    "1 продукт стоит 1 денежную единицу;",
                    "1 работник производит 2 продукта, зарабатывая за это 1 единицу денег",
                ), Model.bankHintAlignment
            ),
        )
        nextHint()
    }
    
    override fun nextMode(): LevelMode {
        return CreditingMode()
    }

    override fun Model.checkTakeProductsFromManufactureToMarket(): Boolean {
        if (market.money <= 0.0) {
            messages.apply {
                lines = listOf("Нет денег в магазине")
                buttonText = "Понятно"
                onNext = {clear()}
                closeDismiss = true
            }
            return false
        }
        return true
    }

    override fun Model.takeProductsFromManufactureToMarket() {        
        val maxCount = minOf(manufacture.products, market.money.toInt())

        market.apply {
            money -= maxCount
            products += maxCount
        }

        manufacture.apply {
            money += maxCount
            products -= maxCount
        }
    }
    
    override fun Model.checkTakeProductsFromMarketToPeople(): Boolean {
        if (people.money <= 0.0) {
            messages.apply {
                lines = listOf("Нет денег у населения")
                buttonText = "Понятно"
                onNext = {clear()}
                closeDismiss = true
            }
            return false
        }
        
        return true
    }

    override fun Model.takeProductsFromMarketToPeople() {
        val maxCount = minOf(people.money.toInt(), market.products)

        people.apply { 
            money -= maxCount
            products += maxCount
            checkFood()
        }
        
        market.apply { 
            money += maxCount
            products -= maxCount
        }
    }

    override fun Model.workOnManufacture() {
        val workersCount = minOf(manufacture.money.toInt(), people.population.toInt())
        
        val workMoney = calcSalary().toInt()

        people.apply {
            money += workMoney
        }

        manufacture.apply {
            money -= workMoney
            products += nextAddProduct(workersCount)
        }
    }
    
    override fun Model.clickBank(){
        val economicParticipantsCount = 3
        
        if (bank.money < economicParticipantsCount) return
        
        //Банк поровну раздаёт деньги населению, производству и предприятию            
        val partMoney = (bank.money / economicParticipantsCount).toInt()
        
        people.money += partMoney
        manufacture.money += partMoney
        market.money += partMoney
        
        bank.money -= partMoney * economicParticipantsCount
    }

    override fun Model.bankTick() {
       bank.emmitMoney()
    }
    
    override fun Model.checkClickManufacture(): Boolean {
        if (manufacture.money < calcSalary()) {
            messages.apply {
                lines = listOf("Нет денег на предприятии для оплаты труда")
                buttonText = "Понятно"
                onNext = {clear()}
                closeDismiss = true
            }
            return false
        }
        
        return true
    }
    
    override fun Model.clickManufacture(){
        tick()        
    }
    
    private fun Model.calcSalary() = people.population * manufacture.salary
}