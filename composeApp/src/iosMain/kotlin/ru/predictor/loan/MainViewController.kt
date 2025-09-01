package ru.predictor.loan

import androidx.compose.ui.window.ComposeUIViewController
import ru.predictor.loan.model.Model

fun MainViewController() = ComposeUIViewController { App(Model()) }