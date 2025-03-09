package ru.predictor.loan.utils

import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

fun calcFullOffset(source: LayoutCoordinates?, target: LayoutCoordinates?, targetSize: IntSize?) =
    if (source == null || target == null || targetSize == null) {
        IntOffset.Zero
    } else {
        val targetPosition = target.positionInWindow()
        val sourcePosition = source.positionInWindow()

        val targetX = targetPosition.x.toInt() + targetSize.width / 2
        val targetY = targetPosition.y.toInt() + targetSize.height / 2

        val dx = targetX - sourcePosition.x.toInt()
        val dy = targetY - sourcePosition.y.toInt()

        IntOffset(dx, dy)
    }