package javanet.l04;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server extends Application {
    private DataGetter getter;

    @Override
    public void start(Stage primaryStage) {

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("时间");
        yAxis.setLabel("温度(℃)");
        xAxis.setAutoRanging(true);
        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);
        lineChart.setCreateSymbols(false);
        lineChart.setTitle("实时温度图");
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Average");
        lineChart.getData().add(series);

        Data data = new Data(lineChart);
        getter = new DataGetter(data);
        getter.start();

        Scene scene = new Scene(lineChart, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("实时温度监控");
    }

    @Override
    public void stop() {
        getter.kill();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

/**
 * 数据类，主要维护一个hash表，用于存储得到的数据
 * 然后提供给绘图线程
 */
class Data {
    private long timeStart;
    private LineChart<Number, Number> chart;

    Data(LineChart<Number, Number> chart) {
        this.chart = chart;
        timeStart = System.currentTimeMillis();
    }

    synchronized void addData(String listName, Byte item) {
        int index = -1;
        ObservableList<XYChart.Series<Number, Number>> seriesList = chart.getData();

        for (int i = 0; i < seriesList.size(); i++) {
            if (listName.equals(seriesList.get(i).getName())) {
                index = i;
                break;
            }
        }
        int x = (int) ((System.currentTimeMillis() - timeStart) / 1000);
        if (index != -1) {
            XYChart.Series<Number, Number> series = seriesList.get(index);
            series.getData().add(new XYChart.Data<>(x, item));
        } else {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(listName);
            series.getData().add(new XYChart.Data<>(x, item));

            Platform.runLater(() -> chart.getData().add(series));
        }
        int sum = 0;
        int num = 0;
        for (XYChart.Series<Number, Number> series : seriesList) {
            for (int i = 0; i < series.getData().size(); i++) {
                XYChart.Data<Number, Number> data = series.getData().get(i);
                if ((int) data.getXValue() == x) {
                    sum +=  (byte)data.getYValue();
                    num++;
                    break;
                }
            }
        }
        System.out.println("Sum "+sum);
        System.out.println(num);
        if (num==0)return;
        try {
            seriesList.get(0).getData().set(x, new XYChart.Data<>(x, sum / (float) num));
        } catch (IndexOutOfBoundsException e) {
            seriesList.get(0).getData().add(seriesList.get(0).getData().size(), new XYChart.Data<>(x, sum/num));
        }
    }
}

/**
 * 网络线程，用于接受数据，存入Data中
 */
class DataGetter extends Thread {
    private Data data;
    private boolean flag = true;

    DataGetter(Data data) {
        this.data = data;
    }

    @Override
    public void run() {
        byte[] receive = new byte[1];
        DatagramSocket server = null;
        try {
            server = new DatagramSocket(1314);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        DatagramPacket response = new DatagramPacket(receive, receive.length);
        while (flag) {
            try {
                if (server != null) {
                    server.receive(response);
                    data.addData(response.getAddress().toString(), receive[0]);
                } else
                    break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        if (server != null)
            server.close();
    }

    void kill() {
        this.flag = false;
    }
}