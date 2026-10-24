# Теория кодирования — Лабораторный практикум (УрФУ)

Современное Android-приложение на **Jetpack Compose** и **Multi-Module Clean Architecture**, предназначенное для расчета, сжатия и визуализации алгоритмов теории информации и кодирования.

---

## 📌 Архитектура проекта (Многомодульная структура)

Приложение построено по принципам **Clean Architecture** (Separation of Concerns) с явным разделением на изолированные модули:

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
    │       │   ├── Task1Result.kt
    │       │   └── CalculateNumberRepresentationUseCase.kt
    │       └── ui/
    │           └── RepresentationScreen.kt # Экран перевода и сложения чисел
    │
    ├── coding/                             # :feature:coding (Лабораторная работа №1)
    │   └── src/main/java/com/example/test1internalrepresentation/feature/coding/
    │       ├── data/
    │       │   └── VariantsRepository.kt   # Варианты 7, Ципф 120, богатый пресет
    │       ├── domain/
    │       │   ├── CodingMethodResult.kt
    │       │   ├── CodingAnalysis.kt
    │       │   ├── BuildShannonFanoUseCase.kt
    │       │   ├── BuildHuffmanUseCase.kt
    │       │   ├── CalculateMetricsUseCase.kt
    │       │   └── AnalyzeTextFrequencyUseCase.kt
    │       └── ui/
    │           ├── CodingViewModel.kt
    │           ├── InputScreen.kt          # Настройка источника и просмотр алфавита
    │           └── ResultsScreen.kt        # Результаты сжатия и теоретическая справка
    │
    └── archiver/                           # :feature:archiver (Лабораторная работа №2)
        └── src/main/java/com/example/test1internalrepresentation/feature/archiver/
            ├── domain/
            │   ├── CompressionResult.kt     # CompressionReport / DecompressionReport
            │   ├── CompressHuffmanUseCase.kt
            │   └── DecompressHuffmanUseCase.kt
            └── ui/
                ├── Lab2ViewModel.kt
                └── Lab2Screen.kt            # Вкладки Архивация и Деархивация
```

---

## 🛠 Ключевые возможности и модули

### 1. Контрольная работа: Внутреннее представление чисел (`:feature:representation`)
* Перевод операндов в прямой, обратный и дополнительный коды.
* Определение адаптивной разрядной сетки (2, 4, 8, 16, 32 бит).
* Двоичное сложение операндов с пошаговой хронологией решений.
* Горизонтальный скролл разрядной сетки для 16/32-битных результатов и плавная фокусировка на итоговой карточке.

### 2. Лабораторная работа №1: Эффективное кодирование (`:feature:coding`)
* **Источники сообщений:** готовые варианты из таблицы 1.1, распределение Ципфа для алфавита из 120 символов ($N = 120$), частотный анализ текста (>100 уникальных символов) и ручной ввод.
* **Алгоритмы кодирования:**
  * Метод Шеннона — Фано.
  * Оптимальный метод Хаффмана.
* **Рассчитываемые метрики:**
  * Энтропия источника $H = -\sum P_i \log_2 P_i$
  * Максимальная энтропия $H_{\max} = \log_2 N$
  * Средняя длина кодового слова $L_{ср} = \sum P_i \cdot l_i$
  * Коэффициент статистического сжатия $K_{сс} = H_{\max} / L_{ср}$
  * Коэффициент относительной эффективности $K_{оэ} = H / L_{ср}$
* **Теоретическая справка (`MetricsExplanationCard`):** раскрывающийся аккордеон и кнопка инфо в шапке с формулами и объяснением физического смысла параметров.

### 3. Лабораторная работа №2: Архиватор Хаффмана (`:feature:archiver`)
* **Вкладка 1: Архивация (Сжатие):**
  * Выбор любого файла на устройстве через `ActivityResultContracts.OpenDocument()`.
  * Побитовая упаковка через `BitOutputStream`, оптимизированный служебный заголовок с сигнатурой `'HF'`, длиной файла и таблицей частот.
  * Экспорт готового архива (.huff) в память устройства.
* **Вкладка 2: Деархивация (Распаковка):**
  * Чтение `.huff` файла, валидация сигнатуры `'HF'`, восстановление кодового дерева, побитовое декодирование через `BitInputStream`.
  * Автоматическое восстановление исходного имени файла с оригинальным расширением (`restored_*`) при сохранении.

---

## 🎨 UI/UX Особенности
* **Единый стиль Material Design 3 (Material You):** унифицированные шапки `TopAppBar` с подзаголовками дисциплин во всех модулях, мягкие скругления (16–28 dp), тональные контейнеры.
* **Интерактивный просмотр алфавита (`EnsembleDialog`):** диалоговое окно для просмотра всех символов $a_i$, их вероятностей и процентов без конфликта жестов прокрутки.
* **Моноширинный шрифт (`FontFamily.Monospace`):** бейджи двоичных кодов, HEX-дампы заголовков и таблицы кодовых слов.
