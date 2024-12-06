package ru.predictor.loan.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.predictor.loan.model.Market

@Composable
fun market(
    model: Market,
    modifier: Modifier = Modifier,
) {
    if (model.has) {
        Surface(
            modifier = modifier
                .padding(16.dp),
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            border = BorderStroke(width = 1.dp, color = Color.Gray),
            color = Color(0xA0FFFFFF),
        ) {
            Column(
                modifier = modifier
                    .padding(16.dp)
            ) {
                Image(
                    painterResource(model.getIcon()),
                    null,
                    modifier = modifier
                        .size(150.dp),
                )
                if (model.showMoney) {
                    Text("Деньги: ${model.money}")
                }
                Text("Продукты: ${model.products}")
                if (model.showPrice) {
                    Text("Цены: ${model.price}")
                }
            }
        }
    }
}