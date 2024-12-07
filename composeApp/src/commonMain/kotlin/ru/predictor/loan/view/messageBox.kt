package ru.predictor.loan.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.predictor.loan.model.Messages

@Composable
fun messageBox(
    model: Messages
) {
    if (model.messages.isEmpty()) return

    AlertDialog(
        onDismissRequest = {},
        text = 
        {
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
        },
        confirmButton = {
            Button(
                { 
                    model.clear()
                    model.next()
                }
            ) {
                Text(
                    model.buttonText, 
                    fontSize = 22.sp
                )
            }
        }
    )
}