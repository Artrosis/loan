package ru.predictor.loan

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.*
import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.icon
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ru.predictor.loan.model.Model

val model = Model()

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(
            width = 1200.dp,
            height = 800.dp
        ),
        title = "Ссудный процент",
        icon = painterResource(Res.drawable.icon)
    ) {
        val backgroundVolumeScope = rememberCoroutineScope()
        backgroundVolumeScope.launch {
            model.playBackgroundVolume()
        }
        app(model)
    }
}