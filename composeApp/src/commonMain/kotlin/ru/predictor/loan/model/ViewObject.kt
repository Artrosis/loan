package ru.predictor.loan.model

import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.IntSize
import ru.predictor.loan.utils.MutableStateDelegate

open class ViewObject: CheckMobile() {
    var coordinates by MutableStateDelegate<LayoutCoordinates?>(null)
    var size by MutableStateDelegate<IntSize?>(null)
}