package ru.predictor.loan.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.predictor.loan.model.Model

@Composable
fun level(
    model: Model,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            border = BorderStroke(width = 1.dp, color = Color.Gray),
            color = Color(0xA0FFD700),
        ) {
            Text(
                text = "Этап: ${model.levelMode.age.caption}",
                modifier = Modifier.padding(8.dp),
                fontSize = 24.sp,
            )
        }

        textProgressIndicator(
            progress = model.populationProgress(),
            text = model.populationText(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}