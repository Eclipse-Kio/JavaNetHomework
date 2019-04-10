package javanet.e05;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 设计一个简单的对称加密算法，并利用该算法对一篇短文进行加密解密
 */
public class Exercise1 {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.println("Choose Operation " +
                "\n 1:key-gen 2:encryption 3:decrypt 4:exit");
        String option = input.nextLine();
        switch (option) {
            case "1":
                keyGen(input);
                main(args);
                break;
            case "2":
                cryption(input, true);
                main(args);
                break;
            case "3":
                cryption(input, false);
                main(args);
                break;
            case "4":
                input.close();
                break;
            default:
                System.out.println("No Command Matched");
                main(args);
        }

    }

    private static void cryption(Scanner input, boolean isEncryption) throws IOException {
        System.out.println("Where the key file is ?");
        String keyPath = input.nextLine();
        File keyFile = new File(keyPath);
        if (!keyFile.exists()) {
            System.out.println("No Such File!");
            return;
        }
        if (isEncryption)
            System.out.println("Where the file to be encrypted is ?");
        else
            System.out.println("Where the file to be decrypted is ?");
        String filePath = input.nextLine();
        File fileFile = new File(filePath);
        if (!fileFile.exists()) {
            System.out.println("No Such File!");
            return;
        }

        FileInputStream keyFis = new FileInputStream(keyFile);
        FileInputStream fileFis = new FileInputStream(fileFile);

        ArrayList<Integer> key = new ArrayList<>();
        int keyContent;

        while ((keyContent = keyFis.read()) != -1) {
            key.add(keyContent);
        }

        String subPath;
        if (isEncryption)
            subPath = "/encrypted";
        else
            subPath = "/decrypted";

        FileOutputStream fos = new FileOutputStream(fileFile.getParentFile() + subPath);
        int fileContent;
        int index = 0;
        while ((fileContent = fileFis.read()) != -1) {
            fos.write(fileContent ^ key.get(index % key.size()));
        }
        fos.close();
        keyFis.close();
        fileFis.close();
    }

    private static void keyGen(Scanner input) throws IOException {
        System.out.println("Where should the key file to be saved ?");
        String savePath = input.nextLine();
        StringBuilder keyBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            keyBuilder.append(java.util.UUID.randomUUID());
        }
        String data = keyBuilder.toString();
        File file = new File(savePath);
        if (!file.getParentFile().exists()) {
            System.out.println("No Such Directory!");
            return;
        }
        System.out.println(file.getAbsolutePath());
        FileWriter writer = new FileWriter(file);
        writer.write(data);
        writer.close();
    }
}
