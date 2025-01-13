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
import ru.predictor.loan.model.People

@Composable
fun People.editSettings() {
    if (!editSettings) return

    val tempProducts = remember { mutableStateOf(products) }
    val tempMoney = remember { mutableStateOf(money) }
    val tempPopulation = remember { mutableStateOf(population) }
    val tempPopulationRate = remember{ mutableStateOf(populationRate) }

    AlertDialog(
        onDismissRequest = {editSettings = false},
        title = {
            Text(
                "Настройки населения",
                fontSize = 30.sp,
            )
        },
        text =
        {
            Column {
                OutlinedTextField(
                    value = tempProducts.value.toString(),
                    onValueChange = {strValue: String ->
                        val value = strValue.toIntOrNull() ?: return@OutlinedTextField

                        tempProducts.value = value
                    },
                    label = { Text("Продукты") },

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
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
                    value = tempPopulation.value.toString(),
                    onValueChange = {strValue: String ->
                        val value = strValue.toFloatOrNull() ?: return@OutlinedTextField

                        tempPopulation.value = value
                    },
                    label = { Text("Население") },

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                OutlinedTextField(
                    value = tempPopulationRate.value.toString(),
                    onValueChange = {strValue: String ->
                        val value = strValue.toFloatOrNull() ?: return@OutlinedTextField

                        tempPopulationRate.value = value
                    },
                    label = { Text("Коэффициент роста") },

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        },
        confirmButton = {
            Button(
                {
                    products = tempProducts.value
                    money = tempMoney.value
                    population = tempPopulation.value
                    populationRate = tempPopulationRate.value
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