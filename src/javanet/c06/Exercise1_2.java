package javanet.c06;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

/**
 * 编写一个Java程序，该程序能够利用对称加密算法AES对一段短文进行加密解密
 *
 * @author Kio
 */
public class Exercise1_2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("请设置一个密码：");
        String password = input.nextLine();
        System.out.println("请输入要加密的文本内容：");
        String content = input.nextLine();

        String encryptedContent;
        try {
            encryptedContent = encrypt(content, password);
        } catch (Exception e) {
            System.out.println("加密出错!");
            return;
        }
        System.out.println("加密后的内容：");
        System.out.println(encryptedContent);
        System.out.println("现在，您需要输入密码来进行解密：");
        String newPassword = input.nextLine();
        String decryptedContent = null;
        try {
            decryptedContent = decrypt(encryptedContent, newPassword);
        } catch (Exception e) {
            System.out.println("您输入的密码不正确!");
            return;
        }
        System.out.println("对密文解密后：");
        System.out.println(decryptedContent);
    }

    /**
     * 对一个使用了Base64编码的密文进行解密
     * @param content 使用Base64编码了的密文
     * @param password 解密密码
     * @return 密码正确时返回明文
     * @throws Exception 密码错误时抛出一个异常
     */
    private static String decrypt(String content, String password) throws Exception {
        byte[] base64Parsed = Base64.getDecoder().decode(content);//将Base64字符串转换为原始字节
        //解密器
        Cipher cipher = Cipher.getInstance("AES");
        //密钥生成器
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        //初始化密钥生成器
        keyGenerator.init(128, new SecureRandom(password.getBytes()));
        //初始化解密器
        cipher.init(Cipher.DECRYPT_MODE, keyGenerator.generateKey());
        //返回解密内容
        return new String(cipher.doFinal(base64Parsed));
    }

    /**
     * 加密文本
     *
     * @param content  文本内容
     * @param password 加密密码
     * @return 加密后的密文（使用BASE64进行转码）
     */
    private static String encrypt(String content, String password) throws Exception {
        //密钥生成器
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        //加密器
        Cipher cipher = Cipher.getInstance("AES");
        //初始化密钥生成器
        keyGenerator.init(128, new SecureRandom(password.getBytes()));
        //初始化加密器
        cipher.init(Cipher.ENCRYPT_MODE, keyGenerator.generateKey());

        byte[] result = cipher.doFinal(content.getBytes());
        return Base64.getEncoder().encodeToString(result);//返回用BASE64编码后的密文
    }
}
