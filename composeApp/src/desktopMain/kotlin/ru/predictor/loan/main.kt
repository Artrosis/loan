package ru.predictor.loan

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.icon
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ru.predictor.loan.model.Model
import ru.predictor.loan.model.WindowsSizeValue

val model = Model(WindowsSizeValue(1200, 800))

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    val windowState = rememberWindowState(
        width = 1200.dp,
        height = 800.dp
    )
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Ссудный процент",
        icon = painterResource(Res.drawable.icon),
    ) {
        LaunchedEffect(windowState) {
            snapshotFlow { windowState.size }
                .onEach { onWindowResize(it) }
                .launchIn(this) // или remember { mutableStateOf(WindowState()) }
        }

        val backgroundVolumeScope = rememberCoroutineScope()
        backgroundVolumeScope.launch {
            model.playBackgroundVolume()
        }
        App(model)

    }
}

private fun onWindowResize(size: DpSize) {
    model.resize(WindowsSizeValue(size.width.value.toInt(), size.height.value.toInt()))
}