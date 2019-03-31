package javanet.c02;

/**
 * 编写一个会产生死锁的Java多线程程序，并说明产生死锁的原因；
 */
public class Exercise2_2  {
    private static Object lock1 = new Object();
    private static Object lock2 = new Object();

    public static void main(String[] args) {
        new DeadLockThread("Thread1",lock1,lock2).start();
        new DeadLockThread("Thread2",lock2,lock1).start();
    }
}
class DeadLockThread extends Thread{
    private String name;

    private final Object lock1;
    private final Object lock2;


    DeadLockThread(String name, Object l1, Object l2) {
        this.name = name;
        this.lock1 = l1;
        this.lock2 = l2;
    }

    @Override
    public void run() {
        System.out.println("I am "+name+",I need lock"+this.lock1);
        synchronized(this.lock1) {
            //do something
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("I am "+name+",I got lock"+this.lock1);
            System.out.println("I am "+name+",I need lock"+this.lock2);
            synchronized(this.lock2) {
                System.out.println("I am "+name+",I got lock"+this.lock2);
            }
        }
    }
}