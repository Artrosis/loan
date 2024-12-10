package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.Model
import ru.predictor.loan.model.MutableStateDelegate

class IndustryMode: LevelMode() {
    override val maxLevelPopulation = 3000
    override var age by MutableStateDelegate(Age.INDUSTRY)
    override val canMoveMoneyFromBankToPeople = true
    override val canMoveProductsFromManufactureToMarket = true

    override fun initModel(model: Model) {
        
        BarterMode().initModel(model)
        
        //Появился банк
        model.bank.has = true

        //У всех появились деньги
        model.people.showMoney = true
        model.manufacture.showMoney = true
        model.market.showMoney = true
        
        //Обнулили еду у населения       
        model.people.food = 0
        
        //На заводе и в магазине товаров минимум 100 в начале уровня
        model.manufacture.products = maxOf(model.manufacture.products, 100)
        model.market.products = maxOf(model.market.products, 100)
        
        //Банк напечатал денег под товары в магазине
        model.bank.emmitMoney()
    }
    
    override fun nextMode(): LevelMode {
        return FinishMode()
    }

    override fun takeProductsFromManufactureToMarket(gameModel: Model) {
        
        if (gameModel.market.money == 0) {
            gameModel.messages.apply {
                messages = listOf("Нет денег в магазине")
                buttonText = "Понял"
                onNext = {clear()}
            }
        }
        
        val maxCount = minOf(gameModel.manufacture.products, gameModel.market.money)

        gameModel.market.apply {
            money -= maxCount
            products += maxCount
        }

        gameModel.manufacture.apply {
            money += maxCount
            products -= maxCount
        }
    }

    override fun takeProductsFromMarketToPeople(gameModel: Model) {
        if (gameModel.people.money == 0) {
            gameModel.messages.apply {
                messages = listOf("Нет денег у населения")
                buttonText = "Понял"
                onNext = {clear()}
            }
        }
        
        val maxCount = minOf(gameModel.people.money, gameModel.market.products)

        gameModel.people.apply { 
            money -= maxCount
            food += maxCount
            checkFood()
        }
        
        gameModel.market.apply { 
            money += maxCount
            products -= maxCount
        }
    }

    override fun takeMoveMoneyFromBankToPeople(gameModel: Model){
        gameModel.people.money += gameModel.bank.money
        gameModel.bank.money = 0
    }

    override fun workOnManufacture(gameModel: Model) {
        if (gameModel.manufacture.money == 0) {
            gameModel.messages.apply {
                messages = listOf("Нет денег на предприятии для оплаты труда")
                buttonText = "Понял"
                onNext = {clear()}
            }
        }

        val workersCount = minOf(gameModel.manufacture.money, gameModel.people.population.toInt())

        gameModel.people.apply {
            money += workersCount
        }

        gameModel.manufacture.apply {
            money -= workersCount
            products += nextAddProduct(workersCount)
        }
    }
}