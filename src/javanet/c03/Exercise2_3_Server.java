package javanet.c03;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Exercise2_3_Server {
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        System.out.println("$exercise.Exercise3_1_Server: exercise.Exercise3_1_Server Waiting Connection!");
        serverSocket = new ServerSocket(1314);
        for (int i = 0; i < 3; i++) {
            serve(i + 1);
        }
    }

    private static void serve(int num) {
        try {
            System.out.println("-----------------------" + num + "---------------------------");
            Socket socket = serverSocket.accept();
            System.out.println("$exercise.Exercise3_1_Server: Accepted An Connection");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String get;

            int sum = 0;
            ArrayList<Integer> numbers = new ArrayList<>();

            while (!(get = reader.readLine()).contains("#")) {
                int item = Integer.parseInt(get);
                sum += item;
                numbers.add(item);
            }

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            float average = sum/(float)(numbers.size());
            float deltaSum = 0;
            for (int i:numbers) {
                deltaSum +=(average-i)*(average-i);
            }


            writer.println(sum);
            writer.println(average);
            System.out.println(deltaSum);
            writer.println(Math.sqrt(deltaSum/((float)numbers.size())));

            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
