# Graph Report - coding_theory  (2026-10-03)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 270 nodes · 540 edges · 20 communities (6 shown, 14 thin omitted)
- Extraction: 100% EXTRACTED · 0% INFERRED · 0% AMBIGUOUS
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `0b10a19d`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- CodingViewModel.kt
- Theme.kt
- RepresentationScreen.kt
- CalculateNumberRepresentationUseCase
- Composable
- InputScreen
- Screen.kt
- ExampleInstrumentedTest.kt
- ExampleUnitTest.kt
- formatDecimal

## God Nodes (most connected - your core abstractions)
1. `CodingViewModel` - 15 edges
2. `SymbolProbability` - 14 edges
3. `ResultsScreen()` - 12 edges
4. `InputScreen()` - 12 edges
5. `CalculateNumberRepresentationUseCase` - 11 edges
6. `Task1Screen()` - 9 edges
7. `CodeWord` - 8 edges
8. `CodingAnalysis` - 8 edges
9. `InputMode` - 8 edges
10. `RootScreen()` - 8 edges

## Surprising Connections (you probably didn't know these)
- `CodingAnalysis` --references--> `CodingMethodResult`  [EXTRACTED]
  app/src/main/java/com/example/test1internalrepresentation/domain/model/CodingAnalysis.kt → app/src/main/java/com/example/test1internalrepresentation/domain/model/CodingMethodResult.kt
- `UiState` --references--> `SymbolProbability`  [EXTRACTED]
  app/src/main/java/com/example/test1internalrepresentation/ui/input/CodingViewModel.kt → app/src/main/java/com/example/test1internalrepresentation/domain/model/SymbolProbability.kt
- `EnsembleDialog()` --references--> `SymbolProbability`  [EXTRACTED]
  app/src/main/java/com/example/test1internalrepresentation/ui/input/InputScreen.kt → app/src/main/java/com/example/test1internalrepresentation/domain/model/SymbolProbability.kt
- `UiState` --references--> `CodingAnalysis`  [EXTRACTED]
  app/src/main/java/com/example/test1internalrepresentation/ui/input/CodingViewModel.kt → app/src/main/java/com/example/test1internalrepresentation/domain/model/CodingAnalysis.kt
- `RootScreen()` --calls--> `InputScreen()`  [EXTRACTED]
  app/src/main/java/com/example/test1internalrepresentation/ui/navigation/RootScreen.kt → app/src/main/java/com/example/test1internalrepresentation/ui/input/InputScreen.kt

## Import Cycles
- None detected.

## Communities (20 total, 14 thin omitted)

### Community 0 - "CodingViewModel.kt"
Cohesion: 0.07
Nodes (13): VariantData, VariantsRepository, CodeWord, CodingMethodResult, SymbolProbability, AnalyzeTextFrequencyUseCase, BuildHuffmanUseCase, Internal (+5 more)

### Community 1 - "Theme.kt"
Cohesion: 0.09
Nodes (6): MainActivity, DashboardTileCard(), HeaderWelcomeBanner(), HomeScreen(), RootScreen(), Test1InternalRepresentationTheme()

### Community 6 - "CalculateNumberRepresentationUseCase"
Cohesion: 0.18
Nodes (9): Task1Result, CalculateNumberRepresentationUseCase, HeroResultCard(), InputAndPresetsSection(), RepresentationScreen(), Task1Screen(), TimelineStepItem(), TimelineStepsList() (+1 more)

### Community 7 - "Composable"
Cohesion: 0.23
Nodes (12): CodingAnalysis, AlgorithmComparisonHelp(), CodeBadge(), ComparisonCardsSection(), EntropyOverviewCard(), MetricHelpItem(), MetricRow(), MetricsExplanationCard() (+4 more)

### Community 8 - "InputScreen"
Cohesion: 0.21
Nodes (13): InputMode, CUSTOM_PROBABILITIES, TEXT_ANALYSIS, VARIANT_7_LOWER, VARIANT_7_UPPER, UiState, CustomProbabilitiesSection(), EnsembleDialog() (+5 more)

### Community 9 - "Screen.kt"
Cohesion: 0.22
Nodes (4): Coding, Home, Representation, Screen

## Knowledge Gaps
- **8 isolated node(s):** `PresetPair`, `Coding`, `Home`, `Representation`, `CUSTOM_PROBABILITIES` (+3 more)
  These have ≤1 connection - possible missing edges. (Counts symbols only; 92 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **14 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `SymbolProbability` connect `CodingViewModel.kt` to `InputScreen`, `InputScreen.kt`?**
  _High betweenness centrality (0.109) - this node is a cross-community bridge._
- **Why does `CodingViewModel` connect `CodingViewModel.kt` to `InputScreen`, `RootScreen.kt`?**
  _High betweenness centrality (0.063) - this node is a cross-community bridge._
- **Why does `CodingAnalysis` connect `Composable` to `CodingViewModel.kt`, `InputScreen`, `ResultsScreen.kt`?**
  _High betweenness centrality (0.059) - this node is a cross-community bridge._
- **What connects `PresetPair`, `Coding`, `Home` to the rest of the system?**
  _8 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `CodingViewModel.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.07373737373737374 - nodes in this community are weakly interconnected._
- **Should `Theme.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.09401709401709402 - nodes in this community are weakly interconnected._
- **Should `InputScreen.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.07407407407407407 - nodes in this community are weakly interconnected._