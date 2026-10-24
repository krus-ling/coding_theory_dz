package com.example.test1internalrepresentation.feature.representation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.test1internalrepresentation.core.ui.R as CoreUiR
import com.example.test1internalrepresentation.feature.representation.R as RepR
import com.example.test1internalrepresentation.feature.representation.domain.CalculateNumberRepresentationUseCase
import com.example.test1internalrepresentation.feature.representation.domain.Task1Result
import com.example.test1internalrepresentation.feature.representation.ui.components.HeroResultCard
import com.example.test1internalrepresentation.feature.representation.ui.components.InputAndPresetsSection
import com.example.test1internalrepresentation.feature.representation.ui.components.TimelineStepsList
import com.example.test1internalrepresentation.feature.representation.ui.components.VerificationSpoilerCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * Главный Composable-экран решения Контрольной работы по представлению чисел и двоичной алгебре.
 *
 * @param onBackClick Обработчик нажатия на кнопку "Назад".
 * @param useCase Экземпляр [CalculateNumberRepresentationUseCase] для выполнения расчетов.
 */
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
                    Column {
                        Text(
                            text = stringResource(RepR.string.rep_title),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(RepR.string.rep_subtitle),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(CoreUiR.string.action_back)
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
        Task1Screen(
            useCase = useCase,
            modifier = Modifier.padding(padding)
        )
    }
}

/**
 * Содержимое экрана ввода и отображения решения задачи представления чисел.
 */
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
                text = stringResource(RepR.string.rep_calculate),
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
                text = stringResource(RepR.string.rep_solution_steps),
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
