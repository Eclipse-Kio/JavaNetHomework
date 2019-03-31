package javanet.c02;

import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 3.	编写一个Java程序，该程序能够将任务n！分解给m个线程执行；请比较单线程算
 * 法与多线程算法的执行时间。
 **/
public class Exercise1_3 {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        try {

            System.out.println("Please input n");
            int n = input.nextInt();
            System.out.println("Please input m");
            int m = input.nextInt();
            if (m > n)
                m = n;

            // Single Thread
            long start = System.nanoTime();

            long sum = 1;
            for (long i = 1; i <= n; i++) {
                sum = sum * i;
            }
            long end = System.nanoTime();
            System.out.println("--Single Thread-----");
            System.out.println("	Result is " + sum);
            System.out.println("	Time spend:" + (end - start) + "ns");

            // Multiple Thread

            @SuppressWarnings("rawtypes")
            FutureTask[] threads = new FutureTask[m];

            int num = n / m;
            start = System.nanoTime();
            // Initialize Thread Task
            for (int i = 0; i < threads.length; i++) {
                if (i == threads.length - 1)
                    threads[i] = new FutureTask<>(new MyThread(1 + num * i, n));
                else
                    threads[i] = new FutureTask<>(new MyThread(1 + num * i, num * (i + 1)));
                new Thread(threads[i]).start();
            }
            sum = 1;

            for (FutureTask thread : threads) {
                sum = sum * (long) thread.get();
            }
            end = System.nanoTime();

            System.out.println("--Multiple Thread-----");
            System.out.println("	Result is " + sum);
            System.out.println("	Time spend:" + (end - start) + "ns");

        } catch (Exception e) {
            System.out.println("Error!");
            e.printStackTrace();
        }

        input.close();
    }

}

class MyThread implements Callable<Long> {
    private int start;
    private int end;
    private long result;

    MyThread(int start, int end) {
        this.start = start;
        this.end = end;

        this.result = 1;
    }
    @Override
    public Long call() {
        for (int i = start; i <= end; i++) {
            result = result * i;
        }
        return result;
    }
}
