package javanet.c04;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Java网络程序，其工作过程如下：
 * 客户端能够随机生成若干个数（数值范围0-1000，数值个数不定）并通过UDP协议发送到服务器；
 * 服务器端能够接收到这些数值后将其累加并将结果返回给客户端；客户端接收到结果后打印后结束。
 */
public class Exercise2_2_S {
    public static void main(String[] args) throws IOException {
        DatagramSocket server = new DatagramSocket(1314);
        byte[] buf = new byte[400];
        DatagramPacket request = new DatagramPacket(buf, buf.length);
        System.out.println("Waiting");
        server.receive(request);
        System.out.println("Received");
        int sum = 0;
        //输出内容
        for (int i = 0; i < 400; i = i + 4) {
            int j = (buf[i] & 0xff) << 24 | (buf[i + 1] & 0xff) << 16 | (buf[i + 2] & 0xff) << 8 | (buf[i + 3] & 0xff);
            sum += j;
        }
        System.out.println("Summary: "+sum);

        byte[] response = new byte[4];
        response[0] = (byte) ((sum >> 24) & 0xff);
        response[1] = (byte) ((sum >> 16) & 0xff);
        response[2] = (byte) ((sum >> 8) & 0xff);
        response[3] = (byte) (sum & 0xff);
        request.setData(response);
        server.send(request);
        System.out.println("Response Over");
    }
}
