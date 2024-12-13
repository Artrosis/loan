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
){
    var isShowTakeProducts = false
    var products by ObservableStateDelegate(0){ newValue ->
        if (!isShowTakeProducts && newValue > 0) {
            onFirstGetProducts(Unit)
            isShowTakeProducts = true
        }
    }

    val onFirstGetProducts = Event<Unit>()
    
    var showMoney by MutableStateDelegate(false)
    var money by MutableStateDelegate(0.0)
    var showPrice by MutableStateDelegate(false)
    var price by MutableStateDelegate(0)
    var showSalary by MutableStateDelegate(false)
    var salary by MutableStateDelegate(0)

    var showCredit by MutableStateDelegate(false)
    var credit by MutableStateDelegate(0.0)
    var payment by MutableStateDelegate(0.0)

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

    fun nextAddProduct(workersCount: Int) = workersCount * 2

    fun click() {
        onClick()
    }

    fun takeProducts(): Int {
        val result = products
        products = 0

        return result
    }
}