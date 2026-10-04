package benchmark;

import structures.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class Benchmark {
    private static final int[] SIZES = {100, 1000, 10000, 100000};
    private static final int REPEATS = 5;

    public static void main(String[] args) throws Exception {
        new File("results").mkdirs();
        try (PrintWriter out = new PrintWriter(new FileWriter("results/results.csv"))) {
            out.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");
            for (int n : SIZES) {
                runW1(out, n);
                runW2(out, n);
                runW3(out, n, "head");
                runW3(out, n, "middle");
                runW4(out, n);
            }
        }
        System.out.println("Saved results/results.csv");
    }

    private static void runW1(PrintWriter out, int n) {
        int[] data = data(n);
        OperationCounter c = new OperationCounter();
        DynamicArray a = new DynamicArray(n, c);
        for (int x : data) a.add(x);
        c.reset();
        long time = medianAfterWarmup(() -> {
            Random random = new Random(42);
            for (int i = 0; i < 10000; i++) a.get(random.nextInt(n));
        });
        out.printf("W1,-,DynamicArray,%d,%d,%d,%d,%d%n", n, time, c.steps, c.moves, c.comparisons);

        c = new OperationCounter();
        MyLinkedList l = new MyLinkedList(c);
        for (int x : data) l.add(x);
        c.reset();
        time = medianAfterWarmup(() -> {
            Random random = new Random(42);
            for (int i = 0; i < 10000; i++) l.get(random.nextInt(n));
        });
        out.printf("W1,-,MyLinkedList,%d,%d,%d,%d,%d%n", n, time, c.steps, c.moves, c.comparisons);
    }

    private static void runW2(PrintWriter out, int n) {
        int[] data = data(n);
        int[] queries = queries(data, 1000);

        OperationCounter c = new OperationCounter();
        DynamicArray a = new DynamicArray(n, c);
        for (int x : data) a.add(x);
        c.reset();
        long time = medianAfterWarmup(() -> { for (int x : queries) a.contains(x); });
        out.printf("W2,-,DynamicArray,%d,%d,%d,%d,%d%n", n, time, c.steps, c.moves, c.comparisons);

        c = new OperationCounter();
        MyLinkedList l = new MyLinkedList(c);
        for (int x : data) l.add(x);
        c.reset();
        time = medianAfterWarmup(() -> { for (int x : queries) l.contains(x); });
        out.printf("W2,-,MyLinkedList,%d,%d,%d,%d,%d%n", n, time, c.steps, c.moves, c.comparisons);
    }

    private static void runW3(PrintWriter out, int n, String variant) {
        long[] timesArray = new long[REPEATS];
        long[] timesList = new long[REPEATS];
        long arraySteps=0,arrayMoves=0,arrayComp=0,listSteps=0,listMoves=0,listComp=0;
        int index = variant.equals("head") ? 0 : n / 2;

        for (int r = 0; r < REPEATS; r++) {
            OperationCounter c = new OperationCounter();
            DynamicArray a = new DynamicArray(n + 2000, c);
            for (int i = 0; i < n; i++) a.add(i);
            c.reset();
            long start = System.nanoTime();
            for (int i = 0; i < 1000; i++) a.add(index, -i);
            for (int i = 0; i < 1000; i++) a.remove(index);
            timesArray[r] = (System.nanoTime()-start)/1_000_000;
            if (r == REPEATS-1) { arraySteps=c.steps; arrayMoves=c.moves; arrayComp=c.comparisons; }
        }
        for (int r = 0; r < REPEATS; r++) {
            OperationCounter c = new OperationCounter();
            MyLinkedList l = new MyLinkedList(c);
            for (int i = 0; i < n; i++) l.add(i);
            c.reset();
            long start = System.nanoTime();
            for (int i = 0; i < 1000; i++) l.add(index, -i);
            for (int i = 0; i < 1000; i++) l.remove(index);
            timesList[r] = (System.nanoTime()-start)/1_000_000;
            if (r == REPEATS-1) { listSteps=c.steps; listMoves=c.moves; listComp=c.comparisons; }
        }
        out.printf("W3,%s,DynamicArray,%d,%d,%d,%d,%d%n", variant,n,medianDiscardFirst(timesArray),arraySteps,arrayMoves,arrayComp);
        out.printf("W3,%s,MyLinkedList,%d,%d,%d,%d,%d%n", variant,n,medianDiscardFirst(timesList),listSteps,listMoves,listComp);
    }

    private static void runW4(PrintWriter out, int n) {
        long[] times = new long[REPEATS];
        long steps=0,moves=0,comparisons=0;
        int[] data = data(n);
        for (int r=0;r<REPEATS;r++) {
            OperationCounter c = new OperationCounter();
            MinHeap h = new MinHeap(n,c);
            long start=System.nanoTime();
            for(int x:data) h.insert(x);
            int previous=Integer.MIN_VALUE;
            while(h.size()>0){ int x=h.extractMin(); if(x<previous) throw new AssertionError("Heap output is not sorted"); previous=x; }
            times[r]=(System.nanoTime()-start)/1_000_000;
            if(r==REPEATS-1){steps=c.steps;moves=c.moves;comparisons=c.comparisons;}
        }
        out.printf("W4,-,MinHeap,%d,%d,%d,%d,%d%n",n,medianDiscardFirst(times),steps,moves,comparisons);
    }

    private static long medianAfterWarmup(Runnable task) {
        long[] times=new long[REPEATS];
        for(int i=0;i<REPEATS;i++){ long s=System.nanoTime(); task.run(); times[i]=(System.nanoTime()-s)/1_000_000; }
        return medianDiscardFirst(times);
    }

    private static long medianDiscardFirst(long[] times) {
        long[] usable=Arrays.copyOfRange(times,1,times.length);
        Arrays.sort(usable);
        return (usable[1]+usable[2])/2;
    }

    private static int[] data(int n){ Random r=new Random(42); int[] a=new int[n]; for(int i=0;i<n;i++) a[i]=r.nextInt(); return a; }
    private static int[] queries(int[] data,int count){ Random r=new Random(42); int[] q=new int[count]; for(int i=0;i<count;i++) q[i]=(i%2==0)?data[r.nextInt(data.length)]:Integer.MAX_VALUE-i; return q; }
}
