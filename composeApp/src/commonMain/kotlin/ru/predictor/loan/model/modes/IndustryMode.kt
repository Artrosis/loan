package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.HintData
import ru.predictor.loan.model.Model
import ru.predictor.loan.utils.MutableStateDelegate

open class IndustryMode: LevelMode() {
    override val maxLevelPopulation = 3000
    override var age by MutableStateDelegate(Age.INDUSTRY)
    override val canMoveProductsFromManufactureToMarket = true
    override val levelMessages = listOf(
        "Людей стало больше.",
        "Для удобства обмена, люди придумали деньги.",
        "Учёт денег выполняет банк.",
        "Он же следит за тем, чтобы количество денег было равно количеству товаров.",
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
                    "Клинки на Банк, чтобы раздать всем деньги для начала обмена.",
                ), Model.bankHintAlignment
            ),
        )
        nextHint()
    }
    
    override fun nextMode(): LevelMode {
        return CreditingMode()
    }

    override fun Model.takeProductsFromManufactureToMarket() {
        
        if (market.money == 0.0) {
            messages.apply {
                messages = listOf("Нет денег в магазине")
                buttonText = "Понял"
                onNext = {clear()}
            }
        }
        
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

    override fun Model.takeProductsFromMarketToPeople() {
        if (people.money == 0.0) {
            messages.apply {
                messages = listOf("Нет денег у населения")
                buttonText = "Понял"
                onNext = {clear()}
            }
        }
        
        val maxCount = minOf(people.money.toInt(), market.products)

        people.apply { 
            money -= maxCount
            food += maxCount
            checkFood()
        }
        
        market.apply { 
            money += maxCount
            products -= maxCount
        }
    }

    override fun Model.workOnManufacture() {
        val workersCount = minOf(manufacture.money.toInt(), people.population.toInt())
        
        val workMoney = (workersCount * manufacture.salary).toInt()

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
    
    override fun Model.clickManufacture(){
        if (manufacture.money < people.population) {
            messages.apply {
                messages = listOf("Нет денег на предприятии для оплаты труда")
                buttonText = "Понял"
                onNext = {clear()}
            }
            return
        }
        
        tick()        
    }
}