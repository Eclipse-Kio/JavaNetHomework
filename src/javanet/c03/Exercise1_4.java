package javanet.c03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

/**
 * 编写一个Java程序，
 * 该程序能够通过TCP协议与daytime服务通信，
 * 获取并打印服务器时间（输出时间格式为“2019年3月10日  18时30分”）。
 */
public class Exercise1_4 {
    public static void main(String[] args) {
        System.out.println("Please Input an DayTime exercise.Exercise3_1_Server Address");
        Scanner input = new Scanner(System.in);
        String address = input.nextLine();
        try {
            Socket socket = new Socket(address,13);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String result;
            System.out.println("Reply From exercise.Exercise3_1_Server");
            while ((result=reader.readLine())!=null){
                System.out.println("  "+result);
            }
        } catch (ConnectException e1) {
            System.out.println("Connection Timeout,The exercise.Exercise3_1_Server Might Not Available");
        } catch (IOException e2){
            System.out.println("Error During Message Deliver");
        }

        input.close();
    }
}
