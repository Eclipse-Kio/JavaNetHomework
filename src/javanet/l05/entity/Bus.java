package javanet.l05.entity;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 公交车类，模拟公交车的运营，定时向服务器报告自己的位置
 */
public class Bus extends Thread {
    private String id = java.util.UUID.randomUUID().toString();
    private int code;
    private Line stops;
    private double currentPosition;
    private double speed = 0.01;
    private boolean flag = true;

    public Bus(Line stops) {
        this.code = stops.getCode();
        this.stops = stops;
        this.currentPosition = Math.random() * stops.size();//随机初始位置
    }

    @Override
    public void run() {
        //模拟公交车运行
        while (flag) {
                currentPosition += speed * Math.random();//模拟公交车非匀速运动
            if (currentPosition < 0) {
                currentPosition = 0;
                speed = -speed;
            }

            if (currentPosition >= stops.size()) {
                currentPosition = stops.size() - 1;
                speed = -speed;
            }
            // 向服务器报告
            String report;
            JSONObject reportObject = new JSONObject();
            reportObject.put("id", id);
            reportObject.put("code", code);
            reportObject.put("position", currentPosition);
            report = reportObject.toString();
            try {
                DatagramPacket packet = new DatagramPacket(report.getBytes(),
                        report.getBytes().length,
                        InetAddress.getByName("localhost"),
                        1315);
                DatagramSocket socket = new DatagramSocket();
                socket.send(packet);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public int getCode() {
        return code;
    }

    public Line getStops() {
        return stops;
    }

    public double getCurrentPosition() {
        return currentPosition;
    }

    public String getCurrentStop() {
        return stops.getStops().get((int) currentPosition);
    }

    public void kill() {
        this.flag = false;
    }

}
