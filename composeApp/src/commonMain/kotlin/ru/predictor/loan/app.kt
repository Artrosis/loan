package ru.predictor.loan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.grass
import org.jetbrains.compose.resources.painterResource
import ru.predictor.loan.model.Hint
import ru.predictor.loan.model.Model
import ru.predictor.loan.model.modes.BarterMode
import ru.predictor.loan.model.modes.CreditingMode
import ru.predictor.loan.model.modes.IndependentMode
import ru.predictor.loan.model.modes.IndustryMode
import ru.predictor.loan.view.*

@Composable
@Preview
fun previewApp(){
    val model = Model().apply {
        messages.clear()
        levelMode = IndependentMode()
        initialization()
        manufacture.products = 13
        levelMode = BarterMode()
        initialization()
        levelMode = IndustryMode()
        initialization()
        messages.clear()
        levelMode = CreditingMode()
        initialization()
        hint.clear()
        hintQueue.clear()
    }
    
    app(model)
}

val marketAlignment: Alignment = BiasAlignment(0f, -0.5f)
val bankAlignment: Alignment = BiasAlignment(0f, 0.7f)

@Composable
fun app(model: Model) {
    MaterialTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background( Color.Green.copy(alpha = 0.6f))
                .paint(
                    painterResource(Res.drawable.grass),
                    contentScale = if (model.isMobile) ContentScale.FillHeight else ContentScale.FillBounds),
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
                text = "Дата: ${model.date}"
            )
            
            people(
                model.people,
                Modifier.padding(16.dp).align(Alignment.BottomEnd)
            )
            
            bankWithActions(
                model,
                modifier = Modifier
                    .align(bankAlignment),
            )

            manufactureWithAction(
                model,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomStart),
            )

            marketWithAction(
                model,
                modifier = Modifier.align(marketAlignment),
            )            
            
            hint(model.hint)
        }
    }
}

@Composable
fun bankWithActions(
    model: Model, 
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (model.bank.has) {
            AnimatedVisibility(model.levelMode.canTakeMoneyFromBank)
            {
                marketTakeMoney(
                    Modifier.align(Alignment.CenterHorizontally),
                    model
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                AnimatedVisibility(model.levelMode.canTakeMoneyFromBank)
                {
                    manufactureTakeMoney(
                        Modifier.align(Alignment.Bottom),
                        model
                    )
                }
                bank(
                    model.bank,
                )
                AnimatedVisibility(model.levelMode.canTakeMoneyFromBank)
                {
                    peopleTakeMoney(
                        Modifier.align(Alignment.Bottom),
                        model
                    )
                }
            }
        }
    }
}

@Composable
fun marketTakeMoney(
    modifier: Modifier = Modifier,
    model: Model
) {
    move(
        modifier = modifier
            .rotate(-90f)
    ){
        model.marketTakeMoney()
    }
}

@Composable
fun manufactureTakeMoney(
    modifier: Modifier = Modifier,
    model: Model
) {
    move(
        modifier = modifier
            .rotate(180f)
    ){
        model.manufactureTakeMoney()
    }
}

@Composable
fun peopleTakeMoney(
    modifier: Modifier = Modifier,
    model: Model
) {
    move(
        modifier = modifier
    ){
        model.peopleTakeMoney()
    }
}

@Composable
fun manufactureWithAction(
    model: Model,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
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
}

@Composable
fun marketWithAction(
    model: Model,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(16.dp)
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
            model.moveProductsFromMarketToPeople()
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
            onMove = {model.moveProductsFromManufactureToMarket()}
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
            model.moveProductsFromManufactureToPeople()
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
            .size(60.dp)
            .clickable(
                onClick = onMove
            ),
        colorFilter = ColorFilter.tint(color = Color(0xFFF0E68C)),
    )
}