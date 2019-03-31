package javanet.c04;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
 * 1.	编写一个Java服务器程序，该程序能够返回一个随机的校验码
 * （端口号，校验码长度自定，由数字与字母组成，每个字符占1个字节，区分大小写）
 * 给客户端，请使用sockit工具与其通信并测试程序运行是否正常。
 */
public class Exercise2_1 {
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(1314)) {
            System.out.println("Waiting");
            DatagramPacket request = new DatagramPacket(new byte[1], 0, 1);
            socket.receive(request);
            System.out.println("Getting");
            byte[] data = new byte[10];
            //随机生成数据
            for (int i=0;i<data.length;i++){
                int type = (int)(Math.random()*3);
                if (type==0){//发送数字
                    data[i] = (byte)(Math.random()*10+48);
                }else if (type ==1){//发送大写字母
                    data[i] = (byte)(Math.random()*26+65);
                }else{//发送小写字母
                    data[i] = (byte)(Math.random()*26+97);
                }
            }
            System.out.println(Arrays.toString(data));
            InetAddress client = request.getAddress();
            DatagramPacket response = new DatagramPacket(data, data.length, client, request.getPort());
            socket.send(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
