package ru.predictor.loan

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.Event
import ru.predictor.loan.model.Model
import ru.predictor.loan.model.WindowsSizeValue

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val model = Model(WindowsSizeValue(window.innerWidth, window.innerHeight))

    window.addEventListener("resize") { _: Event ->
        model.resize(WindowsSizeValue(window.innerWidth, window.innerHeight))
    }

    ComposeViewport(document.body!!) {
        App(model)
    }
}