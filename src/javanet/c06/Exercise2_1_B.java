package javanet.c06;

import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * 1.	设有2台主机A、B，请模拟以下过程：<br>
 * （1）	A主机上有公钥文件， B主机上有私钥文件；<br>
 * （2）	A主机连接B主机时首先将生成一个随机密码，并将密码通过公钥加密发送给B主机；<br>
 * （3）	B主机接收到A主机的密码之后将首先计算文件的消息摘要，然后利用接收到的密码采用对称加密算法将文件与消息摘要一并传送给A主机；<br>
 * （4）	A主机接收到文件后将文件解密并通过消息摘要验证文件是否完整无误。
 */
public class Exercise2_1_B {
    private static KeyPair keyPair;
    private static ServerSocket server;

    private static class RequestHandler extends Thread {
        @Override
        public void run() {
            try {
                Socket socket = server.accept();

                System.out.println("Accepted");
                new RequestHandler().start();
                while (true) {
                    try {
                        JSONObject request = getRequest(socket.getInputStream());
                        System.out.println(request);
                        if (request.getInt("type") == 1) {//获取公钥
                            handlerGetPublicKey(socket.getOutputStream());
                        }
                        if (request.getInt("type") == 2) {//请求文件
                            handlerFileRequest(socket.getOutputStream(), request);
                        }
                    } catch (Exception e) {
                        try {
                            if (socket != null) {
                                socket.close();
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handlerFileRequest(OutputStream outputStream, JSONObject request) {
            //用私钥解密
            String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            JSONObject result = new JSONObject();
            try {
                String password = Exercise1_3.decryptByPrivateKey(request.getString("password"), privateKey);
                File file = new File(request.getString("path"));
                if (!file.exists()) {
                    result.put("success", false);
                    result.put("msg", "找不到该文件");
                } else {
                    byte[] buff = new byte[1024];
                    FileInputStream fis = new FileInputStream(file);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream(1024 * 1024 * 50);//最大50m
                    int length;
                    while ((length = fis.read(buff)) != -1) {
                        bos.write(buff, 0, length);
                    }
                    byte[] fileBytes = bos.toByteArray();
                    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                    messageDigest.update(fileBytes);
                    byte[] fileMd5 = messageDigest.digest();
                    Base64.Encoder encoder = Base64.getEncoder();
                    result.put("success", true);
                    result.put("file", encoder.encodeToString(encrypt(fileBytes, password.getBytes())));
                    result.put("MD", encoder.encodeToString(encrypt(fileMd5, password.getBytes())));
                    fis.close();
                    bos.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
                result.put("success", false);
                result.put("msg", "解密失败，您的公钥可能有误！");
            }
            PrintWriter os = new PrintWriter(outputStream);
            os.println(result.toString());
            os.flush();
        }


        private void handlerGetPublicKey(OutputStream outputStream) {
            PrintWriter os = new PrintWriter(outputStream);
            os.println(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
            os.flush();
        }

        private JSONObject getRequest(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String request = reader.readLine();
            return new JSONObject(request);
        }
    }

    public static void main(String[] args) throws Exception {
        //开启服务
        server = new ServerSocket(1314);
        //生成公钥与私钥
        keyPair = Exercise1_3.genKey(4096);
        new RequestHandler().start();
    }

    public static byte[] encrypt(byte[] source, byte[] key) {
        byte[] dest = new byte[source.length];

        for (int i = 0; i < source.length; i++) {
            dest[i] = (byte) (source[i] ^ key[i % key.length]);
        }
        return dest;
    }
}
