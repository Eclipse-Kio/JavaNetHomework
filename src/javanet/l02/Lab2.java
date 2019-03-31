package javanet.l02;

/**
 * <Strong>编写一个程序，该程序能够生成3个线程，每个线程从1输出到10，要求用两种方法实现；</Strong>
 *
 * @author Kio
 */

public class Lab2 {

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new MyThread(i + "").start();
            new Thread(new MyRunnable(i + "")).start();
        }
    }

}

class MyThread extends Thread {

    MyThread(String name) {
        this.setName("Thread " + name);
    }

    @Override
    public void run() {
        compute(this.getName());
    }

    static void compute(String name) {
        synchronized (System.out) {
            System.out.print(name + ": ");
            for (int i = 1; i < 11; i++) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }
}

class MyRunnable implements Runnable {

    private final String name;

    MyRunnable(String name) {
        this.name = "Runnable " + name;
    }

    @Override
    public void run() {
        MyThread.compute(this.getName());
    }

    private String getName() {
        return this.name;
    }

}
