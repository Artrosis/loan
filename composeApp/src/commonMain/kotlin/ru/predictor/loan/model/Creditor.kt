package ru.predictor.loan.model

import ru.predictor.loan.utils.MutableStateDelegate

open class Creditor{
    var showCredit by MutableStateDelegate(false)
    var credit by MutableStateDelegate(0.0)
    var payment by MutableStateDelegate(0.0)

    var showMoney by MutableStateDelegate(false)
    var money by MutableStateDelegate(0.0)
}