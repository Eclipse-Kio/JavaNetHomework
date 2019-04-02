package javanet.l04;

import java.io.IOException;
import java.net.*;

/**
 * 客户端每隔1秒向服务端发送一个温度值，服务端能够接收该温度值并显示一条动态温度曲线
 */

public class Customer{
    public static void main(String[] args) throws UnknownHostException, SocketException {
        InetAddress address = InetAddress.getByName("localhost");
        DatagramSocket socket = new DatagramSocket();

        new Thread(() -> {
            while (true){
                byte[] data =  new byte[1];
                data[0] = (byte)(Math.random()*128);
                DatagramPacket datagramPacket = new DatagramPacket(data,0,data.length,address,1314);
                try {
                    socket.send(datagramPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            socket.close();

        }).start();

    }
}
