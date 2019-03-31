package javanet.c03;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Exercise2_3_Customer {
    public static void main(String[] args) {
        for (int i=0;i<3;i++){
            sendData(i+1);
        }
    }

    private static void sendData(int num){
        try {

            System.out.println("-----------------------" + num + "---------------------------");
            Socket socket = new Socket("localhost", 1314);
            System.out.println("$exercise.Exercise3_1_Customer: Connected");
            Scanner input = new Scanner(System.in);
            System.out.println("Please Input Some Numbers:(needs enter operation,# to finish)");
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String userIn;
            while(!(userIn = input.nextLine()).equals("#")){
                writer.println(userIn);
            }
            writer.println("#");
            writer.flush();
            System.out.println("$exercise.Exercise3_1_Customer: Printed To Stream");


            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String s1 = reader.readLine();
            String s2 = reader.readLine();
            String s3 = reader.readLine();
            System.out.println("$exercise.Exercise3_1_Customer: Got Summary: " + s1 + ", average: " + s2 + ", variance: " + s3);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

