package ru.predictor.loan.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import ru.predictor.loan.model.Bank

@Composable
fun Bank.editSettings() {
    if (!editSettings) return

    val tempLoanSize = remember{ mutableStateOf(loanSize) }
    AlertDialog(
        onDismissRequest = {editSettings = false},
        title = {
            Text(
                "Настройки банка",
                fontSize = 30.sp,
            )
        },
        text =
        {
            Column {
                OutlinedTextField(
                    value = tempLoanSize.value.toString(),
                    onValueChange = {strValue: String ->
                        val value = strValue.toIntOrNull() ?: return@OutlinedTextField

                        tempLoanSize.value = value
                    },
                    label = { Text("Величина кредита") },

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        },
        confirmButton = {
            Button(
                {
                    loanSize = tempLoanSize.value
                    editSettings = false
                }
            ) {
                Text(
                    "Сохранить",
                    fontSize = 22.sp
                )
            }
        }
    )
}