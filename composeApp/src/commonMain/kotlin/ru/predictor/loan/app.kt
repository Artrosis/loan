package ru.predictor.loan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DateTimeComponents.Companion.Format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.grass
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.predictor.loan.model.Model
import ru.predictor.loan.view.*

val DATE_FORMAT: DateTimeFormat<DateTimeComponents> = Format {
    year(); char(' '); monthNumber(); char(' '); dayOfMonth()
}

@Suppress("unused")
@Composable
@Preview
fun previewApp(){
    app(Model())
}

@Composable
fun app(model: Model) {
    MaterialTheme {
        Box(
            Modifier
                .fillMaxSize()
                .paint(
                    painterResource(Res.drawable.grass),
                    contentScale = ContentScale.FillBounds),
        ){
            Button(
                modifier = Modifier
                    .padding(16.dp),
                onClick = { model.tick() }) {
                Text("Следующий день")
            }
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopCenter),
                shape = RoundedCornerShape(corner = CornerSize(8.dp)),
                border = BorderStroke(width = 1.dp, color = Color.Gray),
                color = Color(0xA0FFD700),
            ) {
                Text(
                    text = "Этап: ${model.age.caption}",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 24.sp,
                )
            }
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd),
                text =  "Дата: ${model.time.format(DATE_FORMAT)}"
            )
            
            messageBox(model.messages)
            
            people(
                model.people,
                Modifier.align(Alignment.BottomEnd)
            )
            bank(
                model.bank,
                Modifier.align(Alignment.Center)
            )
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomStart),
                verticalAlignment = Alignment.Bottom
            ) {
                manufacture(
                    model.manufacture
                )
                AnimatedVisibility(model.manufacture.products > 0)
                {
                    Image(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Забрать товары",
                        modifier = Modifier
                            .size(96.dp)
                            .padding(
                                bottom = 16.dp
                            )
                            .clickable {
                                model.takeProductsFromManufactureToPeople()
                            },
                        colorFilter = ColorFilter.tint(color = Color(0xFFF0E68C)),
                    ) 
                }
            }            
            market(
                model.market,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}