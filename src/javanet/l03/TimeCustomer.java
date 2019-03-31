package javanet.l03;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class TimeCustomer {
    public static void main(String[] args) {
        Socket socket = null;
        DataInputStream dis = null;
        boolean error = false;
        int year, month, day, hour, minute, second;
        try {
            socket = new Socket("localhost", 1314);
            dis = new DataInputStream(socket.getInputStream());
            while (!error) {
                try {
                    year = dis.readInt();
                    month = dis.readByte();
                    day = dis.readByte();
                    hour = dis.readByte();
                    minute = dis.readByte();
                    second = dis.readByte();
                    System.out.println(year + "年" + month + "月" + day + "日 " + hour + "时" + minute + "分" + second + "秒");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println("Connection Reset");
                    error = true;
                }
            }

        } catch (IOException e) {
            System.out.println("Server Unreachable");
        } finally {
            try {
                if (dis != null)
                    dis.close();
                if (socket != null)
                    socket.close();
            } catch (Exception e) {
                System.err.println("Error During Close Connect");
            }
        }
    }

}
