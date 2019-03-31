package javanet.c04;

import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 编写一个Java程序，该程序能够通过UDP与time服务通信，
 * 获取并打印服务器时间（输出时间格式为“2019年3月10日
 * 18时30分20秒”）。
 * 说明：服务器可以选择“time.nist.gov”，注意time协议与daytime协议不同，该协议详情请参考“RFC 868”以及网络上的相关资料。
 */
public class Exercise1_1 {
    public static void main(String[] args) {
        byte[] msg = new byte[4];
        try {
            InetAddress address = InetAddress.getByName("time.nist.gov");
            DatagramPacket packet = new DatagramPacket(msg, msg.length, address, 37);
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(packet);

            byte[] feedback = new byte[4];
            DatagramPacket datagramPacket = new DatagramPacket(feedback, feedback.length, address, 37);
            datagramSocket.receive(datagramPacket);

            StringBuilder second = new StringBuilder();
            //转化为二进制字符串
            for (byte item : feedback) {
                StringBuilder binaryString = new StringBuilder(Long.toBinaryString(Byte.toUnsignedLong(item)));
                for (int i = 0; i < 8 - binaryString.length(); i++) {
                    binaryString.insert(0, "0");
                }
                second.append(binaryString);
            }
            //得到从1900年至今过去的毫秒数
            long ms = Long.parseLong(second.toString(), 2) * 1000;
            //Java从1970年开始算，所以需要减去当时的毫秒数
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(1900, Calendar.JANUARY, 1);
            long delta = calendar.getTimeInMillis();
            //转化为从1970年至今过去的毫秒数
            long need = ms + delta;
            //转换成Date类型
            Date computed = new Date(need);
            //格式化输出
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");

            System.out.println("服务器返回的时间是(英国格林尼治时间)："+format.format(computed));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}