package javanet.e04;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ComputeServer extends Thread {
    private ServerSocket socket;

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1314);
        new ComputeServer(server).start();
    }

    private ComputeServer(ServerSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
        String result;
        Socket request = null;
        try {
            request = socket.accept();
            new ComputeServer(socket).start();
            System.out.println("accepted " + request.getInetAddress());
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line = reader.readLine();

            int start = line.indexOf("a=");
            int end = line.indexOf("&", start);
            if (end == -1)
                end = line.indexOf(" ", start);
            int a = Integer.parseInt(line.substring(start + 2, end));
            System.out.println("a: " + a);

            start = line.indexOf("b=");
            end = line.indexOf("&", start);
            if (end == -1)
                end = line.indexOf(" ", start);
            int b = Integer.parseInt(line.substring(start + 2, end));
            System.out.println("b :" + b);


            result = "HTTP/1.1 200 OK\n" +
                    "Content-Type: text/html; charset=UTF-8\r\n\r\n" +
                    "<h1>" + a + "*" + b + "=" + a * b + "</h1>";
        } catch (Exception e) {
            e.printStackTrace();
            result = "HTTP/1.1 500 ERROR\n" +
                    "Content-Type: text/html; charset=UTF-8\r\n\r\n" +
                    "<h1>参数有误</h1>";
        }
        if (request != null) {
            try {
                PrintWriter response = new PrintWriter(new OutputStreamWriter(request.getOutputStream()));
                response.println(result);
                response.close();
                request.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
