package ru.predictor.loan.model.modes

import ru.predictor.loan.model.Age
import ru.predictor.loan.model.HintData
import ru.predictor.loan.model.Model
import ru.predictor.loan.utils.MutableStateDelegate

class IndustryMode: LevelMode() {
    override val maxLevelPopulation = 3000
    override var age by MutableStateDelegate(Age.INDUSTRY)
    override val canMoveMoneyFromBankToPeople = true
    override val canMoveProductsFromManufactureToMarket = true
    override val levelMessages = listOf(
        "Людей стало больше.",
        "Для удобства обмена, люди придумали деньги.",
        "Учёт денег выполняет банк.",
        "Он же следит за тем, чтобы количество денег было равно количеству товаров.",
    )
    
    override fun initModel(model: Model) {        
        BarterMode().initModel(model)

        model.apply {
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

    override fun workOnManufacture(gameModel: Model) {
        gameModel.apply {
            val workersCount = minOf(manufacture.money, people.population.toInt())

            people.apply {
                money += workersCount
            }

            manufacture.apply {
                money -= workersCount
                products += nextAddProduct(workersCount)
            }
        }
    }
    
    override fun clickBank(gameModel: Model){
        gameModel.apply {
            val economicParticipantsCount = 3
            
            if (bank.money < economicParticipantsCount) return
            
            //Банк поровну раздаёт деньги населению, производству и предприятию            
            val partMoney = bank.money / economicParticipantsCount
            
            people.money += partMoney
            manufacture.money += partMoney
            market.money += partMoney
            
            bank.money -= partMoney * economicParticipantsCount
        }
    }
    
    override fun Model.clickManufacture(){
            if (manufacture.money == 0) {
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