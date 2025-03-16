package ru.predictor.loan

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.level_all_background
import loaninterest.composeapp.generated.resources.money
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ru.predictor.loan.images.Telegram
import ru.predictor.loan.model.Hint
import ru.predictor.loan.model.Model
import ru.predictor.loan.model.MoveableAnimation
import ru.predictor.loan.model.modes.BarterMode
import ru.predictor.loan.model.modes.IndependentMode
import ru.predictor.loan.model.modes.IndustryMode
import ru.predictor.loan.utils.animateIntOffsetToTarget
import ru.predictor.loan.utils.calcFullOffset
import ru.predictor.loan.view.*

@Suppress("unused")
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
        /*levelMode = CreditingMode()
        initialization()*/
        hint.clear()
        hintQueue.clear()
    }

    app(model)
}

val peopleOffset: Density.() -> IntOffset = { IntOffset(-380, 130) }
val manufactureOffset: Density.() -> IntOffset = { IntOffset(440, 150) }
val marketOffset: Density.() -> IntOffset = { IntOffset(-130, -230) }
val bankOffset: Density.() -> IntOffset = { IntOffset(50, -10) }
val bankMoneyOffset: Density.() -> IntOffset = { IntOffset(0, 30) }
val moneyToMarketOffset: Density.() -> IntOffset = { IntOffset(-40, -80) }
val moneyToPeopleOffset: Density.() -> IntOffset = { IntOffset(-40, 10) }
val moneyToManufactureOffset: Density.() -> IntOffset = { IntOffset(170, 40) }
val movePeopleWorkOffset: Density.() -> IntOffset = { IntOffset(-250, 110) }
val moveProductsFromManufactureToPeopleOffset: Density.() -> IntOffset = { IntOffset(260, 100) }
val moveProductsFromManufactureToMarketOffset: Density.() -> IntOffset = { IntOffset(280, 120) }
val moveProductsFromMarketToPeopleOffset: Density.() -> IntOffset = { IntOffset(-220, -220) }

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

            level(
                model,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .fillMaxHeight()
            )

            telegramGroup(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd)
            )

            hint(model.hint)
        }
    }
}

@Composable
fun telegramGroup(
    modifier: Modifier = Modifier,
){
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current
    Icon(
        imageVector = Telegram,
        "Телеграмм",
        modifier
            .size(40.dp)
            .padding(4.dp)
            .clickable {
                scope.launch {
                    uriHandler.openUri("https://t.me/c/2363429679/769")
                }
            },
        tint = Color(0xFF039be5)
    )
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
    animatedMove(
        modifier,
        model.moneyToMarket,
    )
}

@Composable
fun animatedMove(
    modifier: Modifier = Modifier,
    model: MoveableAnimation
){
    val coroutineScope = rememberCoroutineScope()
    if (model.isVisible() && !model.hideForReturn)
    {
        var selfCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

        val offset by selfCoordinates.animateIntOffsetToTarget(
            model.isAnimated,
            model.target,
        ){
            coroutineScope.launch {
                model.onFinishAnimation()
            }
        }

        move(
            model.icon,
            modifier = modifier
                .offset {
                    offset
                }
                .onGloballyPositioned { coordinates ->
                    selfCoordinates = coordinates
                }
        ) {
            model.startAnimation()
        }
    }
}

@Composable
fun exchangeWithMoney(
    modifier: Modifier = Modifier,
    model: MoveableAnimation,
    hasMoney: Boolean,
){
    animatedMove(
        modifier,
        model
    )
    
    if (hasMoney && model.isAnimated)
    {
        var moneyCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

        val moneyOffset by moneyCoordinates.animateIntOffsetToTarget(
            model.isAnimated,
            model.target,
        ){}
        
        val fullOffset = calcFullOffset(moneyCoordinates, model.target.coordinates, model.target.size)
            
        Image(
            painterResource(Res.drawable.money),
            contentDescription = "Забрать товары",
            modifier = modifier
                .offset {
                    fullOffset - moneyOffset
                }
                .onGloballyPositioned { coordinates ->
                    moneyCoordinates = coordinates
                }
                .size(60.dp)
        )
    }
}

@Composable
fun manufactureTakeMoney(
    modifier: Modifier = Modifier,
    model: Model
) {
    animatedMove(
        modifier,
        model.moneyToManufacture,
    )
}

@Composable
fun peopleTakeMoney(
    modifier: Modifier = Modifier,
    model: Model
) {
    animatedMove(
        modifier,
        model.moneyToPeople,
    )
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
    exchangeWithMoney(
        modifier,
        model.workPeople,
        model.bank.has
    )
}

@Composable
fun moveProductsFromMarketToPeople(
    modifier: Modifier = Modifier,
    model: Model
) {
    exchangeWithMoney(
        modifier,
        model.productsFromMarketToPeople,
        model.bank.has
    )
}

@Composable
fun moveProductsFromManufactureToMarket(
    modifier: Modifier = Modifier,
    model: Model
) {
    exchangeWithMoney(
        modifier,
        model.productsToMarket,
        model.bank.has
    )
}

@Composable
fun moveProductsFromManufactureToPeople(
    modifier: Modifier = Modifier,
    model: Model,
) {
    animatedMove(
        modifier,
        model.productsFromManufactureToPeople,
    )
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