package com.example.test1internalrepresentation.feature.coding.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.ui.unit.dp
import com.example.test1internalrepresentation.core.domain.util.formatDecimal
import com.example.test1internalrepresentation.feature.coding.domain.CodingAnalysis
import com.example.test1internalrepresentation.feature.coding.ui.components.ComparisonCardsSection
import com.example.test1internalrepresentation.feature.coding.ui.components.EntropyOverviewCard
import com.example.test1internalrepresentation.feature.coding.ui.components.MetricsExplanationCard
import com.example.test1internalrepresentation.feature.coding.ui.components.ReportConclusionCard
import com.example.test1internalrepresentation.feature.coding.ui.components.TableHeader
import com.example.test1internalrepresentation.feature.coding.ui.components.TableRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    analysis: CodingAnalysis,
    onBackClick: () -> Unit
) {
    BackHandler(onBack = onBackClick)
    var isExplanationExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Результаты кодирования",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { isExplanationExpanded = !isExplanationExpanded }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Справка по формулам",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
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
                EntropyOverviewCard(analysis = analysis)
            }

            item {
                MetricsExplanationCard(
                    isExpanded = isExplanationExpanded,
                    onToggleExpand = { isExplanationExpanded = !isExplanationExpanded }
                )
            }

            item {
                Text(
                    text = "Сравнение алгоритмов сжатия",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                ComparisonCardsSection(analysis = analysis)
            }

            item {
                Text(
                    text = "Таблица кодовых слов",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                TableHeader()
            }

            val hufMap = analysis.huffman.codes.associateBy { it.symbol }

            itemsIndexed(
                items = analysis.shannonFano.codes,
                key = { _, item -> item.symbol }
            ) { index, item ->
                val hufCode = hufMap[item.symbol]?.code ?: ""
                TableRow(
                    index = index,
                    symbol = item.symbol,
                    prob = item.probability.formatDecimal(3),
                    sfCode = item.code,
                    hufCode = hufCode
                )
            }

            item {
                ReportConclusionCard()
            }
        }
    }
}
