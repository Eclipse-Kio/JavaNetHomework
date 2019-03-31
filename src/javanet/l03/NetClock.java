package javanet.l03;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;


public class NetClock extends Application implements Runnable {
    private static GraphicsContext gc;
    private int year, month, day, hour, minute, second;
    private DataInputStream dis;
    private boolean flag = true;
    private Socket socket;
    private NetClock worker;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        HBox root = new HBox();

        Scene scene = new Scene(root, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("网络时钟");

        Canvas canvas = new Canvas(500, 600);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        worker = new NetClock();
        new Thread(worker).start();
    }

    @Override
    public void run() {
        Image lock_back = new Image("file:files/l03/clock_back.png");
        try {
            socket = new Socket("localhost", 1314);
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("无法连接服务端");
        }
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font(20));
        while (gc != null && flag) {
            try {
                update();
            } catch (IOException e) {
                System.out.println("读取服务器数据失败！");
            }
            gc.clearRect(0, 0, 500, 600);
            gc.drawImage(lock_back, 10, 10, 480, 480);
            float absHour = hour % 12 + minute / 60f + second / 3600f;
            float absMinute = minute + second / 60f;
            gc.setLineWidth(14);
            gc.setLineCap(StrokeLineCap.ROUND);
            gc.setStroke(Color.BLACK);
            gc.strokeLine(250, 250, 250 + Math.cos(Math.PI * (absHour / 6f - 0.5)) * 100, 250 + Math.sin(Math.PI * (absHour / 6f - 0.5)) * 100);
            gc.setLineWidth(10);
            gc.setStroke(Color.PINK);
            gc.strokeLine(250, 250, 250 + Math.cos(Math.PI * (absMinute / 30f - 0.5)) * 120, 250 + Math.sin(Math.PI * (absMinute / 30f - 0.5)) * 120);
            gc.setLineWidth(6);
            gc.setStroke(Color.RED);
            gc.strokeLine(250, 250, 250 + Math.cos(Math.PI * (second / 30f - 0.5)) * 160, 250 + Math.sin(Math.PI * (second / 30f - 0.5)) * 160);
            gc.setFill(Color.RED);
            gc.fillOval(235, 235, 30, 30);
            gc.setFill(Color.BLACK);
            gc.fillText(year + "年" + month + "月" + day + "日 " + hour + "时" + minute + "分" + second + "秒", 250, 540);
        }
    }

    private void update() throws IOException {
        this.year = dis.readInt();
        this.month = dis.readByte();
        this.day = dis.readByte();
        this.hour = dis.readByte();
        this.minute = dis.readByte();
        this.second = dis.readByte();
    }

    private void kill() throws Exception{
        flag = false;
        if (dis != null)
            dis.close();
        if (socket != null)
            socket.close();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        worker.kill();
        System.out.println("Stopped");
    }

}
