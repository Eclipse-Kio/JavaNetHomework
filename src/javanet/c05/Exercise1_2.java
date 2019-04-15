package javanet.c05;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * 编写一个Java程序，
 * 该程序能够从一个给定的url中获取协议、主机、端口号、路径、请求参数、定位位置等信息。
 */
public class Exercise1_2 {
    public static void main(String[] args) throws MalformedURLException {
        System.out.println("请输入URL");
        String urlString = new Scanner(System.in).nextLine();
        URL url = new URL(urlString);
        System.out.println("协议: " + url.getProtocol());
        System.out.println("主机：" + url.getHost());
        System.out.println("端口号：" + url.getPort());
        System.out.println("路径: " + url.getPath());
        System.out.println("请求参数：" + url.getQuery());
        System.out.println("定位位置： " + url.getRef());
    }
}
