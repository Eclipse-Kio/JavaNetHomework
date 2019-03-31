package javanet.c01;

import javanet.c01.entity.*;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 *  2.	编写一个Java程序，该程序能够从shapes.data文件读取形状信息并打印出来
 */
public class Exercise2_2 {

    private void exercise2_2() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("files/c01/shape.dat"));

        System.out.println("正在从硬盘中读取对象");
        while (true) {
            try {

                Shape shape = (Shape) ois.readObject();
                shape.printInfo();
                Thread.sleep(200);


            } catch (EOFException e) {
                ois.close();
                break;
            }
        }
        System.out.println("读取完成");
    }

    public static void main(String[] args) throws Exception {
        new Exercise2_2().exercise2_2();
    }
}
