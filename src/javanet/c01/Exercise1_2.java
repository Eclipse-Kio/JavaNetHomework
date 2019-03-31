package javanet.c01;

import java.io.File;
import java.io.FileWriter;

/**
 * 2.	编写一个Java程序，该程序首先生成10个随机数（数值范围为1-100），并将其以“Number n: r”的形式
 * （文件共10行，每个随机数1行，n、r分别代表行号与数值）
 * 存储到一个新的文件random.txt当中；
 */
class Exercise1_2 {

    void exercise1_2() throws Exception {
        int[] data = new int[10];

        File file = new File("files/c01/random.txt");

        FileWriter fileWriter = new FileWriter(file, false);

        StringBuilder text = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            data[i] = (int) (99 * Math.random() + 1);
            text.append("Number ").append(i).append(": ").append(data[i]).append(System.getProperty("line.separator"));
        }

        fileWriter.write(text.toString());
        fileWriter.flush();
        fileWriter.close();
    }

    public static void main(String[] args) throws Exception {
        new Exercise1_2().exercise1_2();
    }
}
