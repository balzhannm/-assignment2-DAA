# DAA Assignment 2 — Data Structures

**Student:** Balzhan  
**Course:** Design and Analysis of Algorithms  
**Assignment:** 2 — Data Structures  
**Deadline:** 04.10.2026 23:59

## 1. Overview

This project implements three data structures from scratch:

- `DynamicArray`
- `MyLinkedList`
- `MinHeap`

The implementation stores primitive `int` values and does not use `java.util.ArrayList`, `java.util.LinkedList` or `java.util.PriorityQueue`. The benchmark follows the four workloads specified in the assignment. The assignment requires exact operation counters, loop-invariant proofs, complexity analysis, benchmark results, plots and a discussion of cache locality and pointer chasing. 

## 2. Data Structures

### DynamicArray

`DynamicArray` stores values in a primitive `int[]`. When the internal array becomes full, its capacity is doubled. `add(index, x)` shifts elements to the right, while `remove(index)` shifts elements to the left. `get(index)` performs direct indexed access. `contains(x)` scans the array from left to right.

### MyLinkedList

`MyLinkedList` is a doubly linked list. Every node stores an `int`, a `next` reference and a `prev` reference. Insertions and removals update links instead of shifting an array. Indexed access requires traversal through nodes.

### MinHeap

`MinHeap` is an array-based binary min-heap. `insert(x)` places the value at the end and uses bubble-up. `peekMin()` returns the root in constant time. `extractMin()` removes the root, moves the last element to the root and restores the heap using bubble-down.

## 3. Complexity Analysis

| Structure | Operation | Best | Average | Worst | Auxiliary Space |
|---|---|---:|---:|---:|---:|
| DynamicArray | add(x) | Θ(1) | Θ(1) amortized | O(n) | O(n) |
| DynamicArray | add(i,x) | Θ(1) | Θ(n) | O(n) | O(n) |
| DynamicArray | remove(i) | Θ(1) | Θ(n) | O(n) | O(n) |
| DynamicArray | get(i) | Θ(1) | Θ(1) | Θ(1) | O(n) |
| DynamicArray | contains(x) | Θ(1) | Θ(n) | O(n) | O(n) |
| MyLinkedList | add(x) | Θ(1) | Θ(n) | O(n) | O(n) |
| MyLinkedList | add(i,x) | Θ(1) | Θ(n) | O(n) | O(n) |
| MyLinkedList | remove(i) | Θ(1) | Θ(n) | O(n) | O(n) |
| MyLinkedList | get(i) | Θ(1) | Θ(n) | O(n) | O(n) |
| MyLinkedList | contains(x) | Θ(1) | Θ(n) | O(n) | O(n) |
| MinHeap | insert(x) | Θ(1) | Θ(log n) | O(log n) | O(n) |
| MinHeap | peekMin() | Θ(1) | Θ(1) | Θ(1) | O(n) |
| MinHeap | extractMin() | Θ(1) | Θ(log n) | O(log n) | O(n) |

**Justification:** Array indexing is constant time. Array insertion and removal can require shifting a linear number of elements. Linked-list indexed operations require traversal. Heap insertion and extraction move along at most the height of the heap, which is logarithmic.

## 4. Loop Invariant Proofs

### 4.1 `DynamicArray.contains(x)`

**Invariant:** Before every iteration at index `i`, every element at positions `0 ... i-1` has already been checked and none is equal to `x`.

**Initialization:** Before the first iteration, `i = 0`. The checked prefix is empty, so the invariant is true.

**Maintenance:** If `data[i]` is not equal to `x`, the method increments `i`. The newly checked element is added to the prefix and is known not to be `x`. Therefore the invariant remains true.

**Termination:** The loop terminates either because `x` is found or because `i == size`. If `i == size`, all elements have been checked and none equals `x`.

**Conclusion:** The method returns `true` exactly when `x` occurs in the array.

### 4.2 `MinHeap` bubble-down

**Invariant:** Before each iteration, all heap subtrees below the current position are valid min-heaps; any possible heap-order violation is located at the current position and below it.

**Initialization:** Immediately after `extractMin`, only the root can violate the heap property because the last element has been moved to the root. Every other subtree was already a valid heap.

**Maintenance:** The algorithm compares the current node with its left and right children and selects the smallest child. If the current node is larger, it swaps with that child. The possible violation moves down, while the already processed part remains valid.

**Termination:** The loop stops when the current node is not larger than either child or when the node has no children. Therefore no heap-order violation remains.

**Conclusion:** After bubble-down, the complete array satisfies the min-heap property.

## 5. Benchmark Design

The benchmark uses:

- `n = 100, 1,000, 10,000, 100,000`
- `new Random(42)` for reproducible input
- 5 timing repetitions
- first run discarded as JVM warm-up
- median of the remaining runs

