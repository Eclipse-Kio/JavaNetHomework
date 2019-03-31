package javanet.c03;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * 编写一个简单的Java网络程序，
 * 该程序能够通过扫描服务对应的相关端口判断服务器上的daytime、echo以及web服务是否处于运行状态。
 */
public class Exercise1_3 {
    public static void main(String[] args) {
        System.out.println("Input an exercise.Exercise3_1_Server Address");
        Scanner input = new Scanner(System.in);

        String address = input.nextLine();

        System.out.print("Echo: ");
        testPort(address,7);
        System.out.print("DayTime: ");
        testPort(address,13);
        System.out.print("Web: ");
        testPort(address,80);

       input.close();
    }

    private static void testPort(String host,int port){
        try {
            @SuppressWarnings("unused")
            Socket socket = new Socket(host,port);
            System.out.println("Open");
        } catch (ConnectException e) {
            System.out.println("Time out");
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host");
        } catch (IOException e) {
            System.out.println("Access Failed");
        }
    }
}
