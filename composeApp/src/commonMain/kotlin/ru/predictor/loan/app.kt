package ru.predictor.loan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
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

val marketAlignment: Alignment = BiasAlignment(0f, -0.5f)

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
                .rotate(40f),
            onMove = {model.takeProductsFromMarketToPeople()}
        )
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