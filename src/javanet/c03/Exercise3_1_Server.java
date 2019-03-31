package javanet.c03;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Exercise3_1_Server implements Runnable{
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(1314);
            System.out.println("$exercise.Exercise3_1_Server: exercise.Exercise3_1_Server Waiting Connection!");
            Exercise3_1_Server server = new Exercise3_1_Server();
            new Thread(server).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Socket socket = serverSocket.accept();
            new Thread(this).start();

            int sum = 0;
            ArrayList<Integer> numbers = new ArrayList<>();

            System.out.println("$exercise.Exercise3_1_Server: Accepted An Connection");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String get;

            while (!(get = reader.readLine()).contains("#")) {
                int item = Integer.parseInt(get);
                sum += item;
                numbers.add(item);
            }

            float average = sum/(float)(numbers.size());
            float deltaSum = 0;
            float variance;

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            for (int i:numbers) {
                deltaSum +=(average-i)*(average-i);
            }
            variance = deltaSum/((float)numbers.size());
            writer.println("总和："+sum+"     平均数："+average+"     方差："+variance);
            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
