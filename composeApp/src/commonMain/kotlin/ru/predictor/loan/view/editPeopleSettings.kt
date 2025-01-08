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