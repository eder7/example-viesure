package io.viesure.test.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.viesure.test.ui.navigation.NavigationComponent
import io.viesure.test.ui.theme.ViesureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ViesureTheme {
                NavigationComponent()
            }
        }
    }
}
