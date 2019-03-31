package javanet.c01;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 1.	编写一个Java程序，该程序首先将文本文件中所有的行读入List，
 * 然后从List中找出包含“test”的行并将其打印出来；
 */
class Exercise1_1 {

    void exercise1_1() throws Exception {

        FileInputStream fis = new FileInputStream("files/c01/test.txt");
        String line;
        ArrayList<String> list = new ArrayList<>();

        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        while ((line = br.readLine()) != null) {
            list.add(line);
        }

        for (String item : list) {
            if (item.contains("test")) {
                System.out.println(item);
            }
        }

        fis.close();

    }

    public static void main(String[] args) throws Exception {
        new Exercise1_1().exercise1_1();
    }
}
