package ru.predictor.loan

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.datetime.format
import kotlinx.datetime.format.*
import kotlinx.datetime.format.DateTimeComponents.Companion.Format
import loaninterest.composeapp.generated.resources.*
import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.bank
import loaninterest.composeapp.generated.resources.grass
import loaninterest.composeapp.generated.resources.people
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd),
                text =  "Дата: ${model.time.format(DATE_FORMAT)}"
            )
            
            people(
                model.people,
                Modifier.align(Alignment.BottomEnd)
            )
            bank(
                model.bank,
                Modifier.align(Alignment.Center)
            )
            manufacture(
                model.manufacture,
                Modifier.align(Alignment.BottomStart)
            )
            market(
                model.market,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun market(
    model: Market,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Image(
            painterResource(Res.drawable.compose_multiplatform),
            null,
            modifier = modifier
                .size(150.dp),
        )
        Text("Деньги: ${model.money}")
        Text("Продукты: ${model.products}")
        Text("Цены: ${model.price}")
    }
}

@Composable
fun manufacture(
    model: Manufacture,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Image(
            painterResource(Res.drawable.compose_multiplatform),
            null,
            modifier = modifier
                .size(150.dp),
        )
        Text("Деньги: ${model.money}")
        Text("Продукты: ${model.products}")
        Text("Цены: ${model.price}")
        Text("Зарплаты: ${model.salary}")
    }
}

@Composable
fun bank(
    model: Bank,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Image(
            painterResource(Res.drawable.bank),
            null,
            modifier = modifier
                .size(150.dp),
        )
        Text("Ссудный процент: ${model.loanInterest}")
        Text("Деньги: ${model.money}")
    }
}

@Composable
fun people(
    model: People, 
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Image(
            painterResource(Res.drawable.people),
            null,
            modifier = modifier
                .size(150.dp),
        )
        Text("Деньги: ${model.money}")
    }    
}
