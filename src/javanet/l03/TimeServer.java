package javanet.l03;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

public class TimeServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1314);
            new MyServer(serverSocket).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class MyServer extends Thread {
    private ServerSocket serverSocket;

    MyServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    private static void writeTime(DataOutputStream out) throws IOException {
        Calendar current = Calendar.getInstance();
        out.writeInt(current.get(Calendar.YEAR));
        out.writeByte(current.get(Calendar.MONTH));
        out.writeByte(current.get(Calendar.DAY_OF_MONTH));
        out.writeByte(current.get(Calendar.HOUR_OF_DAY));
        out.writeByte(current.get(Calendar.MINUTE));
        out.writeByte(current.get(Calendar.SECOND));
    }

    @Override
    public void run() {
        Socket socket = null;
        DataOutputStream osw = null;
        try {
            socket = serverSocket.accept();
            new Thread(new MyServer(serverSocket)).start();
            osw = new DataOutputStream(socket.getOutputStream());
            while (socket.isConnected()) {
                writeTime(osw);
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("closing");
        } finally {
            try {
                if (osw != null)
                    osw.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                System.err.println("close failed");
            }
        }
    }
}