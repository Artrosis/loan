package ru.predictor.loan.model

import loaninterest.composeapp.generated.resources.*
import loaninterest.composeapp.generated.resources.Res
import ru.predictor.loan.utils.Event
import ru.predictor.loan.utils.MutableStateDelegate
import ru.predictor.loan.utils.ObservableStateDelegate

class Market(
    val getAge: () -> Age,
): Creditor() {
    //Видимость
    var has by MutableStateDelegate(false)

    private var isShowTakeProducts = false
    var products by ObservableStateDelegate(0){ newValue ->
        if (!isShowTakeProducts && newValue > 0) {
            onFirstGetProducts(Unit)
            isShowTakeProducts = true
        }
    }

    val onFirstGetProducts = Event<Unit>()
    
    var showPrice by MutableStateDelegate(false)
    var price by MutableStateDelegate(0)

    var editSettings by MutableStateDelegate(false)

    fun getIcon() = when(getAge()) {
        Age.INDEPENDENT -> throw Exception("На этапе Самообеспечение нет рынка")
        Age.BARTER -> Res.drawable.level_2_market
        Age.INDUSTRY -> Res.drawable.level_2_market
        Age.CREDITING -> Res.drawable.level_2_market
        Age.FINISH -> Res.drawable.level_2_market
    }

    fun takeProducts(): Int {
        val result = products
        products = 0

        return result
    }
}