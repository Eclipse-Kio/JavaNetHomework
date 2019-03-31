package javanet.c02;

/**
 * 1.	编写一个Java程序，该程序将启动4个线程，
 * 其中3个是掷硬币线程，1个是主线程；
 * 每个掷硬币线程将连续掷出20次硬币，
 * 如果出现3次以上的正面便将其打印出来。
 */
public class Exercise1_1  extends Thread{
    private int id;


    public Exercise1_1(int id) {
        this.id = id;
    }

    //抛硬币，正面为true，反面为false
    private boolean getResult() {
        return Math.random()>0.5;
    }

    @Override
    public void run() {
        int num = 0;
        for(int i=0;i<20;i++)
            if(getResult())num++;
        if(num>2)
            System.out.println("Thread "+id+" has "+num+" times");
    }

    public static void main(String[] args) {
        for(int i=0;i<3;i++) {
            new Exercise1_1(i).start();
        }
    }

}

