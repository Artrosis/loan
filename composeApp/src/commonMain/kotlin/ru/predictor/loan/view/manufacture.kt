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
import ru.predictor.loan.model.Manufacture

@Composable
@Preview
fun previewManufacture(){
    val model = Manufacture(
        onClick = { },
        getAge = { Age.INDEPENDENT },
    ).apply {
        products = 70
    }

    manufacture(model)
}

@Composable
fun manufacture(
    model: Manufacture,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .padding(16.dp)
            .clickable { 
                model.click()
            },
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        border = BorderStroke(width = 1.dp, color = Color.Gray),
        color = Color(0xA0FFFFFF),
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painterResource(model.getIcon()),
                null,
                modifier = modifier
                    .size(150.dp),
            )
            Text("Продукты: ${model.products}")
            if (model.showMoney) {
                Text("Деньги: ${model.money}")
            }
            if (model.showPrice) {
                Text("Цены: ${model.price}")
            }
            if (model.showSalary) {
                Text("Зарплаты: ${model.salary}")
            }
        }
    }
}