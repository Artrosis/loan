package ru.predictor.loan.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.predictor.loan.model.Bank
import ru.predictor.loan.utils.toCaption

/*
@Composable
@Preview
fun previewBank(){
    val model = Bank({}, {0.0}, { mapOf()}).apply {
        has = true
        money = 100500.0
    }

    bank(model)
}
*/

@Composable
fun bank(
    model: Bank,
    modifier: Modifier = Modifier,
) {
    if (model.has) {
        model.editSettings()
        Surface(
            modifier = modifier
                .clickable {
                    model.click()
                },
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            border = BorderStroke(width = 1.dp, color = Color.Gray),
            color = Color(0xA0FFFFFF),
        ) {
            Column(
                modifier = modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(if (model.isMobile) 80.dp else 150.dp)
                ){
                    model.settings(
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                    Image(
                        painterResource(model.getIcon()),
                        null,
                        modifier = modifier
                            .size(if (model.isMobile) 70.dp else 150.dp),
                    )
                }
                
                if (model.showLoanInterest) {
                    Text("Ссудный процент: ${model.loanInterest}")
                }

                if (model.showMoney) {
                    Text("Деньги: ${model.money.toCaption()}")
                }
            }
        }
    }
}

@Composable
fun Bank.settings(
    modifier: Modifier = Modifier,
) {
    Image(
        imageVector = Icons.Filled.Settings,
        contentDescription = "Настройки",
        modifier = modifier
            .clickable(
                onClick = {
                    editSettings = true
                }
            ),
    )
}