package javanet.c06;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Base64;

public class Exercise2_1_A extends Application {
    private Socket socket;
    private InputStream responseStream;
    private OutputStream requestStream;
    private String publicKey;
    private TextArea lb_get_public_key;
    private TextArea lb_gen_password;
    private TextField tf_file_path;
    private TextArea lb_get_file;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        responseStream.close();
        requestStream.close();
        socket.close();
    }

    @Override
    public void start(Stage primaryStage) {
        //连接服务器
        VBox promptRoot = new VBox();
        TextField tf_ip = new TextField();
        tf_ip.setText("localhost");
        tf_ip.selectAll();
        Button closePrompt = new Button("确定");
        promptRoot.getChildren().addAll(new Label("请输入服务器IP地址"), tf_ip, closePrompt);
        Scene prompt = new Scene(promptRoot, 500, 500);
        primaryStage.setScene(prompt);
        primaryStage.show();
        closePrompt.setOnAction((e) -> {
            try {
                String ip = tf_ip.getText();
                socket = new Socket(ip, 1314);
                responseStream = socket.getInputStream();
                requestStream = socket.getOutputStream();

                VBox vBox = new VBox();
                vBox.setPadding(new Insets(20, 20, 20, 20));
                vBox.setSpacing(30);
                Scene scene = new Scene(vBox, 1000, 800);
                primaryStage.setScene(scene);
                Button btn_get_public_key = new Button("获取公钥");
                vBox.getChildren().add(btn_get_public_key);
                btn_get_public_key.setOnAction(ef -> getPublicKey());
                lb_get_public_key = new TextArea();
                lb_get_public_key.setWrapText(true);
                lb_get_public_key.setEditable(false);
                lb_get_public_key.setMaxHeight(400);
                lb_get_public_key.setFont(Font.font(20));
                vBox.getChildren().add(lb_get_public_key);
                Label lb_file_path = new Label("文件路径");
                lb_file_path.setFont(Font.font(14));
                tf_file_path = new TextField();
                Button btn_get_file = new Button("立即获取文件");
                lb_get_file = new TextArea();
                lb_get_file.setEditable(false);
                vBox.getChildren().addAll(lb_file_path, tf_file_path, btn_get_file, lb_get_file);
                btn_get_file.setOnAction((e3) -> {
                    try {
                        getFile();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });
    }

    private void getFile() {
        new Thread(() -> {
            String msg = System.currentTimeMillis() + "##正在生成密码";
            appendLbGetFile(msg);
            String password = java.util.UUID.randomUUID().toString();
            msg = System.currentTimeMillis() + "##密码是:" + password;
            appendLbGetFile(msg);
            msg = System.currentTimeMillis() + "##正在生成请求";
            appendLbGetFile(msg);
            JSONObject request = new JSONObject();
            request.put("type", 2);
            request.put("path", tf_file_path.getText());
            String fileName = new File(tf_file_path.getText()).getName();
            request.put("password", password);
            msg = System.currentTimeMillis() + "##请求内容：" + request.toString();
            appendLbGetFile(msg);
            msg = System.currentTimeMillis() + "##正在加密密码";
            appendLbGetFile(msg);
            String encryptedPassword;
            try {
                encryptedPassword =
                        Exercise1_3.encryptByPublicKey(password, publicKey);
                request.put("password", encryptedPassword);
            } catch (Exception e) {
                msg = System.currentTimeMillis() + "##加密失败！！！！！" + e.getMessage();
                appendLbGetFile(msg);
                return;
            }
            msg = System.currentTimeMillis() + "##正在发送请求";
            appendLbGetFile(msg);
            PrintWriter out = new PrintWriter(requestStream);
            out.println(request.toString());
            out.flush();
            JSONObject response = null;
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(responseStream));
            try {
                response = new JSONObject(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response == null)
                return;

            if (!response.getBoolean("success")) {
                msg = System.currentTimeMillis() + "##" + response.getString("msg");
                appendLbGetFile(msg);
            } else {
                msg = System.currentTimeMillis() + "##正在解密文件";
                appendLbGetFile(msg);
                //对msg进行解密
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] fileByte = Exercise2_1_B.encrypt(decoder.decode(
                        response.getString("file")), password.getBytes());
                byte[] mdByte = Exercise2_1_B.encrypt(decoder.decode(
                        response.getString("MD")), password.getBytes());
                msg = System.currentTimeMillis() + "##正在校验文件";
                appendLbGetFile(msg);
                MessageDigest messageDigest;
                try {
                    messageDigest = MessageDigest.getInstance("MD5");
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                messageDigest.update(fileByte);
                byte[] verifyMdByte = messageDigest.digest();

                boolean verifySuccess = true;

                for (int i = 0; i < verifyMdByte.length; i++)
                    if (verifyMdByte[i] != mdByte[i]) {
                        verifySuccess = false;
                        break;
                    }
                if (!verifySuccess) {
                    msg = System.currentTimeMillis() + "##校验失败！";
                    appendLbGetFile(msg);
                    return;
                }

                msg = System.currentTimeMillis() + "##校验成功，开始将文件写入本地";
                appendLbGetFile(msg);
                FileOutputStream fis;
                try {
                    fis = new FileOutputStream("C:\\Users\\Kio\\Desktop\\" + fileName);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
                try {
                    fis.write(fileByte);
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    msg = System.currentTimeMillis() + "##存储时遇到问题";
                    appendLbGetFile(msg);

                }
                msg = System.currentTimeMillis() + "##您的文件保存在C:/Users/Kio/Desktop/" + fileName;
                appendLbGetFile(msg);
            }
        }).start();


    }

    private void appendLbGetFile(String msg) {
        Platform.runLater(() ->
                this.lb_get_file.setText(
                        this.lb_get_file.getText() + "\n" + msg));
    }

    private void getPublicKey() {
        new Thread(() -> {
            JSONObject request = new JSONObject();
            request.put("type", 1);
            PrintWriter writer = new PrintWriter(requestStream);
            writer.println(request);
            writer.flush();
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(responseStream));
            try {
                this.publicKey = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() ->
                    this.lb_get_public_key.setText("获取到的公钥为：\n" + this.publicKey));
        }).start();
    }
}
