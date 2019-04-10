package javanet.l05.entity;

public class Bus extends Thread {
    private int code;
    private Line stops;
    private double currentPosition;
    private double speed = 0.2;
    private boolean flag;

    public Bus(Line stops) {
        this.code = stops.getCode();
        this.stops = stops;
        this.currentPosition = 0;
    }

    @Override
    public void run() {
        //模拟公交车运行
        while (flag) {
            synchronized (this) {
                currentPosition += speed;
            }
            if (currentPosition < 0) {
                currentPosition = 0;
                speed = -speed;
            }

            if (currentPosition >= stops.size()) {
                currentPosition = stops.size() - 1;
                speed = -speed;
            }
            //TODO 向服务器报告
            try {
                Thread.sleep(100);
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

}
