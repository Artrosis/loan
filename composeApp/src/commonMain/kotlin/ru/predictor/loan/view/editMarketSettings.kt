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
import ru.predictor.loan.model.Market

@Composable
fun Market.editSettings() {
    if (!editSettings) return

    val tempProducts = remember { mutableStateOf(products) }
    val tempMoney = remember { mutableStateOf(money) }


    AlertDialog(
        onDismissRequest = { editSettings = false },
        title = {
            Text(
                "Настройки производства",
                fontSize = 30.sp,
            )
        },
        text =
        {
            Column {
                OutlinedTextField(
                    value = tempProducts.value.toString(),
                    onValueChange = { strValue: String ->
                        val value = strValue.toIntOrNull() ?: return@OutlinedTextField

                        tempProducts.value = value
                    },
                    label = { Text("Продукты") },

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                OutlinedTextField(
                    value = tempMoney.value.toString(),
                    onValueChange = { strValue: String ->
                        val value = strValue.toDoubleOrNull() ?: return@OutlinedTextField

                        tempMoney.value = value
                    },
                    label = { Text("Деньги") },

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        },
        confirmButton = {
            Button(
                {
                    products = tempProducts.value
                    money = tempMoney.value
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