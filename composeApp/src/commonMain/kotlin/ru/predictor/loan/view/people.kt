package ru.predictor.loan.view

import androidx.compose.desktop.ui.tooling.preview.Preview
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.people
import org.jetbrains.compose.resources.painterResource
import ru.predictor.loan.model.People
import ru.predictor.loan.model.format

@Composable
@Preview
fun previewApp(){
    val model = People{}.apply {
        population = 315f
    }

    people(model)
}

@Composable
fun people(
    model: People,
    modifier: Modifier = Modifier,
) {
     Surface(
         modifier = modifier
             .padding(16.dp),
         shape = RoundedCornerShape(corner = CornerSize(16.dp)),
         border = BorderStroke(width = 1.dp, color = Color.Gray),
         color = model.state.color,
     ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Население: ${model.population.format()}")
            Image(
                painterResource(Res.drawable.people),
                null,
                modifier = Modifier
                    .size(150.dp)
            )
            Text("Еда: ${model.food}")
            if (model.showMoney) {
                Text("Деньги: ${model.money}")
            }
        }
    }    
}