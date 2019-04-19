package javanet.c05;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/**
 *
 */

public class Exercise2_1 {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1314);
        System.out.println("Waiting For Connect");
        new ServerThread(server).run();//使用run让主线程也工作
    }
}

/**
 * 服务器线程类
 */
class ServerThread extends Thread {

    ServerSocket server;

    public ServerThread(ServerSocket server) {
        this.server = server;
    }

    @Override
    public void run() {
        OutputStream writer = null;
        BufferedReader reader = null;
        Socket socket = null;
        try {
            socket = server.accept();
            new ServerThread(this.server).start();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = socket.getOutputStream();

            //获取请求头
            String header = getHeader(reader);
            //获取资源地址
            String path = getPath(header);
            System.out.println(header);
            boolean exist = new File("files/c05/" + path).exists();
            int status = 404;
            String statusText = "Not Found";

            if (exist) {//若文件存在，修改状态码
                status = 200;
                statusText = "OK";
            }

            writer.write("HTTP/1.1 ".getBytes());
            writer.write((status + " ").getBytes());
            writer.write((statusText + "\r\n").getBytes());
            writer.write("Content-Type: text/html; charset=UTF-8\r\n\r\n".getBytes());

            //输出
            if (exist)
                outputContent("files/c05/" + path, writer);
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                if (reader != null)
                    reader.close();
                if (writer != null)
                    writer.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String getHeader(@NotNull BufferedReader reader) throws IOException {
        StringBuilder request = new StringBuilder();
        String line;
        if ((line = reader.readLine()) != null) {
            request.append(line);
        }
        return request.toString();
    }

    private String getPath(@NotNull String header) {
        int start = header.indexOf(" ");
        int end = header.indexOf(" ", start + 1);

        String path;

        try {
            path = header.substring(start, end);
            path = new URL("http://" + path).getPath();
            if (path.equals("/"))
                path = "index.html";
        } catch (Exception e) {
            e.printStackTrace();
            path = "index.html";
        }

        return path;
    }

    private void outputContent(@NotNull String path, OutputStream os) throws IOException {

        FileInputStream fis = new FileInputStream(path);
        byte[] buff = new byte[1024];//最大10m
        int length;
        while ((length = fis.read(buff)) != -1) {
            os.write(buff, 0, length);
        }
        fis.close();
    }

}
