package ru.predictor.loan.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import loaninterest.composeapp.generated.resources.Res
import loaninterest.composeapp.generated.resources.bank
import org.jetbrains.compose.resources.painterResource
import ru.predictor.loan.model.Bank

@Composable
@Preview
fun previewBank(){
    val model = Bank({}, {0}, { mapOf()}).apply { 
        has = true
        money = 100500
    }

    bank(model)
}

@Composable
fun bank(
    model: Bank,
    modifier: Modifier = Modifier,
) {
    if (model.has) {
        Surface(
            modifier = modifier
                .padding(16.dp)
                .clickable {
                    model.click()
                },
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            border = BorderStroke(width = 1.dp, color = Color.Gray),
            color = Color(0xA0FFFFFF),
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
                if (model.showLoanInterest) {
                    Text("Ссудный процент: ${model.loanInterest}")
                }
                Text("Деньги: ${model.money}")
            }
        }
    }
}