# DAA Assignment 2 — Data Structures

## Requirements

- Java 25
- Maven
- IntelliJ IDEA or another Java IDE

## Project structure

```text
src/main/java/structures/
    DynamicArray.java
    MyLinkedList.java
    MinHeap.java
    OperationCounter.java

src/main/java/benchmark/
    Benchmark.java

src/test/java/
    DataStructuresTest.java

results/
    results.csv
    plots/

README.md
REPORT.md
pom.xml
```

## 1. Run tests

```bash
mvn test
```

## 2. Build

```bash
mvn -q -DskipTests package
```

## 3. Run benchmark

```bash
java -cp target/classes benchmark.Benchmark
```

The benchmark writes:

```text
results/results.csv
```

It uses `n = 100, 1000, 10000, 100000`, `Random(42)`, five timing runs and discards the first warm-up run.

## 4. Generate plots

Install Python packages:

```bash
pip install pandas matplotlib
```

Then run:

```bash
python generate_plots.py
```

The script reads `results/results.csv` and creates PNG charts in:

```text
results/plots/
```

## 5. Important before submission

The supplied archive contains provisional timing values for `n = 100000` because the preparation environment could not complete the full linked-list benchmark. Run the Java 25/Maven benchmark on the student's machine before submitting and regenerate the plots.

## 6. Git release

Create feature branches:

```bash
git checkout -b feature/array
git checkout -b feature/list
git checkout -b feature/heap
git checkout -b feature/metrics
```

Merge the finished work into `main`, then create:

```bash
git tag v1.0
git push origin main --tags
```
