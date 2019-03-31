package javanet.c03;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 编写一个Java服务器程序，该程序能够返回一个随机数给客户端，
 * 请使用telnet程序与其通信并测试程序运行是否正常
 */
public class Exercise2_1 {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1314);
            System.out.println("exercise.Exercise3_1_Server Waiting Connection!");
            Socket socket = serverSocket.accept();
            System.out.println("Accepted An Connection");
            int result = (int)(Math.random()*10000);
            System.out.println("Contributed An Num: "+result);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println(result);
            writer.flush();
            writer.close();
            socket.close();
            serverSocket.close();
            System.out.println("Connection Closed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
