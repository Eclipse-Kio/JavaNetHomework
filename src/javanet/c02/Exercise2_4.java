package javanet.c02;

import java.util.ArrayList;

/**
 * 4.	设现有50人抢购20件商品（每件商品的价格20-100元不等），要求如下：
 * 每人购买的商品不能够超过3件；购买的商品价格总额不能够超过100元；
 * 抢购时不知商品价格，结账时才知道商品价格；如果超过限额将退回部分商品，
 * 退回策略自定，退回后可再次抢购。
 * 请编写一个Java多线程程序，模拟并打印出抢购过程。
 */
public class Exercise2_4 {

    public static void main(String[] args) {

        GoodsPool pool = new GoodsPool(20);
        for (int i = 0; i < 50; i++) {
            GoodGetter getter = new GoodGetter(pool, "Thread " + i);
            getter.start();
        }

    }
}

class GoodGetter extends Thread {
    private GoodsPool pool;
    private ArrayList<Good> bought;

    GoodGetter(GoodsPool pool, String name) {
        super(name);
        this.pool = pool;
        bought = new ArrayList<>();
    }

    @Override
    public void run() {

        while (bought.size() < 3) {
            long start = System.currentTimeMillis();
            Good good = pool.get();
            long end = System.currentTimeMillis();

            //长时间无法获得商品，认定商店已没有货物，也不会再有人放回
            if (end - start > 500) {
                break;
            }

            //取到了数据则放入自己的口袋，然后计算是否能支付
            if (good != null) {
                bought.add(good);
                checkPrice();
            }
        }

        System.out.println(this.getName() + this.bought);
    }


    private void checkPrice() {
        int sum = 0;
        for (Good good : bought) {
            sum += good.getPrice();
        }
        if (sum > 100) {
            //超出100，丢弃第一个商品，再计算能否支付
            pool.push(bought.remove(0));
            checkPrice();
        }

    }

}

class GoodsPool {
    private ArrayList<Good> list;
    private final int maxmium;

    GoodsPool(int num) {
        maxmium = num;
        list = new ArrayList<>();
        for (int i = 0; i < maxmium; i++)
            list.add(new Good());
    }

    synchronized Good get() {
        Good good = null;

        if (list.size() > 0) {
            good = list.remove(0);
        } else {
            try {
                this.wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return good;
    }

    /**
     * @param good The good needs to be push
     */
    synchronized void push(Good good) {

        if (list.size() < maxmium) {
            list.add((int) (Math.random() * list.size()), good);
            this.notifyAll();
        }
    }

}

class Good {
    private int price;

    Good() {
        this.price = (int) (Math.random() * 81 + 20);
    }

    int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Good [price=" + price + "]";
    }

}