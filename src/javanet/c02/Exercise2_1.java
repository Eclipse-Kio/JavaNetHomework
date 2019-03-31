package javanet.c02;

import java.util.Scanner;

/**
 * 利用线程同步机制编写一个Java多线程程序，模拟100个球迷抢购20张门票的过程。
 */
public class Exercise2_1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        try {
            System.out.println("Please input ticket number");
            int ticketNum = input.nextInt();
            System.out.println("Please input fans number");
            int fansNum = input.nextInt();

            // create a seller
            TicketSeller seller = new TicketSeller(ticketNum);

            // create fans
            for (int i = 0; i < fansNum; i++)
                new Fans("fans" + i, seller);
            input.close();
        } catch (Exception e) {
            System.out.println("Error Input");
            input.close();
        }

    }
}

class TicketSeller {
    private int num;

    TicketSeller(int num) {
        this.num = num;
    }

    synchronized boolean buyTicket() {
        if (this.num >= 1) {
            this.num--;
            return true;
        }
        return false;
    }

}

class Fans extends Thread {
    private String name;
    private TicketSeller seller;

    Fans(String name, TicketSeller seller) {
        this.name = name;
        this.seller = seller;
        this.start();
    }

    // buy ticket
    @Override
    public void run() {
        int num = 0;

        while (seller.buyTicket()) {
            num++;
            Thread.yield();//avoid one thread get too many tickets
        }
        System.out.println("My name is " + this.name + ",I bought " + num + "ticket(s)");
    }
}
