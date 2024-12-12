package ru.predictor.loan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
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
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
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
import ru.predictor.loan.model.Hint
import ru.predictor.loan.model.Model
import ru.predictor.loan.model.modes.BarterMode
import ru.predictor.loan.view.*

val DATE_FORMAT: DateTimeFormat<DateTimeComponents> = Format {
    year(); char(' '); monthNumber(); char(' '); dayOfMonth()
}

@Composable
@Preview
fun previewApp(){
    val model = Model().apply {
        messages.clear()
        levelMode = BarterMode()
    }

    model.levelMode.initModel(model)
    
    app(model)
}

val marketAlignment: Alignment = BiasAlignment(0f, -0.5f)
val bankAlignment: Alignment = BiasAlignment(0f, 0.45f)

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
            messageBox(model.messages)
            
            level(
                model,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter)
            )
            
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd),
                text = "Дата: ${model.time.format(DATE_FORMAT)}"
            )
            
            people(
                model.people,
                Modifier.align(Alignment.BottomEnd)
            )
                           
            bank(
                model.bank,
                modifier = Modifier
                    .align(bankAlignment),
            )
            
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomStart),
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    moveProductsFromManufactureToMarket(
                        modifier = Modifier.align(Alignment.End),
                        model
                    )
                    manufacture(
                        model.manufacture
                    )
                }
                moveProductsFromManufactureToPeople(model)
            }
            Row(
                modifier = Modifier.align(marketAlignment)
            ) {
                market(
                    model.market
                )
                moveProductsFromMarketToPeople(
                    Modifier.align(Alignment.Bottom),
                    model
                )
            }
            
            hint(model.hint)
        }
    }
}

@Composable
fun BoxScope.hint(
    model: Hint
) {
    if (
        model.message.isEmpty()
        || model.disable
    ) return

    Surface(
        modifier = Modifier.align(model.alignment),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.Gray),
        color = Color(0x80FFFFFF),
    ) {
        Column {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                model.message.forEach {
                    Text(
                        modifier = Modifier.padding(2.dp),
                        text = it,
                    )
                }
            }

            Row(
                modifier = Modifier.align(Alignment.End).padding(4.dp),
            ) {

                Button(
                    onClick = {model.disable = true}
                ){
                    Text(
                        text = "Отключить подсказки",
                        fontSize = 10.sp
                    )
                }
                
                Button(
                    modifier = Modifier
                        .padding(start = 4.dp),
                    onClick = {model.confirm()}
                ){
                    Text(
                        text = model.buttonText,
                    )
                }
            }
        }
    }
}

@Composable
fun moveProductsFromMarketToPeople(
    modifier: Modifier = Modifier,
    model: Model
) {
    AnimatedVisibility(
        model.market.products > 0,
        modifier = modifier
    ){
        move(
            modifier = Modifier
                .rotate(40f)
        ){
            model.takeProductsFromMarketToPeople()
        }
        
    }
}

@Composable
fun moveProductsFromManufactureToMarket(
    modifier: Modifier = Modifier,
    model: Model
) {
    AnimatedVisibility(
        model.manufacture.products > 0
                && model.levelMode.canMoveProductsFromManufactureToMarket,
        modifier = modifier
    ){
        move(
            modifier = Modifier
                .rotate(-40f),
            onMove = {model.takeProductsFromManufactureToMarket()}
        )
    }
}

@Composable
fun moveProductsFromManufactureToPeople(
    model: Model
) {
    AnimatedVisibility(
        model.manufacture.products > 0
                && model.levelMode.canMoveProductsFromManufactureToPeople
    ){
        move{
            model.takeProductsFromManufactureToPeople()
        }
    }
}

@Composable
fun move(
    modifier: Modifier = Modifier,
    onMove: () -> Unit
) {
    Image(
        imageVector = Icons.Filled.PlayArrow,
        contentDescription = "Забрать товары",
        modifier = modifier
            .size(96.dp)
            .padding(
                bottom = 16.dp
            )
            .clickable(
                onClick = onMove
            ),
        colorFilter = ColorFilter.tint(color = Color(0xFFF0E68C)),
    )
}