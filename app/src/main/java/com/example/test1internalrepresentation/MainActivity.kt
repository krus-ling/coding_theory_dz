package com.example.test1internalrepresentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.test1internalrepresentation.core.ui.theme.Test1InternalRepresentationTheme
import com.example.test1internalrepresentation.ui.navigation.RootScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Test1InternalRepresentationTheme {
                RootScreen()
            }
        }
    }
}
