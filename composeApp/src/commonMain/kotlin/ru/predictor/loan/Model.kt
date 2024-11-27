package ru.predictor.loan

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.datetime.Clock
import kotlin.reflect.KProperty
import kotlin.time.Duration.Companion.days

class MutableStateDelegate<T>(private var valueProp: MutableState<T>) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return valueProp.value
    }
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        valueProp.value = value
    }
}

class Model {

    private var timeProp = mutableStateOf(Clock.System.now())
    var time by MutableStateDelegate(timeProp)
    
    fun tick() {
        time += 1.days
        people.tick()
        market.tick()
        manufacture.tick()
        bank.tick()
    }

    val people = People()
    val market = Market()
    val manufacture = Manufacture()
    val bank = Bank()
}

class People{
    private var moneyProp = mutableStateOf(0)
    var money by MutableStateDelegate(moneyProp)

    fun tick() {
        money += 10
    }
}

class Market{
    private var moneyProp = mutableStateOf(0)
    var money by MutableStateDelegate(moneyProp)

    private var productsProp = mutableStateOf(0)
    var products by MutableStateDelegate(productsProp)

    private var priceProp = mutableStateOf(0)
    var price by MutableStateDelegate(priceProp)

    fun tick() {
        money += 10
    }
}

class Manufacture{
    private var moneyProp = mutableStateOf(0)
    var money by MutableStateDelegate(moneyProp)

    private var productsProp = mutableStateOf(0)
    var products by MutableStateDelegate(productsProp)

    private var priceProp = mutableStateOf(0)
    var price by MutableStateDelegate(priceProp)

    private var salaryProp = mutableStateOf(0)
    var salary by MutableStateDelegate(salaryProp)

    fun tick() {
        money += 10
    }
}

class Bank{
    //Ссудный процент
    private var loanInterestProp = mutableStateOf(1.0)
    var loanInterest by MutableStateDelegate(loanInterestProp)
    
    private var moneyProp = mutableStateOf(0)
    var money by MutableStateDelegate(moneyProp)

    fun tick() {
        money += 10
    }
}