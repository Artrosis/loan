package ru.predictor.loan.model

import loaninterest.composeapp.generated.resources.*
import loaninterest.composeapp.generated.resources.Res
import ru.predictor.loan.utils.Event
import ru.predictor.loan.utils.MutableStateDelegate
import ru.predictor.loan.utils.ObservableStateDelegate

class Manufacture(
    val getAge: () -> Age,
) : Creditor() {
    var isShowTakeProducts = false
    var products by ObservableStateDelegate(0) { newValue ->
        if (!isShowTakeProducts && newValue > 0) {
            onFirstGetProducts(Unit)
            isShowTakeProducts = true
        }
    }

    val onFirstGetProducts = Event<Unit>()

    var hideProductsToMarket by MutableStateDelegate(false)

    var showPrice by MutableStateDelegate(false)
    var price by MutableStateDelegate(0)
    var showSalary by MutableStateDelegate(false)
    var salary by MutableStateDelegate(1.0)
    var efficiency by MutableStateDelegate(2.0)
    var editSettings by MutableStateDelegate(false)

    fun getIcon() = when (getAge()) {
        Age.INDEPENDENT -> Res.drawable.level_1_manuf_2
        Age.BARTER -> Res.drawable.level_2_manuf
        Age.INDUSTRY -> Res.drawable.level_3_manuf
        Age.CREDITING -> Res.drawable.level_3_manuf
        Age.FINISH -> Res.drawable.level_3_manuf
    }

    fun nextAddProduct(workersCount: Int) = (workersCount * efficiency).toInt()

    fun takeProducts(): Int {
        val result = products
        products = 0

        return result
    }
}