package ru.predictor.loan.view

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun textProgressIndicator(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 1.0)
    progress: Float,
    text:String,
){
    Box(
        modifier = modifier            
    ){
        LinearProgressIndicator(
            modifier = modifier
                .height(40.dp)
                .align(Alignment.Center),
            progress = progress,
            color = Color.Green,
        )
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}