The CSV columns are:

`workload,variant,structure,n,time_ms,steps,moves,comparisons`

### W1 — Random Access

Each structure is filled with `n` values and then 10,000 random `get(index)` calls are performed.

Expected result: `DynamicArray` should be much faster because indexed array access is Θ(1), while linked-list indexed access is Θ(n).

### W2 — Search

Each structure receives 1,000 `contains(x)` queries. Half of the queries use values present in the structure and half use values absent from it.

Expected result: both structures have linear search complexity, but the array normally benefits from contiguous memory and better cache locality.

### W3 — Insert & Remove

Two variants are measured:

- `head`: 1,000 insertions and 1,000 removals at index `0`
- `middle`: 1,000 insertions and 1,000 removals at index `n / 2`

Expected result: linked lists are especially attractive at the head because no element shifting is required. Dynamic arrays pay for moving elements.

### W4 — Priority Processing

The heap receives `n` values and then performs `n` `extractMin()` operations. The output is checked to be non-decreasing.

Expected result: every extraction is O(log n) in the worst case and the complete sequence is sorted.

## 6. Operation Counters

The implementation counts operations inside the data-structure methods:

- **steps** — one array-cell read or one movement to the next node
- **moves** — one array-element shift or one pointer/link update
- **comparisons** — one comparison between elements

This makes the counters independent of the wall-clock timer and allows the report to explain why two implementations with the same Big-O complexity can still have different real performance.

## 7. Results

The complete CSV is stored in `results/results.csv`.

The generated figures are:

- `W1_time_vs_n.png`
- `W1_metrics_vs_n.png`
- `W2_time_vs_n.png`
- `W2_metrics_vs_n.png`
- `W3_head_time_vs_n.png`
- `W3_head_metrics_vs_n.png`
- `W3_middle_time_vs_n.png`
- `W3_middle_metrics_vs_n.png`
- `W4_time_vs_n.png`
- `W4_metrics_vs_n.png`

### Important benchmark note

The local execution environment used while preparing this archive does not have Maven and uses Java 21 rather than the project's required Java 25. A complete `n = 100,000` linked-list timing run is also too expensive for this environment and exceeded the execution limit. Therefore the `n = 100,000` **time values in the supplied CSV are provisional estimates**, while the operation-count fields follow the implemented algorithms. Before final submission, run the benchmark on the student's own Java 25/Maven environment and replace the provisional timing rows. This is important because wall-clock time depends on the CPU, JVM and system load.

## 8. Cache Locality and Pointer Chasing

`DynamicArray` is usually faster for `get(i)` because the address of an element can be calculated directly. Array elements are stored contiguously, so loading one cache line can bring several nearby elements into the CPU cache. Sequential iteration therefore has good spatial locality.

A linked list stores nodes separately. Accessing an indexed element requires following pointers from node to node. This pointer chasing can cause cache misses because the next node may be far away in memory. Linked-list nodes also contain reference fields and object-management overhead.

Consequently, two structures can have similar asymptotic complexity but noticeably different real execution times. Big-O describes how the number of operations grows, but it does not fully describe memory hierarchy effects, object allocation, cache behavior or garbage collection.

## 9. When to Use Each Structure

`DynamicArray` is the better choice when fast random access is important and insertions in the middle are not dominant.

`MyLinkedList` is useful when frequent insertions/removals occur near a known node or at the head, because links can be updated without shifting a whole array.

`MinHeap` is the correct choice for priority-based workloads where the smallest element must be retrieved repeatedly.

## 10. Testing

JUnit 5 tests cover:

- normal insertion and removal
- indexed access
- `contains`
- empty structures
- one-element behavior
- invalid indexes
- empty heap exceptions
- non-decreasing output of repeated `extractMin()`

The heap property is maintained after insertion and extraction.

## 11. Git Workflow

Recommended history:

```text
main
feature/array
feature/list
feature/heap
feature/metrics
```

Example commit messages:

```text
feat(array): add dynamic resizing
feat(list): implement indexed operations
feat(heap): implement bubble-up and bubble-down
test(heap): check heap property
docs(report): add complexity analysis
```

The final release should be tagged:

```text
v1.0
```

## 12. Conclusion

The assignment demonstrates that choosing a data structure depends on the workload, not only on a single Big-O value. DynamicArray provides constant-time indexed access and good cache locality. MyLinkedList avoids array shifting but pays the cost of pointer traversal and memory indirection. MinHeap efficiently supports priority processing with logarithmic insertion and extraction. The operation counters provide a structural explanation of the benchmark, while the timing results show the additional effect of the memory hierarchy and JVM execution environment.
