package com.example.test1internalrepresentation.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home", "Главная", Icons.Default.Home)
    data object Representation : Screen("representation", "Внутреннее представление чисел", Icons.Default.Calculate)
    data object Coding : Screen("coding", "Эффективное кодирование", Icons.Default.Compress)
    data object Lab2Compressor : Screen("lab2_compressor", "Архиватор (Хаффман)", Icons.Default.Archive)
}