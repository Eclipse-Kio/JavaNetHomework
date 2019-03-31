package javanet.c01;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
/**
 * 编写一个Java程序，该程序将random.txt中随机数值大于50的行追加至test.txt文件的尾部。
 */
public class Exercise1_3 {

    private void exercise1_3() throws Exception {
        FileInputStream fis = new FileInputStream("files/c01/random.txt");
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        // 检查是否大于50
        while ((line = br.readLine()) != null) {
            int i = Integer.parseInt(line.substring(10));
            if (i > 50)
                append(line);
        }

        fis.close();

    }

    private static void append(String line) throws Exception {
        File file = new File("files/c01/test.txt");
        FileWriter fileWriter = new FileWriter(file, true);

        line += System.getProperty("line.separator");

        fileWriter.write(line);
        fileWriter.flush();
        fileWriter.close();
    }

    public static void main(String[] args) throws Exception {
        new Exercise1_3().exercise1_3();
    }
}
