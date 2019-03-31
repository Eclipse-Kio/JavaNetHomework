package javanet.c03;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * 编写一个简单的Java网络程序，该程序能够获取本机的IP地址与MAC地址
 */
public class Exercise1_2 {
    public static void main(String[] args) {
        try {
            System.out.println("Reading Local Internet Config...");
            InetAddress address = InetAddress.getByName("localhost");
            System.out.println("IP Address:"+address);

            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements()){
                NetworkInterface item = interfaces.nextElement();
                System.out.println(item.getName()+" :  "+ Arrays.toString(item.getHardwareAddress()));
            }


        } catch (Exception e) {
            System.out.println("Read Fail!");
            e.printStackTrace();
        }
    }
}
