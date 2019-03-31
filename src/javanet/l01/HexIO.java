package javanet.l01;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 1．	编写一个能够将文件数据以16进制方式显示的程序。
 * 2．	编写一个程序能实现文件的复制功能。
 * 3．	完成将a文件夹下的文件拷贝到b文件夹里。
 * a和b文件夹可能存在相同文件，若b文件夹下已经存在a文件夹中的文件则不拷贝，
 * 若不存在则拷贝。
 */
public class HexIO {
    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream("files/l02/Test.txt");
            FileWriter fw = new FileWriter("files/l02/TestOut.txt");
            int b, n = 0;
            while ((b = fis.read()) != -1) {
                fw.write(" " + Integer.toHexString(b));
                if (((++n) % 10) == 0) fw.write("\r\n");
            }
            fis.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
