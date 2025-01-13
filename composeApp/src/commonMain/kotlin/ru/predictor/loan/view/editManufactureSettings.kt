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
import ru.predictor.loan.model.Manufacture

@Composable
fun Manufacture.editSettings() {
    if (!editSettings) return

    val tempSalary = remember{ mutableStateOf(salary) }
    val tempEfficiency = remember{ mutableStateOf(efficiency) }
    val tempProducts = remember { mutableStateOf(products) }
    AlertDialog(
        onDismissRequest = {editSettings = false},
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
                    value = tempSalary.value.toString(),
                    onValueChange = {strValue: String ->
                        val value = strValue.toDoubleOrNull() ?: return@OutlinedTextField

                        tempSalary.value = value
                    },
                    label = { Text("Зарплатный коэффициент") },

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                OutlinedTextField(
                    value = tempEfficiency.value.toString(),
                    onValueChange = {strValue: String ->
                        val value = strValue.toDoubleOrNull() ?: return@OutlinedTextField

                        tempEfficiency.value = value
                    },
                    label = { Text("Производительность") },

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                OutlinedTextField(
                    value = tempProducts.value.toString(),
                    onValueChange = {strValue: String ->
                        val value = strValue.toIntOrNull() ?: return@OutlinedTextField

                        tempProducts.value = value
                    },
                    label = { Text("Продукты") },

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        },
        confirmButton = {
            Button(
                {
                    salary = tempSalary.value
                    efficiency = tempEfficiency.value
                    products = tempProducts.value
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