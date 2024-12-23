package ru.predictor.loan.view

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.predictor.loan.model.Creditor

@Composable
fun Creditor.creditText(
    modifier: Modifier = Modifier,
) {
    val creditor = this
    if (creditor.showCredit) {
        Row(
            modifier = modifier
        ) {
            Text("${creditor.credit} (")
            Text("-${creditor.payment}", color = Color.Red)
            Text(")")
        }
    }
}