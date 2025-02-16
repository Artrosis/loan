package ru.predictor.loan.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.predictor.loan.model.Age
import ru.predictor.loan.model.People
import ru.predictor.loan.utils.toCaption

@Composable
@Preview
fun previewApp() {
    val model = People({}, { Age.INDEPENDENT }).apply {
        population = 315f
    }

    people(model)
}

@Composable
fun people(
    model: People,
    modifier: Modifier = Modifier,
) {
    model.editSettings()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(model.getIcon()),
            null,
            modifier = Modifier
                .clickable {
                    model.editSettings = true
                }
                .size(if (model.isMobile) 70.dp else 200.dp)
        )
        Surface(
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            border = BorderStroke(width = 1.dp, color = Color.Gray),
            color = model.state.color,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        model.editSettings = true
                    }.padding(8.dp)
            ) {
                Text("Продукты: ${model.products.toCaption()}")
                if (model.showMoney) {
                    Text("Деньги: ${model.money.toCaption()}")
                }

                model.creditText()
            }
        }
    }
}
