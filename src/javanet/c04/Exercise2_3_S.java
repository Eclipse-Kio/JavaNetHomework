package javanet.c04;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Exercise2_3_S {
    public final static int MAX_DISTANCE = 100;

    public static void main(String[] args) throws IOException {
        while (true) {
            byte[] request = new byte[8];
            DatagramPacket requestPacket = new DatagramPacket(request, 8);
            DatagramSocket server = new DatagramSocket(1314);
            try {
                server.receive(requestPacket);
                System.out.println("received");
            } catch (Exception e) {
                break;
            }
            //获取坐标
            int x = (request[0] & 0xff) << 24 | (request[1] & 0xff) << 16 | (request[2] & 0xff) << 8 | (request[3] & 0xff);
            int y = (request[4] & 0xff) << 24 | (request[5] & 0xff) << 16 | (request[6] & 0xff) << 8 | (request[7] & 0xff);
            int distance = (int) Math.sqrt(x * x + y * y);
            System.out.println(x + "/" + y + "/" + distance);
            byte[] response = new byte[1];
            if (distance > MAX_DISTANCE) {
                response[0] = 1;
            }
            requestPacket.setData(response);
            server.send(requestPacket);
            server.close();
        }

    }

}
