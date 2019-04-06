package javanet.c04;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 2.	编写一个Java程序，该程序能够通过UDP协议与ntp服务进行通信，获取并打印服务器时间
 * （输出时间格式为“2019年3月10日  18时30分20秒”）。
 * 说明：服务器可以选择“ntp1.aliyun.com”，
 * 关于ntp服务的详情请参考“RFC 4330”与“RFC 5909”以及网络上的相关资料。
 */
public class Exercise1_2 {
    public static void main(String[] args) {
        try {
            TimeStamp timeStamp = new NTPUDPClient().getTime(InetAddress.getByName("ntp1.aliyun.com")).getMessage().getTransmitTimeStamp();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            System.out.println(dateFormat.format(timeStamp.getDate()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
