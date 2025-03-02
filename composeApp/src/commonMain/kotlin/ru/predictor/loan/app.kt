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
import kotlinx.coroutines.launch
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

val peopleOffset: Density.() -> IntOffset = { IntOffset(-380, 130) }
val manufactureOffset: Density.() -> IntOffset = { IntOffset(440, 150) }
val marketOffset: Density.() -> IntOffset = { IntOffset(-130, -230) }
val bankOffset: Density.() -> IntOffset = { IntOffset(50, -10) }
val bankMoneyOffset: Density.() -> IntOffset = { IntOffset(0, 150) }
val moneyToMarketOffset: Density.() -> IntOffset = { IntOffset(-30, -80) }
val moneyToPeopleOffset: Density.() -> IntOffset = { IntOffset(-60, 0) }
val moneyToManufactureOffset: Density.() -> IntOffset = { IntOffset(100, 70) }
val movePeopleWorkOffset: Density.() -> IntOffset = { IntOffset(-250, 110) }
val moveProductsFromManufactureToPeopleOffset: Density.() -> IntOffset = { IntOffset(260, 100) }
val moveProductsFromManufactureToMarketOffset: Density.() -> IntOffset = { IntOffset(300, 120) }
val moveProductsFromMarketToPeopleOffset: Density.() -> IntOffset = { IntOffset(-210, -230) }

@Composable
fun app(model: Model) {
    MaterialTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.6f))
                .paint(
                    painterResource(Res.drawable.level_all_background),
                    contentScale = FixedScale(0.8f)
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

            if (model.bank.has) {
                bank(
                    model.bank,
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
                bankMoney(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(bankMoneyOffset),
                    model
                )                
                marketTakeMoney(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(moneyToMarketOffset),
                    model
                )
                peopleTakeMoney(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(moneyToPeopleOffset),
                    model
                )
                manufactureTakeMoney(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(moneyToManufactureOffset),
                    model
                )
            }

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
fun bankMoney(
    modifier: Modifier = Modifier,
    model: Model
) {
    if (model.snowBankMoney()) {
        move(
            model.moneyIcon(),
            modifier = modifier
        ) {
            model.startDistributeMoney()
        }
    }

    //Иконки показывающие перенос денег
    
    //к магазину
    if (model.movedMoneyFromBankToAll)
    {
        var selfCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

        val offset by selfCoordinates.animateIntOffsetToTarget(
            model.isAnimatedMoveMoney,
            model.market
        )
        {
            model.isAnimatedMoveMoney = false
            model.finishedMoneyFromBankToAll()
        }
        move(
            Res.drawable.money,
            modifier = modifier
                .offset {
                    offset
                }
                .onGloballyPositioned { coordinates ->
                    selfCoordinates = coordinates
                }
        ){}
    }
    //к заводу
    if (model.movedMoneyFromBankToAll)
    {
        var selfCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

        val offset by selfCoordinates.animateIntOffsetToTarget(
            model.isAnimatedMoveMoney,
            model.manufacture
        )
        {
            model.isAnimatedMoveMoney = false
            model.finishedMoneyFromBankToAll()
        }
        move(
            Res.drawable.money,
            modifier = modifier
                .offset {
                    offset
                }
                .onGloballyPositioned { coordinates ->
                    selfCoordinates = coordinates
                }
        ){}
    }
    //к населению
    if (model.movedMoneyFromBankToAll)
    {
        var selfCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

        val offset by selfCoordinates.animateIntOffsetToTarget(
            model.isAnimatedMoveMoney,
            model.people
        )
        {
            model.isAnimatedMoveMoney = false
            model.finishedMoneyFromBankToAll()
        }
        move(
            Res.drawable.money,
            modifier = modifier
                .offset {
                    offset
                }
                .onGloballyPositioned { coordinates ->
                    selfCoordinates = coordinates
                }
        ){}
    }
}

@Composable
fun marketTakeMoney(
    modifier: Modifier = Modifier,
    model: Model
) {
    if (model.canMarketTakeMoneyFromBank())
    {
        val coroutineScope = rememberCoroutineScope()
        var selfCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

        val offset by selfCoordinates.animateIntOffsetToTarget(
            model.movedMoneyFromBankToMarket,
            model.market
        )
        {
            coroutineScope.launch {
                model.finishedMoveMoneyFromBankToMarket()
            }
        }
        move(
            Res.drawable.money,
            modifier = modifier
                .offset {
                    offset
                }
                .onGloballyPositioned { coordinates ->
                    selfCoordinates = coordinates
                }
        ) {
            model.moveMoneyFromBankToMarket()
        }
    }
}

@Composable
fun manufactureTakeMoney(
    modifier: Modifier = Modifier,
    model: Model
) {
    if (model.canManufactureTakeMoneyFromBank())
    {
        val coroutineScope = rememberCoroutineScope()
        var selfCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

        val offset by selfCoordinates.animateIntOffsetToTarget(
            model.movedMoneyFromBankToManufacture,
            model.manufacture
        )
        {
            coroutineScope.launch {
                model.finishedMoveMoneyFromBankToManufacture() 
            }
        }
        move(
            Res.drawable.money,
            modifier = modifier
                .offset {
                    offset
                }
                .onGloballyPositioned { coordinates ->
                    selfCoordinates = coordinates
                }
        ) {
            model.moveMoneyFromBankToManufacture()
        }
    }
}

@Composable
fun peopleTakeMoney(
    modifier: Modifier = Modifier,
    model: Model
) {
    if (model.canPeopleTakeMoneyFromBank())
    {
        val coroutineScope = rememberCoroutineScope()
        var selfCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

        val offset by selfCoordinates.animateIntOffsetToTarget(
            model.movedMoneyFromBankToPeople,
            model.people
        )
        {
            coroutineScope.launch {
                model.finishedMoveMoneyFromBankToPeople() 
            }
        }
        move(
            Res.drawable.money,
            modifier = modifier
                .offset {
                    offset
                }
                .onGloballyPositioned { coordinates ->
                    selfCoordinates = coordinates
                }
        ) {
            model.moveMoneyFromBankToPeople()
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

        var selfCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

        val offset by selfCoordinates.animateIntOffsetToTarget(
            model.movedProductsFromMarketToPeople,
            model.people
        )
        {
            model.finishedMoveProductsFromMarketToPeople()
        }

        move(
            Res.drawable.level_2_wood,
            modifier = Modifier
                .offset {
                    offset
                }
                .onGloballyPositioned { coordinates ->
                    selfCoordinates = coordinates
                }
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
    val coroutineScope = rememberCoroutineScope()
    if (model.showProductsFromManufactureToMarket())
    {
        var selfCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

        val offset by selfCoordinates.animateIntOffsetToTarget(
            model.movedProductsFromManufactureToMarket,
            model.market
        )
        {
            coroutineScope.launch {
                model.finishedMoveProductsFromManufactureToMarket()
            }
        }

        move(
            Res.drawable.level_2_wood,
            modifier = modifier
                .offset {
                    offset
                }
                .onGloballyPositioned { coordinates ->
                    selfCoordinates = coordinates
                }
        )
        { model.moveProductsFromManufactureToMarket() }
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