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

    val tempMoney = remember { mutableStateOf(money) }
    val tempLoanSize = remember{ mutableStateOf(loanSize) }
    val tempLoanInterest = remember{ mutableStateOf(loanInterest) }
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
                    value = tempMoney.value.toString(),
                    onValueChange = {strValue: String ->
                        val value = strValue.toDoubleOrNull() ?: return@OutlinedTextField

                        tempMoney.value = value
                    },
                    label = { Text("Деньги") },

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                OutlinedTextField(
                    value = tempLoanSize.value.toString(),
                    onValueChange = {strValue: String ->
                        val value = strValue.toIntOrNull() ?: return@OutlinedTextField

                        tempLoanSize.value = value
                    },
                    label = { Text("Величина кредита") },

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                if (showLoanInterest) {
                    OutlinedTextField(
                        value = tempLoanInterest.value.toString(),
                        onValueChange = { strValue: String ->
                            val value = strValue.toIntOrNull() ?: return@OutlinedTextField

                            tempLoanInterest.value = value
                        },
                        label = { Text("Ссудный процент") },

                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                {
                    money = tempMoney.value
                    loanSize = tempLoanSize.value
                    loanInterest = tempLoanInterest.value
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