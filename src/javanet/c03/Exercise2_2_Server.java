package javanet.c03;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 编写一Java网络程序，其工作过程如下：
 * 客户端能够接收用户输入的两个数并发送到服务器；
 * 服务器端能够接收到这两个数并将其相加并将结果返回给客户端；
 * 客户端接收到结果后打印出来并关闭连接。
 */
public class Exercise2_2_Server {
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(1314);
            System.out.println("$exercise.Exercise3_1_Server: exercise.Exercise3_1_Server Waiting Connection!");
            Socket socket = serverSocket.accept();
            System.out.println("$exercise.Exercise3_1_Server: Accepted An Connection");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            int m = Integer.parseInt(reader.readLine());
            int n = Integer.parseInt(reader.readLine());
            System.out.println("$exercise.Exercise3_1_Server: Accept m("+m+"),n("+n+")");
            int result = m + n;
            System.out.println("$exercise.Exercise3_1_Server: Result Is "+result);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println(result);
            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
