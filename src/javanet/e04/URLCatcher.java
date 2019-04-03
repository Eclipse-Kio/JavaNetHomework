package javanet.e04;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * 该类使用了一个Jar包，方便进行HTML的解析
 */
public class URLCatcher {
    public static void main(String[] args) throws IOException {
        URL url = new URL("http://www.baidu.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder data= new StringBuilder();
        String line;
        while((line = in.readLine())!=null)
            data.append(line);
        //将获得的数据转换为可用的HTML Document
        Document document = Jsoup.parse(data.toString());
        //从Element中获取第一个form表单
        Element form = document.getElementsByTag("form").first();
        //遍历该表单中的每一个Element
        for (Element item:form.children()){
            //若该Element为input，则输出其name属性值
            if (item.tag().equals(Tag.valueOf("input"))){
                System.out.println(item.attr("name"));
            }
        }
    }
}
