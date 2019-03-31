package javanet.c03;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * 编写一Java网络程序，其工作过程如下：
 * 客户端能够接收用户输入的两个数并发送到服务器；
 * 服务器端能够接收到这两个数并将其相加并将结果返回给客户端；
 * 客户端接收到结果后打印出来并关闭连接。
 */
public class Exercise2_2_Customer {
    public static void main(String[] args) {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Please Input 2 Numbers:");
            int a = input.nextInt();
            int b = input.nextInt();
            Socket socket = new Socket("localhost", 1314);
            System.out.println("$exercise.Exercise3_1_Customer: Connected");
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println(a);
            writer.println(b);
            System.out.println("$exercise.Exercise3_1_Customer: Printed To Stream");
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String result = reader.readLine();
            System.out.println("$exercise.Exercise3_1_Customer: Got Summary: " + result);


            System.out.println("$exercise.Exercise3_1_Customer: Exit");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

