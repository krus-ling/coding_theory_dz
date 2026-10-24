package com.example.test1internalrepresentation.feature.archiver.ui

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.test1internalrepresentation.feature.archiver.ui.components.CompressionReportCard
import com.example.test1internalrepresentation.feature.archiver.ui.components.DecompressionReportCard
import com.example.test1internalrepresentation.feature.archiver.ui.components.HeaderHexDumpCard
import com.example.test1internalrepresentation.feature.archiver.ui.components.HuffmanCodesTableCard
import com.example.test1internalrepresentation.feature.archiver.ui.components.PresetsSection
import com.example.test1internalrepresentation.feature.archiver.ui.components.RestoredHexDumpCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Lab2Screen(
    onBackClick: () -> Unit,
    viewModel: Lab2ViewModel = viewModel()
) {
    BackHandler(onBack = onBackClick)
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val compressFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { viewModel.onCompressFileSelected(context, it) }
    }

    val decompressFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { viewModel.onDecompressFileSelected(context, it) }
    }

    val exportCompressedLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri ->
        uri?.let { viewModel.exportCompressedFile(context, it) }
    }

    val exportDecompressedLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*")
    ) { uri ->
        uri?.let { viewModel.exportDecompressedFile(context, it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Архиватор Хаффмана", fontWeight = FontWeight.Bold)
                        Text(
                            "Лабораторная работа №2 • Сжатие без потерь",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(
                selectedTabIndex = state.selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                Tab(
                    selected = state.selectedTabIndex == 0,
                    onClick = { viewModel.selectTab(0) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Archive, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Архивация (Сжатие)", fontWeight = FontWeight.Bold)
                        }
                    }
                )

                Tab(
                    selected = state.selectedTabIndex == 1,
                    onClick = { viewModel.selectTab(1) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Unarchive, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Деархивация (Распаковка)", fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                state.errorMessage?.let { error ->
                    item(key = "error_msg") {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Error, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(error, color = MaterialTheme.colorScheme.onErrorContainer, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                state.successMessage?.let { success ->
                    item(key = "success_msg") {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(success, color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                if (state.isProcessing) {
                    item(key = "progress_spinner") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                if (state.selectedTabIndex == 0) {
                    item(key = "compress_file_picker") {
                        ElevatedCard(
                            onClick = { compressFilePicker.launch(arrayOf("*/*")) },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.UploadFile,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Выбрать исходный файл для сжатия",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Выбор произвольного документа из памяти устройства",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    item(key = "presets_row") {
                        PresetsSection(
                            activePreset = state.activePresetType,
                            onSelectPreset = { viewModel.applyPreset(it) }
                        )
                    }

                    state.compressionReport?.let { report ->
                        item(key = "compression_report_card") {
                            CompressionReportCard(
                                report = report,
                                onExportClick = {
                                    val exportName = report.originalFileName.substringBeforeLast('.') + ".huff"
                                    exportCompressedLauncher.launch(exportName)
                                }
                            )
                        }

                        item(key = "header_hex_dump") {
                            HeaderHexDumpCard(
                                sampleHex = report.sampleHex,
                                headerSize = report.headerSizeBytes
                            )
                        }

                        item(key = "huffman_codes_table") {
                            HuffmanCodesTableCard(codesMap = report.codesMap)
                        }
                    }
                } else {
                    item(key = "decompress_file_picker") {
                        ElevatedCard(
                            onClick = { decompressFilePicker.launch(arrayOf("*/*")) },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.secondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.FolderOpen,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Выбрать файл архива (.huff)",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Чтение сигнатуры HF, таблицы частот и декодирование",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    state.decompressionReport?.let { report ->
                        item(key = "decompression_report_card") {
                            val defaultSaveName = if (report.restoredFileName.startsWith("restored_")) {
                                report.restoredFileName
                            } else {
                                "restored_" + report.restoredFileName
                            }
                            DecompressionReportCard(
                                report = report,
                                defaultSaveName = defaultSaveName,
                                onSaveRestoredClick = {
                                    exportDecompressedLauncher.launch(defaultSaveName)
                                }
                            )
                        }

                        item(key = "restored_hex_dump") {
                            RestoredHexDumpCard(sampleHex = report.sampleHex)
                        }
                    }
                }
            }
        }
    }
}
