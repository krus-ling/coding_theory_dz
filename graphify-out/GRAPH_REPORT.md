# Graph Report - coding_theory  (2026-10-03)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 104 nodes · 133 edges · 12 communities (2 shown, 10 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 1 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `de72ffac`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Theme.kt
- task1.kt
- ExampleInstrumentedTest.kt
- Task1Screen
- ExampleUnitTest.kt

## God Nodes (most connected - your core abstractions)
1. `solveTask1WithSteps()` - 9 edges
2. `Task1Screen()` - 8 edges
3. `toTwosComplement()` - 6 edges
4. `Test1InternalRepresentationTheme()` - 4 edges
5. `addOneInBinary()` - 4 edges
6. `generateVerificationStep()` - 4 edges
7. `reverseStroke()` - 4 edges
8. `Task1StepsScreen()` - 4 edges
9. `Task1VerificationStep()` - 4 edges
10. `MainActivity` - 3 edges

## Surprising Connections (you probably didn't know these)
- `Task1Screen()` --calls--> `solveTask1WithSteps()`  [INFERRED]
  app/src/main/java/com/example/test1internalrepresentation/MainActivity.kt → app/src/main/java/com/example/test1internalrepresentation/task1.kt
- `solveTask1WithSteps()` --references--> `Task1Result`  [EXTRACTED]
  app/src/main/java/com/example/test1internalrepresentation/task1.kt → app/src/main/java/com/example/test1internalrepresentation/Task1Result.kt

## Import Cycles
- None detected.

## Communities (12 total, 10 thin omitted)

### Community 2 - "task1.kt"
Cohesion: 0.36
Nodes (9): addOneInBinary(), conversionToBinary(), fillInTheMissingDigits(), generateVerificationStep(), reverseStroke(), solveTask1WithSteps(), sumInBinary(), toTwosComplement() (+1 more)

### Community 4 - "Task1Screen"
Cohesion: 0.67
Nodes (4): ResultCard(), Task1Screen(), Task1StepsScreen(), Task1VerificationStep()

## Knowledge Gaps
- **10 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Task1Screen()` connect `Task1Screen` to `MainActivity.kt`, `Theme.kt`, `task1.kt`?**
  _High betweenness centrality (0.168) - this node is a cross-community bridge._
- **Why does `solveTask1WithSteps()` connect `task1.kt` to `Task1Screen`?**
  _High betweenness centrality (0.157) - this node is a cross-community bridge._
- **Why does `Test1InternalRepresentationTheme()` connect `Theme.kt` to `MainActivity.kt`?**
  _High betweenness centrality (0.084) - this node is a cross-community bridge._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.044444444444444446 - nodes in this community are weakly interconnected._
- **Should `Theme.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.13333333333333333 - nodes in this community are weakly interconnected._