package ru.predictor.loan.model

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import ru.predictor.loan.utils.MutableStateDelegate

open class MoveableAnimation(
    val target: ViewObject,
    val icon: DrawableResource,
    val isVisible: () -> Boolean,
    val onFinishAction: () -> Unit,
): CheckMobile() {
    var isAnimated by MutableStateDelegate(false)
    
    fun startAnimation() {
        isAnimated = true
    }

    internal var hideForReturn by MutableStateDelegate(false)

    suspend fun onFinishAnimation() = coroutineScope {
        isAnimated = false
        hideForReturn = true

        onFinishAction()

        launch {
            delay(100L)
            hideForReturn = false
        }
    }  
}