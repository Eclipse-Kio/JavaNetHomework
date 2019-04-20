package javanet.c06;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

/**
 * 编写一个Java程序，该程序能够利用非对称加密算法RSA对一段短文进行加密解密。
 */
public class Exercise1_3 {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        KeyPair pair = genKey(512);
        System.out.println("我们为您生成了密钥(Base64编码),您可以使用我们提供的，也可以使用已有的");
        String publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
        System.out.println("公钥:" + publicKey);
        System.out.println("私钥:" + privateKey);
        System.out.println("现在，您可以尝试输入公钥来进行加密，再使用私钥进行解密");
        System.out.println("输入公钥：");
        String inputPublicKey = input.nextLine();
        System.out.println("输入需要加密的内容：");
        String content = input.nextLine();
        System.out.println("您的内容加密后(Base64编码)：");
        String encryptByPublicKey = encryptByPublicKey(content, inputPublicKey);
        System.out.println(encryptByPublicKey);
        System.out.println("现在，您可以尝试使用私钥来进行解密：");
        String inputPrivateKey = input.nextLine();
        String decryptByPrivateKey = decryptByPrivateKey(encryptByPublicKey, inputPrivateKey);
        System.out.println("您得到了明文!");
        System.out.println(decryptByPrivateKey);
    }

    /**
     * 使用私钥进行解密
     *
     * @param encryptByPublicKey 通过公钥加密了的密文（Base64）
     * @param inputPrivateKey    私钥（Base64）
     * @return 明文
     */
    public static String decryptByPrivateKey(String encryptByPublicKey, String inputPrivateKey) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptByPublicKey);
        byte[] inputPrivateBytes = Base64.getDecoder().decode(inputPrivateKey);

        RSAPrivateKey privateKey
                = (RSAPrivateKey)KeyFactory
                .getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(inputPrivateBytes));

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        byte[] result = cipher.doFinal(encryptedBytes);
        return new String(result);
    }


    /**
     * 使用公钥加密
     *
     * @param content        需要加密的内容
     * @param inputPublicKey 公钥（Base64）
     * @return 使用公钥加密后的密文（Base64）
     */
    public static String encryptByPublicKey(String content, String inputPublicKey) throws Exception {
        byte[] contentBytes = content.getBytes();
        byte[] publicKeyBytes = Base64.getDecoder().decode(inputPublicKey);

        RSAPublicKey publicKey
                = (RSAPublicKey) KeyFactory
                .getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(contentBytes);
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 生成密钥
     *
     * @return keyPair
     * @throws Exception 找不到响应加密算法时抛出异常
     */
    public static KeyPair genKey(int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize, new SecureRandom());//生成128位的公钥与私钥
        return keyPairGenerator.generateKeyPair();
    }
}
