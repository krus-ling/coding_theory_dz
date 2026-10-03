package com.example.test1internalrepresentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test1internalrepresentation.ui.theme.Test1InternalRepresentationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Test1InternalRepresentationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Task1Screen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Task1Screen(modifier: Modifier = Modifier) {
    var inputA by remember { mutableStateOf("-74") }
    var inputB by remember { mutableStateOf("12") }
    var result by remember { mutableStateOf<Task1Result?>(null) }
    var isVerificationExpanded by remember { mutableStateOf(false) }
    var scrollBeforeExpand by remember { mutableIntStateOf(0) }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    // Отслеживаем разворачивание блока проверки в реальном времени
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Поля ввода
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputA,
                onValueChange = { inputA = it },
                label = { Text("Число А") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = inputB,
                onValueChange = { inputB = it },
                label = { Text("Число Б") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        // Превью операции
        Text(
            text = "Складываем ${inputA.ifEmpty { "0" }} + ${inputB.ifEmpty { "0" }}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        // Кнопка расчета
        Button(
            onClick = {
                val a = inputA.toIntOrNull() ?: 0
                val b = inputB.toIntOrNull() ?: 0
                result = solveTask1WithSteps(a, b)
                isVerificationExpanded = false

                focusManager.clearFocus()
                coroutineScope.launch {
                    delay(50.milliseconds)
                    scrollState.animateScrollTo(
                        value = scrollState.maxValue,
                        animationSpec = tween(durationMillis = 400)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать")
        }

        // Блок решения
        result?.let { taskResult ->
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            ResultCard(
                binaryResult = taskResult.binaryResult,
                decimalResult = taskResult.decimalResult
            )

            // Основные шаги
            Task1StepsScreen(steps = taskResult.steps)

            // Кнопка-спойлер для раздела проверки
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        if (!isVerificationExpanded) {
                            scrollBeforeExpand = scrollState.value
                            isVerificationExpanded = true
                        } else {
                            isVerificationExpanded = false
                            // Плавный возврат скролла назад при сворачивании
                            coroutineScope.launch {
                                scrollState.animateScrollTo(
                                    value = scrollBeforeExpand,
                                    animationSpec = tween(durationMillis = 300)
                                )
                            }
                        }
                    }
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Проверка:",
                    fontWeight = FontWeight.Bold
                )
                Text(if (isVerificationExpanded) "▲ Свернуть" else "▼ Развернуть")
            }

            // Проверка с синхронизированной анимацией
            AnimatedVisibility(
                visible = isVerificationExpanded,
                enter = expandVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(animationSpec = tween(200))
            ) {
                Task1VerificationStep(steps = taskResult.verification)
            }
        }
    }
}

@Composable
fun Task1StepsScreen(steps: List<String>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.4f))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        steps.forEach { step ->
            Text(
                text = step,
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun Task1VerificationStep(steps: List<String>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.4f))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        steps.forEach { step ->
            Text(
                text = step,
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun ResultCard(binaryResult: String, decimalResult: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Итоговый результат",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "${binaryResult}₂",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "= ${decimalResult}₁₀",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}