package javanet.c04;

import java.net.*;

public class Exercise2_3_C {
    private int x, y;
    private boolean signal = false;

    public static void main(String[] args) throws SocketException, UnknownHostException {
        new Exercise2_3_C(0, 0).run();
    }

    private Exercise2_3_C(int x, int y) {
        this.x = x;
        this.y = y;
        new Thread(() -> {
            while (true) {
                if (this.signal) {
                    System.out.println("Go Back");
                    goBack();
                } else {
                    goOut();
                }
                System.out.println(this.x + "/" + this.y);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //公交车往回走
    private void goBack() {
        if (this.x > 0)
            this.x -= (int) (Math.random() * 20);
        if (this.x < 0)
            this.x += (int) (Math.random() * 20);
        if (this.y > 0)
            this.y -= (int) (Math.random() * 20);
        if (this.y < 0)
            this.y += (int) (Math.random() * 20);
    }

    //公交车闲逛
    private void goOut() {
        this.x += (int) (Math.random() * 80) - 40;
        this.y += (int) (Math.random() * 80) - 40;
    }

    private void run() throws SocketException, UnknownHostException {
        byte[] request = new byte[8];
        DatagramPacket requestPacket = new DatagramPacket(request, 8, InetAddress.getByName("localhost"), 1314);
        DatagramSocket socket = new DatagramSocket();
        while (true) {
            try {
                request[2] = (byte) ((x >> 8) & 0xff);
                request[0] = (byte) ((x >> 24) & 0xff);
                request[1] = (byte) ((x >> 16) & 0xff);
                request[3] = (byte) (x & 0xff);
                request[4] = (byte) ((y >> 24) & 0xff);
                request[5] = (byte) ((y >> 16) & 0xff);
                request[6] = (byte) ((y >> 8) & 0xff);
                request[7] = (byte) (y & 0xff);
                System.out.println("send");
                socket.send(requestPacket);
                byte[] response = new byte[1];
                DatagramPacket responsePacket = new DatagramPacket(response, 1);
                socket.receive(responsePacket);
                System.out.println("received:" + response[0]);
                this.signal = response[0] == 1;
                Thread.sleep(1000);
            } catch (Exception e) {
                break;
            }

        }
    }
}
