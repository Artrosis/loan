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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import loaninterest.composeapp.generated.resources.*
import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.level_all_background
import loaninterest.composeapp.generated.resources.money
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ru.predictor.loan.model.Hint
import ru.predictor.loan.model.Model
import ru.predictor.loan.model.modes.BarterMode
import ru.predictor.loan.model.modes.CreditingMode
import ru.predictor.loan.model.modes.IndependentMode
import ru.predictor.loan.model.modes.IndustryMode
import ru.predictor.loan.utils.animateIntOffsetToTarget
import ru.predictor.loan.view.*

@Composable
@Preview
fun previewApp() {
    val model = Model().apply {
        messages.clear()
        levelMode = IndependentMode()
        initialization()
        manufacture.products = 13
        market.products = 50
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

val peopleOffset: Density.() -> IntOffset = { IntOffset(-350, 100) }
val movePeopleWorkOffset: Density.() -> IntOffset = { IntOffset(-270, 100) }
val bankOffset: Density.() -> IntOffset = { IntOffset(40, -20) }
val marketOffset: Density.() -> IntOffset = { IntOffset(-60, -230) }
val manufactureOffset: Density.() -> IntOffset = { IntOffset(280, 100) }
val moveProductsFromManufactureToPeopleOffset: Density.() -> IntOffset = { IntOffset(200, 100) }
val moveProductsFromManufactureToMarketOffset: Density.() -> IntOffset = { IntOffset(200, 50) }
val moveProductsFromMarketToPeopleOffset: Density.() -> IntOffset = { IntOffset(-150, -240) }

@Composable
fun app(model: Model) {
    MaterialTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.6f))
                .paint(
                    painterResource(Res.drawable.level_all_background),
                    contentScale = FixedScale(0.7f)
                ),
        ) {
            messageBox(model.messages)

            people(
                model.people,
                Modifier
                    .align(Alignment.Center)
                    .offset(peopleOffset)
                    .onGloballyPositioned {
                        model.people.coordinates = it
                    }
                    .onSizeChanged {
                        model.people.size = it
                    }
            )

            movePeopleWork(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(movePeopleWorkOffset),
                model,
            )

            bankWithActions(
                model,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(bankOffset)
                    .onGloballyPositioned {
                        model.bank.coordinates = it
                    }
                    .onSizeChanged {
                        model.bank.size = it
                    },
            )

            manufacture(
                model.manufacture,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(manufactureOffset)
                    .onGloballyPositioned {
                        model.manufacture.coordinates = it
                    }
                    .onSizeChanged {
                        model.manufacture.size = it
                    },
            )

            moveProductsFromManufactureToPeople(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(moveProductsFromManufactureToPeopleOffset),
                model,
            )

            moveProductsFromManufactureToMarket(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(moveProductsFromManufactureToMarketOffset),
                model,
            )

            market(
                model.market,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(marketOffset)
                    .onGloballyPositioned {
                        model.market.coordinates = it
                    }
                    .onSizeChanged {
                        model.market.size = it
                    },
            )

            moveProductsFromMarketToPeople(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(moveProductsFromMarketToPeopleOffset),
                model,
            )

            level(
                model,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .fillMaxHeight()
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
        Res.drawable.money,
        modifier = modifier
    ) {
        model.marketTakeMoney()
    }
}

@Composable
fun manufactureTakeMoney(
    modifier: Modifier = Modifier,
    model: Model
) {
    move(
        Res.drawable.money,
        modifier = modifier
    ) {
        model.manufactureTakeMoney()
    }
}

@Composable
fun peopleTakeMoney(
    modifier: Modifier = Modifier,
    model: Model
) {
    move(
        Res.drawable.money,
        modifier = modifier
    ) {
        model.peopleTakeMoney()
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
                    onClick = { model.disable = true }
                ) {
                    Text(
                        text = "Отключить подсказки",
                        fontSize = 10.sp
                    )
                }
                Button(
                    modifier = Modifier
                        .padding(start = 4.dp),
                    onClick = { model.confirm() }
                ) {
                    Text(
                        text = model.buttonText,
                    )
                }
            }
        }
    }
}

@Composable
fun movePeopleWork(
    modifier: Modifier = Modifier,
    model: Model
) {
    move(
        Res.drawable.woodcutter_out,
        modifier = modifier
    ) {
        model.manufacture.click()
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
    ) {
        move(
            Res.drawable.level_2_wood,
            modifier = Modifier
        ) {
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
    ) {
        move(
            Res.drawable.level_2_wood,
            modifier = Modifier,
            onMove = { model.moveProductsFromManufactureToMarket() }
        )
    }
}

@Composable
fun moveProductsFromManufactureToPeople(
    modifier: Modifier = Modifier,
    model: Model,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = model.manufacture.products > 0
                && model.levelMode.canMoveProductsFromManufactureToPeople
    ) {
        var selfCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

        val offset by selfCoordinates.animateIntOffsetToTarget(
            model.movedProductsFromManufactureToPeople,
            model.people
        )
        {
            model.finishedMoveProductsFromManufactureToPeople()
        }
        move(
            Res.drawable.woodcutter_back,
            modifier = Modifier
                .offset {
                    offset
                }
                .onGloballyPositioned { coordinates ->
                    selfCoordinates = coordinates
                }
        ) {
            model.moveProductsFromManufactureToPeople()
        }
    }
}

@Composable
fun move(
    icon: DrawableResource,
    modifier: Modifier = Modifier,
    onMove: () -> Unit
) {
    Image(
        painterResource(icon),
        contentDescription = "Забрать товары",
        modifier = modifier
            .size(60.dp)
            .clickable(
                onClick = onMove
            )
    )
}