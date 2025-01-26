package ru.predictor.loan.model

import ru.predictor.loan.isMobile
import ru.predictor.loan.utils.MutableStateDelegate

open class CheckMobile {
    var isMobile by MutableStateDelegate(isMobile())
}