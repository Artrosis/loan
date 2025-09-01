package ru.predictor.loan.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.predictor.loan.model.Messages

@Composable
fun MessageBox(
    model: Messages
) {
    if (model.lines.isEmpty()) return

    AlertDialog(
        onDismissRequest = {
            if (model.closeDismiss) {
                model.clear()
            }
        },
        text =
            {
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    itemsIndexed(model.lines) { _, item ->
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = item,
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