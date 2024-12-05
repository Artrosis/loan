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
import loaninterest.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import ru.predictor.loan.model.Market

@Composable
fun market(
    model: Market,
    modifier: Modifier = Modifier,
) {
    if (model.has) {
        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            Image(
                painterResource(Res.drawable.compose_multiplatform),
                null,
                modifier = modifier
                    .size(150.dp),
            )
            Text("Деньги: ${model.money}")
            Text("Продукты: ${model.products}")
            Text("Цены: ${model.price}")
        }
    }
}