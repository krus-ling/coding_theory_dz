# Теория кодирования — Лабораторный практикум (УрФУ)

Современное Android-приложение на **Jetpack Compose** и **Multi-Module Clean Architecture**, предназначенное для расчета, сжатия и визуализации алгоритмов теории информации и кодирования.

---

## 📌 Архитектура проекта (Многомодульная структура)

Приложение построено по принципам **Clean Architecture** (Separation of Concerns) с явным разделением на 6 изолированных модулей:

```text
coding_theory/
├── settings.gradle.kts                     # Включает 6 независимых модулей
├── build.gradle.kts                        # Корневой скрипт сборки
├── gradle/libs.versions.toml               # Версионный каталог зависимостей
│
├── app/                                    # Главный модуль приложения (Android Application)
│   ├── build.gradle.kts
│   └── src/main/java/com/example/test1internalrepresentation/
│       ├── MainActivity.kt                 # Точка входа в приложение
│       └── ui/
│           ├── home/HomeScreen.kt          # Главный дашборд с витриной модулей
│           └── navigation/
│               ├── Screen.kt               # Описание экранов и маршрутов
│               └── RootScreen.kt           # Стек переключения экранов
│
├── core/
│   ├── domain/                             # :core:domain (Общие модели и алгоритмические утилиты)
│   │   └── src/main/java/com/example/test1internalrepresentation/core/domain/
│   │       ├── model/
│   │       │   ├── SymbolProbability.kt    # Символ и его вероятность
│   │       │   └── CodeWord.kt             # Кодовое слово и его длина
│   │       └── util/
│   │           ├── BitStream.kt            # BitInputStream / BitOutputStream
│   │           ├── FormatUtils.kt          # Форматирование файлов и HEX-дампов
│   │           └── Extensions.kt           # Double.formatDecimal
│   │
│   └── ui/                                 # :core:ui (Единая дизайн-система Material Design 3)
│       └── src/main/java/com/example/test1internalrepresentation/core/ui/
│           └── theme/
│               ├── Color.kt
│               ├── Theme.kt
│               └── Type.kt
│
└── feature/
    ├── representation/                     # :feature:representation (Контрольная работа)
    │   └── src/main/java/com/example/test1internalrepresentation/feature/representation/
    │       ├── domain/
    │       │   ├── Task1Result.kt          # Модель результата вычислений
    │       │   └── CalculateNumberRepresentationUseCase.kt # Алгебра дополнительного кода
    │       └── ui/
    │           ├── RepresentationScreen.kt # Экран перевода и сложения чисел
    │           └── components/
    │               ├── InputAndPresetsSection.kt # Поля ввода и модальный диалог
    │               ├── HeroResultCard.kt         # Акцентная карточка итогового кода
    │               ├── TimelineStepsList.kt      # Хронология пошагового решения
    │               └── VerificationSpoilerCard.kt# Спойлер обратной проверки
    │
    ├── coding/                             # :feature:coding (Лабораторная работа №1)
    │   └── src/main/java/com/example/test1internalrepresentation/feature/coding/
    │       ├── data/
    │       │   └── VariantsRepository.kt   # Варианты 7, Ципф 120, богатый пресет
    │       ├── domain/
    │       │   ├── CodingMethodResult.kt   # Результат конкретного алгоритма
    │       │   ├── CodingAnalysis.kt       # Сравнительный отчет методов
    │       │   ├── BuildShannonFanoUseCase.kt # Построение кода Шеннона-Фано
    │       │   ├── BuildHuffmanUseCase.kt     # Построение кода Хаффмана
    │       │   ├── CalculateMetricsUseCase.kt # Расчет H, H_max, L_ср
    │       │   └── AnalyzeTextFrequencyUseCase.kt # Частотный анализ текста
    │       └── ui/
    │           ├── CodingViewModel.kt      # Управление состоянием источника
    │           ├── InputScreen.kt          # Настройка источника сообщений
    │           ├── ResultsScreen.kt        # Сравнительные результаты и таблица
    │           └── components/
    │               ├── ModeSelectionSection.kt# Модальный выбор режима ансамбля
    │               ├── TextAnalysisSection.kt # Ввод текста и вставка пресета (>100)
    │               ├── CustomProbabilitiesSection.kt # Ручной ввод вероятностей
    │               ├── EnsemblePreviewCard.kt # Превью ансамбля с проверкой ∑ P
    │               ├── EnsembleDialog.kt      # Диалог просмотра полного алфавита
    │               ├── ErrorMessageCard.kt    # Ошибки валидации вероятностей
    │               ├── EntropyOverviewCard.kt # Карточка значений H и H_max
    │               ├── MetricsExplanationCard.kt # Теоретическая справка с формулами
    │               ├── ComparisonCardsSection.kt # Карточки Шеннона-Фано vs Хаффмана
    │               └── CodesTableSection.kt   # Таблица двоичных кодовых слов
    │
    └── archiver/                           # :feature:archiver (Лабораторная работа №2)
        └── src/main/java/com/example/test1internalrepresentation/feature/archiver/
            ├── domain/
            │   ├── CompressionResult.kt     # CompressionReport / DecompressionReport
            │   ├── CompressHuffmanUseCase.kt   # Сжатие файлов методом Хаффмана
            │   └── DecompressHuffmanUseCase.kt # Распаковка архива .huff
            └── ui/
                ├── Lab2ViewModel.kt        # Управление архивацией и экспортом
                ├── Lab2Screen.kt            # Вкладки Архивация и Деархивация
                └── components/
                    ├── PresetsSection.kt       # Карточка выбора тестовых файлов
                    ├── CompressionReportCard.kt# Карточка отчета сжатия (V₁, V₂, K_сж)
                    ├── DecompressionReportCard.kt # Отчет распаковки с родным именем
                    ├── HeaderHexDumpCard.kt    # HEX-дамп служебного заголовка
                    ├── HuffmanCodesTableCard.kt# Таблица байтовых кодов Хаффмана
                    └── RestoredHexDumpCard.kt  # HEX-дамп восстановленных байтов
```

