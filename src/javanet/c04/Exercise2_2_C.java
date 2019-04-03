package javanet.c04;

import java.io.IOException;
import java.net.*;

/**
 * Java网络程序，其工作过程如下：
 * 客户端能够随机生成若干个数（数值范围0-1000，数值个数不定）并通过UDP协议发送到服务器；
 * 服务器端能够接收到这些数值后将其累加并将结果返回给客户端；客户端接收到结果后打印后结束。
 */
public class Exercise2_2_C {
    public static void main(String[] args) throws IOException {
        int num = (int) (Math.random() * 100);
        byte[] data = new byte[4 * num];
        for (int i = 0; i < 4 * num; i = i + 4) {
            int item = (int) (Math.random() * 1000);
            data[i] = (byte) ((item >> 24) & 0xff);
            data[i + 1] = (byte) ((item >> 16) & 0xff);
            data[i + 2] = (byte) ((item >> 8) & 0xff);
            data[i + 3] = (byte) (item & 0xff);
        }
        InetAddress address = InetAddress.getByName("localhost");
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, address, 1314);
        DatagramSocket socket = new DatagramSocket();
        System.out.println("Sending Data...");
        socket.send(datagramPacket);

        //接收返回的值
        byte[] result = new byte[4];
        DatagramPacket receive = new DatagramPacket(result, result.length, address, 1314);
        socket.receive(receive);

        int resultInt = (result[0] & 0xff) << 24 | (result[1] & 0xff) << 16 | (result[2] & 0xff) << 8 | (result[3] & 0xff);
        System.out.println("Received Result: " + resultInt);
    }
}
