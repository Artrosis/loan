package ru.predictor.loan.utils

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import ru.predictor.loan.model.ViewObject

@Composable
fun LayoutCoordinates?.animateIntOffsetToTarget(
    isAnimated: Boolean,
    targetObject: ViewObject,
    onFinished: ((IntOffset) -> Unit)
): State<IntOffset> {
    return animateIntOffsetAsState(
        targetValue = if (isAnimated)
        {
            val source = this
            val target = targetObject.coordinates
            val targetSize = targetObject.size

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
        } else {
            IntOffset.Zero
        },
        label = "offset",
        finishedListener = onFinished,
        animationSpec = tween(
            easing = FastOutSlowInEasing,
            durationMillis = 500,
        )
    )
}