---

## 🛠 Подробное описание компонентов по модулям

### 1. Модуль `:feature:representation` (Контрольная работа)
* **`CalculateNumberRepresentationUseCase.kt`:**
  * Переводит операнды A и B в прямой, обратный и дополнительный коды.
  * Динамически определяет разрядную сетку (2, 4, 8, 16, 32 бит).
  * Выполняет двоичное сложение в дополнительном коде с формированием пошаговой хронологии и обратной проверки через суммы степеней двойки.
* **`InputAndPresetsSection.kt`:**
  * Текстовые поля с иконками и валидацией ввода.
  * Карточка вызова диалогового окна быстрых примеров (`PresetSelectionDialog`).
* **`HeroResultCard.kt`:**
  * Акцентная M3-карточка со знаковой подсветкой (красный знаковый бит `bit[0]`, белые информационные биты) и горизонтальным скроллом для 16/32-битных результатов.
* **`TimelineStepsList.kt`:**
  * Вертикальный список шагов решения с круглыми бейджами нумерации.
* **`VerificationSpoilerCard.kt`:**
  * Раскрывающийся аккордеон с анимацией разворачивания для обратной математической проверки.

### 2. Модуль `:feature:coding` (Лабораторная работа №1)
* **`BuildShannonFanoUseCase.kt`:**
  * Рекурсивный алгоритм деления упорядоченного алфавита на две подгруппы с минимальной разностью сумм вероятностей.
* **`BuildHuffmanUseCase.kt`:**
  * Построение оптимального префиксного дерева Хаффмана через очередь с приоритетом (`PriorityQueue`).
* **`CalculateMetricsUseCase.kt`:**
  * Расчет энтропии $H = -\sum P_i \log_2 P_i$, максимальной энтропии $H_{\max} = \log_2 N$ и средней длины кодового слова $L_{ср} = \sum P_i \cdot l_i$.
* **`AnalyzeTextFrequencyUseCase.kt`:**
  * Частотный анализ букв и символов входного текста с нормализацией сумм.
* **`ModeSelectionSection.kt`:**
  * Карточка выбора источника сообщений: Вариант 7 (Верх/Низ), Закон Ципфа ($N = 120$), Частотный анализ (>100 символов), Ручной ввод.
* **`TextAnalysisSection.kt`:**
  * Многострочное поле ввода текста с живым индикатором числа уникальных символов и кнопкой авто-вставки богатого текста.
* **`EnsemblePreviewCard.kt` & `EnsembleDialog.kt`:**
  * Карточка ансамбля с проверкой условия $\sum P_i = 1.0$ и модальное диалоговое окно для просмотра двухколоночной сетки алфавита без конфликтов скролла.
* **`MetricsExplanationCard.kt`:**
  * Раскрывающийся теоретический аккордеон с подробным разъяснением метрик $H, H_{\max}, L_{ср}, K_{сс}, K_{оэ}$ и различий алгоритмов.

### 3. Модуль `:feature:archiver` (Лабораторная работа №2)
* **`CompressHuffmanUseCase.kt`:**
  * Побитовая упаковка файлов любого типа через `BitOutputStream`.
  * Сериализация компактного служебного заголовка: сигнатура `'HF'`, длина и исходное имя файла в UTF-8, объем $V_1$, мощность алфавита $N$ и таблица частот.
* **`DecompressHuffmanUseCase.kt`:**
  * Валидация архива, восстановление дерева Хаффмана, побитовое декодирование через `BitInputStream` и извлечение родного имени с расширением.
* **`PresetsSection.kt`:**
  * Карточка выбора тестовых последовательностей (текст высокой избыточности, 1 байт, случайные байты).
* **`CompressionReportCard.kt` & `DecompressionReportCard.kt`:**
  * Карточки отчетов со сравнением $V_1$ и $V_2$, коэффициентом сжатия $K_{сж}$ и кнопками записи файлов в память устройства с авто-префиксом `restored_`.
* **`HeaderHexDumpCard.kt` & `RestoredHexDumpCard.kt`:**
  * Карточки HEX-дампов заголовка архива и первых восстановленных байт.

---

## 🎨 UI/UX Особенности
* **Единый стиль Material Design 3 (Material You):** унифицированные шапки `TopAppBar` с подзаголовками дисциплин во всех модулях, мягкие скругления (16–28 dp), тональные контейнеры.
* **Интерактивный просмотр алфавита (`EnsembleDialog`):** диалоговое окно для просмотра всех символов $a_i$, их вероятностей и процентов без конфликта жестов прокрутки.
* **Моноширинный шрифт (`FontFamily.Monospace`):** бейджи двоичных кодов, HEX-дампы заголовков и таблицы кодовых слов.
* **Строковые ресурсы (`strings.xml`):** полное отсутствие захардкоженного текста, вся локализация вынесена в XML-ресурсы.
