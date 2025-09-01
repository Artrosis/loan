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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalUriHandler
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
import ru.predictor.loan.model.WindowsSizeValue
import ru.predictor.loan.model.modes.BarterMode
import ru.predictor.loan.model.modes.IndependentMode
import ru.predictor.loan.model.modes.IndustryMode
import ru.predictor.loan.utils.animateIntOffsetToTarget
import ru.predictor.loan.utils.calcFullOffset
import ru.predictor.loan.view.*

@Suppress("unused")
@Composable
@Preview
fun PreviewApp() {
    val model = Model(WindowsSizeValue(800, 600)).apply {
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

    App(model)
}

@Composable
fun App(model: Model) {

    val viewportSize by model.viewport.collectAsState()

    val imgSize = painterResource(Res.drawable.level_all_background).intrinsicSize

    val scaleHeight = viewportSize.height / imgSize.height
    val scaleWith = viewportSize.width / imgSize.width

    val scale = maxOf(scaleHeight, scaleWith)

    MaterialTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.6f))
                .paint(
                    painterResource(Res.drawable.level_all_background),
                    contentScale = FixedScale( scale)
                )
                .scale(scale),
        ) {
            MessageBox(model.messages)

            people(
                model.people,
                Modifier
                    .align(Alignment.Center)
                    .offset(model.peopleOffset)
                    .onGloballyPositioned {
                        model.people.coordinates = it
                    }
                    .onSizeChanged {
                        model.people.size = it
                    }
            )
            
            manufacture(
                model.manufacture,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(model.manufactureOffset)
                    .onGloballyPositioned {
                        model.manufacture.coordinates = it
                    }
                    .onSizeChanged {
                        model.manufacture.size = it
                    },
            )

            market(
                model.market,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(model.marketOffset)
                    .onGloballyPositioned {
                        model.market.coordinates = it
                    }
                    .onSizeChanged {
                        model.market.size = it
                    },
            )

            if (model.bank.has) {
                bank(
                    model.bank,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(model.bankOffset)
                        .onGloballyPositioned {
                            model.bank.coordinates = it
                        }
                        .onSizeChanged {
                            model.bank.size = it
                        },
                )
                BankMoney(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(model.bankMoneyOffset),
                    model
                )
                MarketTakeMoney(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(model.moneyToMarketOffset),
                    model
                )
                PeopleTakeMoney(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(model.moneyToPeopleOffset),
                    model
                )
                ManufactureTakeMoney(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(model.moneyToManufactureOffset),
                    model
                )
            }

            MovePeopleWork(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(model.movePeopleWorkOffset),
                model,
            )

            MoveProductsFromManufactureToPeople(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(model.moveProductsFromManufactureToPeopleOffset),
                model,
            )

            MoveProductsFromManufactureToMarket(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(model.moveProductsFromManufactureToMarketOffset),
                model,
            )

            MoveProductsFromMarketToPeople(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(model.moveProductsFromMarketToPeopleOffset),
                model,
            )

            level(
                model,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .fillMaxHeight()
            )

            Sound(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd)
                    .offset(model.soundOffset),
                model
            )

            TelegramGroup(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd)
            )

            Hint(model.hint)
        }
    }
}

@Composable
fun Sound(
    modifier: Modifier = Modifier,
    model: Model,
){
    val scope = rememberCoroutineScope()
    Image(
        painterResource(model.soundImage.value),
        null,
        modifier = modifier
            .size(40.dp)
            .padding(4.dp)
            .clickable {
                scope.launch {
                    model.changeSound()
                }
            }
    )
}

@Composable
fun TelegramGroup(
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
fun BankMoney(
    modifier: Modifier = Modifier,
    model: Model
) {
    if (model.snowBankMoney()) {
        Move(
            { model.moneyIcon() },
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
        Move(
            { model.moneyIcon() },
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
        Move(
            { model.moneyIcon() },
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
        Move(
            { model.moneyIcon() },
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
fun MarketTakeMoney(
    modifier: Modifier = Modifier,
    model: Model
) {
    AnimatedMove(
        modifier,
        model.moneyToMarket,
    )
}

@Composable
fun AnimatedMove(
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

        Move(
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
fun ExchangeWithMoney(
    modifier: Modifier = Modifier,
    model: MoveableAnimation,
    hasMoney: Boolean,
){
    AnimatedMove(
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
fun ManufactureTakeMoney(
    modifier: Modifier = Modifier,
    model: Model
) {
    AnimatedMove(
        modifier,
        model.moneyToManufacture,
    )
}

@Composable
fun PeopleTakeMoney(
    modifier: Modifier = Modifier,
    model: Model
) {
    AnimatedMove(
        modifier,
        model.moneyToPeople,
    )
}

@Composable
fun BoxScope.Hint(
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
fun MovePeopleWork(
    modifier: Modifier = Modifier,
    model: Model
) {
    ExchangeWithMoney(
        modifier,
        model.workPeople,
        model.bank.has
    )
}

@Composable
fun MoveProductsFromMarketToPeople(
    modifier: Modifier = Modifier,
    model: Model
) {
    ExchangeWithMoney(
        modifier,
        model.productsFromMarketToPeople,
        model.bank.has
    )
}

@Composable
fun MoveProductsFromManufactureToMarket(
    modifier: Modifier = Modifier,
    model: Model
) {
    ExchangeWithMoney(
        modifier,
        model.productsToMarket,
        model.bank.has
    )
}

@Composable
fun MoveProductsFromManufactureToPeople(
    modifier: Modifier = Modifier,
    model: Model,
) {
    AnimatedMove(
        modifier,
        model.productsFromManufactureToPeople,
    )
}

@Composable
fun Move(
    icon: () -> DrawableResource,
    modifier: Modifier = Modifier,
    onMove: () -> Unit
) {
    Image(
        painterResource(icon()),
        contentDescription = "Забрать товары",
        modifier = modifier
            .size(60.dp)
            .clickable(
                onClick = onMove
            )
    )
}