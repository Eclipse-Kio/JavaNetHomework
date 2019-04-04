package javanet.e04;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ComputeServer {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1314);
        Socket socket = server.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;
        line = in.readLine();
        System.out.println(line);
        if (line != null) {
            int dataInfoStart = line.indexOf('?');
            int dataInfoEnd = line.lastIndexOf(' ');
            String data = line.substring(dataInfoStart, dataInfoEnd);
            dataInfoStart = data.indexOf("a=");
            dataInfoEnd = data.indexOf("&", dataInfoStart);

            if (dataInfoEnd == -1) dataInfoEnd = data.length();
            int a = Integer.parseInt(data.substring(dataInfoStart + 2, dataInfoEnd));
            dataInfoStart = data.indexOf("b=");
            dataInfoEnd = data.indexOf("&", dataInfoStart);
            if (dataInfoEnd == -1) dataInfoEnd = data.length();
            int b = Integer.parseInt(data.substring(dataInfoStart + 2, dataInfoEnd));
            int result = a * b;
            String response = "HTTP/1.1 200 OK\n" +
                    "Date: Fri, 22 May 2009 06:07:21 GMT\n" +
                    "Content-Type: text/html; charset=UTF-8" +
                    "<h1>" + a + "*" + b + "=" + result + "<h1>";
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println(response);
            writer.flush();
        }
    }
}
