package javanet.c02;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 2.	编写一个Java程序，该程序将启动4个线程，其中3个是掷硬币线程，1个是主线程。
 * 每个掷硬币线程将连续掷出若干次硬币（10次以内，次数随机生成）；主线程将打印出
 * 正面出现的总次数以及正面出现的概率。
 */
public class Exercise1_2 extends Thread {


    public static void main(String[] args) throws InterruptedException, ExecutionException {

        @SuppressWarnings("rawtypes")
        FutureTask[] tasks = new FutureTask[3];

        for(int i=0;i<3;i++) {
            tasks[i] = new FutureTask<>(new CoinCounter());
            new Thread(tasks[i]).start();
        }

        System.out.println("投掷总数        正面数         比率");
        for(int i=0;i<3;i++) {
            @SuppressWarnings("unchecked")
            List<Integer> result =(List<Integer>)tasks[i].get();

            System.out.println(result.get(0)+"    "+result.get(1)+"    "+result.get(1)/((double)result.get(0)));
        }
    }
}

class CoinCounter implements Callable<List<Integer>> {

    @Override
    public List<Integer> call() {
        ArrayList<Integer> list = new ArrayList<>();

        int num = (int) (Math.random() * 10+1);
        int count = 0;
        for (int i = 0; i < num; i++) {
            if (getResult())
                count++;
        }
        list.add(num);
        list.add(count);
        return list;
    }

    // 抛硬币，正面为true，反面为false
    private boolean getResult() {
        return Math.random() > 0.5;
    }

}