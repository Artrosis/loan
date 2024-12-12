package ru.predictor.loan

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import ru.predictor.loan.model.Model

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val model = Model()
    ComposeViewport(document.body!!) {
        app(model)
    }
}