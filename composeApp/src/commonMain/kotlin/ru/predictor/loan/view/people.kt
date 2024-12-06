package ru.predictor.loan.view

import androidx.annotation.FloatRange
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
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
            Text("Население")
            textProgressIndicator(
                progress = model.populationProgress(),
                text = model.populationText()
            )
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

@Composable
fun textProgressIndicator(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 1.0)
    progress: Float,
    text:String,
){
    Box(
        modifier = modifier.width(250.dp)
    ){
        LinearProgressIndicator(
            modifier = modifier.height(20.dp).align(Alignment.Center),
            progress = progress,
            color = Color.Green,
        )
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}