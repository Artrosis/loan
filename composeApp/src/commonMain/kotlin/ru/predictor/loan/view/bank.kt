package ru.predictor.loan.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.bank
import org.jetbrains.compose.resources.painterResource
import ru.predictor.loan.model.Bank

@Composable
fun bank(
    model: Bank,
    modifier: Modifier = Modifier,
) {
    if (model.has) {
        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            Image(
                painterResource(Res.drawable.bank),
                null,
                modifier = modifier
                    .size(150.dp),
            )
            Text("Ссудный процент: ${model.loanInterest}")
            Text("Деньги: ${model.money}")
        }
    }
}