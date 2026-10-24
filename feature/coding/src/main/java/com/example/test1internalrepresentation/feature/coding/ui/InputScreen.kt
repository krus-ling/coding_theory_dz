package com.example.test1internalrepresentation.feature.coding.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.test1internalrepresentation.feature.coding.ui.components.CustomProbabilitiesSection
import com.example.test1internalrepresentation.feature.coding.ui.components.EnsembleDialog
import com.example.test1internalrepresentation.feature.coding.ui.components.EnsemblePreviewCard
import com.example.test1internalrepresentation.feature.coding.ui.components.ErrorMessageCard
import com.example.test1internalrepresentation.feature.coding.ui.components.ModeSelectionSection
import com.example.test1internalrepresentation.feature.coding.ui.components.TextAnalysisSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    state: UiState,
    onSelectMode: (InputMode) -> Unit,
    onTextChanged: (String) -> Unit,
    onInsertRichTextPreset: () -> Unit,
    onCustomProbChanged: (String) -> Unit,
    onCalculateClick: () -> Unit,
    onBackClick: () -> Unit
) {
    BackHandler(onBack = onBackClick)
    var showEnsembleDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Эффективное кодирование",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Лабораторная работа №1 • Алгоритмы сжатия",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ModeSelectionSection(
                    currentMode = state.currentMode,
                    onSelectMode = onSelectMode
                )
            }

            if (state.currentMode == InputMode.TEXT_ANALYSIS) {
                item {
                    TextAnalysisSection(
                        text = state.inputText,
                        onTextChanged = onTextChanged,
                        onInsertRichTextPreset = onInsertRichTextPreset
                    )
                }
            }

            if (state.currentMode == InputMode.CUSTOM_PROBABILITIES) {
                item {
                    CustomProbabilitiesSection(
                        text = state.customProbabilitiesText,
                        onCustomProbChanged = onCustomProbChanged
                    )
                }
            }

            item {
                EnsemblePreviewCard(
                    state = state,
                    onClick = { showEnsembleDialog = true }
                )
            }

            state.errorMessage?.let { error ->
                item {
                    ErrorMessageCard(errorText = error)
                }
            }

            item {
                Button(
                    onClick = onCalculateClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Построить коды и рассчитать метрики",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

    if (showEnsembleDialog) {
        EnsembleDialog(
            symbols = state.symbols,
            onDismiss = { showEnsembleDialog = false }
        )
    }
}
