package com.example.test1internalrepresentation.ui.representation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test1internalrepresentation.domain.model.Task1Result
import com.example.test1internalrepresentation.domain.usecase.CalculateNumberRepresentationUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class PresetPair(val a: Int, val b: Int)

private val PRESETS = listOf(
    PresetPair(-74, 12),
    PresetPair(52, -30),
    PresetPair(-15, -45),
    PresetPair(127, -128)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepresentationScreen(
    onBackClick: () -> Unit,
    useCase: CalculateNumberRepresentationUseCase = remember { CalculateNumberRepresentationUseCase() }
) {
    BackHandler(onBack = onBackClick)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Представление чисел",
                        fontWeight = FontWeight.Bold
                    )
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
        Task1Screen(
            useCase = useCase,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun Task1Screen(
    useCase: CalculateNumberRepresentationUseCase,
    modifier: Modifier = Modifier
) {
    var inputA by remember { mutableStateOf("-74") }
    var inputB by remember { mutableStateOf("12") }
    var result by remember { mutableStateOf<Task1Result?>(null) }
    var isVerificationExpanded by remember { mutableStateOf(false) }
    var scrollBeforeExpand by remember { mutableIntStateOf(0) }
    var resultTopY by remember { mutableFloatStateOf(0f) }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(result) {
        if (result != null) {
            delay(100.milliseconds)
            if (resultTopY > 0f) {
                scrollState.animateScrollTo(
                    value = resultTopY.toInt(),
                    animationSpec = tween(durationMillis = 500)
                )
            }
        }
    }

    LaunchedEffect(isVerificationExpanded) {
        if (isVerificationExpanded) {
            snapshotFlow { scrollState.maxValue }
                .collect { maxVal ->
                    scrollState.scrollTo(maxVal)
                }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InputAndPresetsSection(
            inputA = inputA,
            inputB = inputB,
            onInputAChange = { inputA = it },
            onInputBChange = { inputB = it },
            onSelectPreset = { preset ->
                inputA = preset.a.toString()
                inputB = preset.b.toString()
            }
        )

        Button(
            onClick = {
                val a = inputA.toIntOrNull() ?: 0
                val b = inputB.toIntOrNull() ?: 0
                result = useCase.execute(a, b)
                isVerificationExpanded = false
                focusManager.clearFocus()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Рассчитать представление",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        result?.let { taskResult ->
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            HeroResultCard(
                binaryResult = taskResult.binaryResult,
                decimalResult = taskResult.decimalResult,
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    resultTopY = coordinates.positionInParent().y
                }
            )

            Text(
                text = "Пошаговое решение",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            TimelineStepsList(steps = taskResult.steps)

            VerificationSpoilerCard(
                isExpanded = isVerificationExpanded,
                verificationSteps = taskResult.verification,
                onToggleExpand = {
                    if (!isVerificationExpanded) {
                        scrollBeforeExpand = scrollState.value
                        isVerificationExpanded = true
                    } else {
                        isVerificationExpanded = false
                        coroutineScope.launch {
                            scrollState.animateScrollTo(
                                value = scrollBeforeExpand,
                                animationSpec = tween(durationMillis = 300)
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun InputAndPresetsSection(
    inputA: String,
    inputB: String,
    onInputAChange: (String) -> Unit,
    onInputBChange: (String) -> Unit,
    onSelectPreset: (PresetPair) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Входные параметры",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = inputA,
                    onValueChange = onInputAChange,
                    label = { Text("Операнд А") },
                    leadingIcon = { Icon(Icons.Default.Numbers, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = inputB,
                    onValueChange = onInputBChange,
                    label = { Text("Операнд Б") },
                    leadingIcon = { Icon(Icons.Default.Numbers, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "Быстрые примеры:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(PRESETS) { preset ->
                    FilterChip(
                        selected = inputA == preset.a.toString() && inputB == preset.b.toString(),
                        onClick = { onSelectPreset(preset) },
                        label = { Text("${preset.a} и ${preset.b}") },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroResultCard(
    binaryResult: String,
    decimalResult: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Итоговое значение (Доп. код)",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp
                ) {
                    Text(
                        text = "= $decimalResult₁₀",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                binaryResult.forEachIndexed { index, bit ->
                    val isSignBit = index == 0
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isSignBit) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface,
                        contentColor = if (isSignBit) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurface
                    ) {
                        Text(
                            text = bit.toString(),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                        )
                    }
                }
                Text(
                    text = "₂",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun TimelineStepsList(steps: List<String>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        steps.forEachIndexed { index, stepText ->
            TimelineStepItem(index = index + 1, stepText = stepText)
        }
    }
}

@Composable
private fun TimelineStepItem(index: Int, stepText: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$index",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stepText,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun VerificationSpoilerCard(
    isExpanded: Boolean,
    verificationSteps: List<String>,
    onToggleExpand: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(300)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggleExpand)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Детальная проверка решения",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
                    verificationSteps.forEach { step ->
                        Text(
                            text = step,
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
