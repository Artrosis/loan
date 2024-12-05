package ru.predictor.loan.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.predictor.loan.model.Messages

@Composable
fun BoxScope.messageBox(
    model: Messages
) {
    if (model.messages.isEmpty()) return

    Surface(
        modifier = Modifier.align(model.messagesAlignment),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.Gray),
        color = Color(0xC0FFFFFF),
    ) {
        Column {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                model.messages.forEach {
                    Text(
                        modifier = Modifier.padding(2.dp),
                        text = it,
                    )
                }
            }            

            Button(
                modifier = Modifier.align(Alignment.End).padding(4.dp),
                onClick = {model.next()}
            ){
                Text(
                    text = model.buttonText,
                )
            }
        }
    }
}