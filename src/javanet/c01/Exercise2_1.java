package javanet.c01;

/**
 * 1.	编写一个Java程序，该程序首先随机生成一个Shape List（List元素的类型为Circle或Rectangle）
 * 并打印出所有形状的相关信息，然后将该List以对象序列化的方式存储至shapes.data文件当中
 */
import javanet.c01.entity.*;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Exercise2_1 {

    private void exercise2_1() throws Exception {
        ArrayList<Shape> list = new ArrayList<>();

        // 随机1-20个元素
        int length = (int) (Math.random() * 20) + 1;

        System.out.println("即将写入硬盘的对象");

        for (int i = 0; i < length; i++) {
            Shape item;
            if (Math.random() > 0.5) {
                item = new Circle(10 * Math.random());
            } else {
                item = new Rectangle(10 * Math.random(), 10 * Math.random());
            }
            list.add(item);
            item.printInfo();
            Thread.sleep(200);
        }

        // 将随机数据写入文件

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/c01/shape.dat"));

        for(Shape i:list) {
            oos.writeObject(i);
            //oos.write(System.getProperty("line.separator").getBytes());
        }


        oos.close();
    }

    public static void main(String[] args) throws Exception {
        new Exercise2_1().exercise2_1();
        System.out.println("写入完成");
    }
}



