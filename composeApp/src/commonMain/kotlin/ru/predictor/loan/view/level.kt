package ru.predictor.loan.view

import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
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
    ) {
        Surface(
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            border = BorderStroke(width = 1.dp, color = Color.Gray),
            color = Color(0xA0e49c52),
        ) {
            Text(
                text = "Этап: ${model.levelMode.age.caption}",
                modifier = Modifier
                    .clickable {
                        model.aboutLevel()
                    }
                    .padding(8.dp),
                fontSize = 24.sp,
            )
        }
        verticalProgress(
            progress = model.populationProgress(),
            text = model.populationText(),
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 8.dp)
                .width(40.dp)
        )
    }
}

@Composable
fun verticalProgress(
    @FloatRange(from = 0.0, to = 1.0)
    progress: Float,
    text: String,
    modifier: Modifier = Modifier
) {
    val mProgress = animateFloatAsState(targetValue = maxOf(0f, minOf(progress, 1F)))
    
    val captionOffset = IntOffset(x = 0, y = -100)
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xB0E5E4E2))
        ) {
            Box(
                modifier = Modifier
                    .weight((if ((1 - mProgress.value) == 0.0F) 0.0001 else 1 - mProgress.value).toFloat())
                    .fillMaxWidth()
            ) {
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .weight(mProgress.value)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xff719baa),
                                Color(0xff719baa),
                                Color(0xff467b9c),
                                Color(0xff467b9c),
                            )
                        )
                    )
            ) {
            }
        }
        
        Text(
            text = text,
            modifier = Modifier
                .requiredWidth(400.dp)
                .offset { captionOffset }
                .rotate(-90F)
                .align(Alignment.Center)                
        )
    }
}
