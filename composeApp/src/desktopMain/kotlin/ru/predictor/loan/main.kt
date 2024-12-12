package ru.predictor.loan

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.bank
import org.jetbrains.compose.resources.painterResource
import ru.predictor.loan.model.Model

val model = Model()

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(
            width = 1200.dp,
            height = 800.dp
        ),
        title = "Ссудный процент",
        icon = painterResource(Res.drawable.bank)
    ) {
        app(model)
    }
}