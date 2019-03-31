package javanet.c03;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * 编写一个简单的Java网络程序，该程序能够获取常见网站的IP地址
 */
public class Exercise1_1 {
    public static void main(String[] args) {
        System.out.println("Please Input a Website Name:");
        Scanner input = new Scanner(System.in);
        String webAddress = input.nextLine();

        try {
            InetAddress ipAddress = InetAddress.getByName(webAddress);
            System.out.println("Cached Address:"+ipAddress);
        } catch (UnknownHostException e) {
            System.out.println("Sorry,We Can't Access to The Address!");
        }finally {
            input.close();
        }

    }
}
