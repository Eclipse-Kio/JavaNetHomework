package javanet.l01;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public class CopyFile {

    private static void copyDir(String source, String dest) {
        File file = new File(source);

        if (!file.exists()) {
            System.out.println("Source File Path Not Found!");
            return;
        }
        String[] filePaths = file.list();
        if (filePaths == null)
            return;
        for (String s : filePaths) {
            File item = new File(source + File.separator + s);
            if (item.isFile()) {
                copyFile(source + File.separator + s, dest + File.separator + s);
            } else {
                if (!new File(dest).exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    new File(dest).mkdir();
                }
                copyDir(source + File.separator + s, dest + File.separator + s);
            }
        }

    }

    private static void copyFile(String source, String dest) {
        try {

            //如果文件夹中没有该文件则进行复制
            if (!new File(dest).exists()) {
                FileInputStream fis = new FileInputStream(source);
                FileOutputStream fos = new FileOutputStream(dest);
                int b;

                while ((b = fis.read()) != -1) {
                    fos.write(b);
                }
                fos.flush();
                fis.close();
                fos.close();
            }

        } catch (Exception ignored) {

        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please Input SourcePath and DestPath");
        System.out.println("SourcePath:");
        String sp = input.nextLine();
        System.out.println("DestPath:");
        String dp = input.nextLine();

        if (new File(sp).isFile())
            copyFile(sp, dp);
        else
            copyDir(sp, dp);

        input.close();
    }
}
