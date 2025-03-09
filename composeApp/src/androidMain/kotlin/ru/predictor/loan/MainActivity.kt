package ru.predictor.loan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.predictor.loan.model.Model

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            app(Model())
        }
    }
}

@Suppress("unused")
@Preview
@Composable
fun AppAndroidPreview() {
    app(Model())
}