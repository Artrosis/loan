package ru.predictor.loan.model

import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.forest
import loaninterest.composeapp.generated.resources.forge
import loaninterest.composeapp.generated.resources.manuf
import ru.predictor.loan.utils.Event
import ru.predictor.loan.utils.MutableStateDelegate
import ru.predictor.loan.utils.ObservableStateDelegate

class Manufacture(
    val onClick: () -> Unit,
    val getAge: () -> Age,
): Creditor() {
    var isShowTakeProducts = false
    var products by ObservableStateDelegate(0){ newValue ->
        if (!isShowTakeProducts && newValue > 0) {
            onFirstGetProducts(Unit)
            isShowTakeProducts = true
        }
    }

    val onFirstGetProducts = Event<Unit>()
    
    var showPrice by MutableStateDelegate(false)
    var price by MutableStateDelegate(0)
    var showSalary by MutableStateDelegate(false)
    var salary by MutableStateDelegate(1.0)
    var efficiency by MutableStateDelegate(2.0)
    
    var editSettings by MutableStateDelegate(false)
    
    private val forestIcon = Res.drawable.forest
    private val forgeIcon = Res.drawable.forge
    private val manufactureIcon = Res.drawable.manuf
    
    fun getIcon() = when(getAge()) {
        Age.INDEPENDENT -> forestIcon
        Age.BARTER -> forgeIcon
        Age.INDUSTRY -> manufactureIcon
        Age.CREDITING -> manufactureIcon
        Age.FINISH -> manufactureIcon
    }

    fun nextAddProduct(workersCount: Int) = (workersCount * efficiency).toInt()

    fun click() {
        onClick()
    }

    fun takeProducts(): Int {
        val result = products
        products = 0

        return result
    }
}