package javanet.c02;

/**
 * 3.	利用线程通知机制编写一个Java多线程程序，模拟商店对某件商品的进货与销售过程并将相关信息打印出来，
 * 具体要求如下：
 * 进货与销售过程各由一个线程模拟；
 * 当商品数目少于10时进货，进货数目随机生成但不少于50；
 * 销售数目随机生成，数目不大于商品数量；2次销售之间的时间随机生成，但不大于2s。
 */
public class Exercise2_3 {

    public static void main(String[] args) {
        Pool pool = new Pool();
        Producer producer = new Producer(pool);
        Customer customer = new Customer(pool);
        producer.start();
        customer.start();
    }

}

class Pool {
    private int num = 0;

    int getNum() {
        return num;
    }

    synchronized void sell(int num) {
        if (this.num < num) {
            return;
        }
        this.num -= num;
        System.out.println("get " + num + " goods,now pool has " + getNum() + "goods");

        if(this.num<10)
            this.notify();
    }

    synchronized void buy(int num) {
        this.num += num;
        System.out.println("################push " + num + " goods,now pool has " + getNum() + "goods###########");
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class Producer extends Thread {
    private Pool pool;

    Producer(Pool pool) {
        this.pool = pool;
    }

    public void run() {
        while (true) {
            int num = ((int) (Math.random() * 100) + 50);
            try {
                pool.buy(num);
            }catch (Exception e){
                break;
            }
        }
    }
}

class Customer extends Thread {
    private Pool pool;

    Customer(Pool pool) {
        this.pool = pool;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep((long) (2000 * Math.random()));
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            int num = (int) (Math.random() * pool.getNum() + 1);
            pool.sell(num);
        }
    }
}