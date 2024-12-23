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
fun editManufactureSettings(
    model: Manufacture
) {
    if (!model.editSettings) return

    val salary = remember{ mutableStateOf(model.salary) }
    AlertDialog(
        onDismissRequest = {model.editSettings = false},
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
                    value = salary.value.toString(),
                    onValueChange = {strValue: String ->
                        val value = strValue.toDoubleOrNull() ?: return@OutlinedTextField

                        salary.value = value
                    },
                    label = { Text("Зарплатный коэффициент") },

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        },
        confirmButton = {
            Button(
                {
                    model.salary = salary.value
                    model.editSettings = false
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