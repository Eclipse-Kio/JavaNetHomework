package javanet.c05;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Exercise3_1 {
    public static void main(String[] args) throws IOException {
        System.out.println("请输入页面地址");
        Scanner input = new Scanner(System.in);
        String url = input.nextLine();

        Document document = Jsoup.connect(url).get();
        System.out.println("页面中的图片链接");
        //获取图片地址
        Elements images = document.getElementsByTag("img");
        for (Element image : images) {
            System.out.println(image.attr("src"));
        }
        System.out.println("页面中的JS链接");
        //JS名
        Elements scripts = document.getElementsByTag("script");
        for (Element script : scripts) {
            System.out.println(script.attr("src"));
        }
        System.out.println("页面中的CSS链接");
        Elements styles = document.getElementsByTag("link");
        for (Element style : styles) {
            if (style.attr("stylesheet").equals("stylesheet"))
                System.out.println(style.attr("href"));
        }
        System.out.println("页面中的超链接");
        Elements links = document.getElementsByTag("a");
        for (Element link : links) {
            System.out.println(link.attr("href"));
        }

        //模拟登录
        System.out.println("请输入用户名");
        String username = input.nextLine();
        System.out.println("请输入密码");
        String password = input.nextLine();
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);

        Connection connection = Jsoup.connect("http://localhost:8080/doLogin.jsp");
        connection.data(parameters).method(Connection.Method.POST).execute();

        String response = connection.response().body();
        System.out.println(response);

    }

}
