package ru.predictor.loan.model

import ru.predictor.loan.Platform
import ru.predictor.loan.getPlatform
import ru.predictor.loan.isMobile
import ru.predictor.loan.utils.MutableStateDelegate

open class CheckMobile(
    val platform: Platform = getPlatform(),
) : Platform by platform {
    var isMobile by MutableStateDelegate(isMobile())
}