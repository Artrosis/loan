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
        
        //Добавили товаров предприятию, чтобы оно могло продать их в магазин и получить денег
        model.manufacture.products = maxOf(model.manufacture.products, 100)
        
        //Банк напечатал денег под товары в магазине
        model.bank.emmitMoney()
    }
    
    override fun nextMode(): LevelMode {
        return FinishMode()
    }

    override fun takeProductsFromManufactureToMarket(gameModel: Model) {
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
        val maxCount = minOf(gameModel.people.money, gameModel.market.products)

        gameModel.people.apply { 
            money -= maxCount
            food += maxCount
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
}