package com.example.test1internalrepresentation.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.test1internalrepresentation.ui.home.HomeScreen
import com.example.test1internalrepresentation.ui.input.CodingViewModel
import com.example.test1internalrepresentation.ui.input.InputScreen
import com.example.test1internalrepresentation.ui.representation.RepresentationScreen
import com.example.test1internalrepresentation.ui.result.ResultsScreen

@Composable
fun RootScreen() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    val codingViewModel: CodingViewModel = viewModel()
    val codingState by codingViewModel.uiState.collectAsState()
    var isShowingResults by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        when (currentScreen) {
            is Screen.Home -> {
                HomeScreen(
                    onNavigateToScreen = { targetScreen ->
                        isShowingResults = false
                        currentScreen = targetScreen
                    }
                )
            }

            is Screen.Representation -> {
                RepresentationScreen(
                    onBackClick = { currentScreen = Screen.Home }
                )
            }

            is Screen.Coding -> {
                val currentAnalysis = codingState.analysisResult
                if (isShowingResults && currentAnalysis != null) {
                    ResultsScreen(
                        analysis = currentAnalysis,
                        onBackClick = { isShowingResults = false }
                    )
                } else {
                    InputScreen(
                        state = codingState,
                        onSelectMode = { codingViewModel.selectMode(it) },
                        onTextChanged = { codingViewModel.onTextChanged(it) },
                        onCustomProbChanged = { codingViewModel.onCustomProbabilitiesChanged(it) },
                        onCalculateClick = {
                            codingViewModel.calculate()
                            if (codingState.errorMessage == null) {
                                isShowingResults = true
                            }
                        },
                        onBackClick = { currentScreen = Screen.Home }
                    )
                }
            }
        }
    }
}
