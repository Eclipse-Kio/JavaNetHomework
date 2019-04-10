package javanet.l05;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

public class Client extends Application {
    private GraphicsContext context;
    private InfoGetter getter;
    private Paint[] paints = new Paint[]{
            Color.BROWN,
            Color.SPRINGGREEN,
            Color.YELLOW,
            Color.TURQUOISE
    };

    public class InfoGetter extends Thread {
        private boolean flag = true;
        private int code = 233;//默认233路

        @Override
        public void run() {
            try {
                Socket socket;
                try {
                    socket = new Socket("localhost", 1314);
                } catch (ConnectException e) {
                    System.out.println("连接被拒绝!");
                    return;
                }
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (flag) {
                    out.println("{\"code\":" + code + "}");
                    out.flush();

                    String line;
                    try {
                        line = reader.readLine();
                    } catch (SocketException e) {
                        System.out.println("与服务端连接失败!");
                        reader.close();
                        out.close();
                        socket.close();
                        break;
                    }
                    Client.this.draw(line);
                    Thread.sleep(16);
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Unable to Reach Server!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void kill() {
            this.flag = false;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    private void draw(String data) {
        JSONObject dataObject = new JSONObject(data);
        context.setFill(Color.CADETBLUE);
        //绘制背景
        context.fillRect(0, 0, context.getCanvas().getWidth(), context.getCanvas().getHeight());
        //绘制路线条
        context.setFill(Color.BLACK);
        context.fillRect(0, 150, context.getCanvas().getWidth(), 20);
        if (dataObject.getInt("code") != 0) {
            context.fillText(dataObject.getString("msg"), context.getCanvas().getWidth() / 2, 200);
            return;
        }

        JSONArray stops = dataObject.getJSONObject("msg").getJSONArray("stops");
        double gap = (context.getCanvas().getWidth() - context.getFont().getSize()) / (stops.length()-1);
        for (int i = 0; i < stops.length(); i++) {
            StringBuilder raw = new StringBuilder(stops.getString(i));
            for (int j = 0; j < raw.toString().length(); j++) {
                raw.insert(j, "\n");
                j++;
            }
            context.fillText(raw.toString(), i * gap + context.getFont().getSize() * 0.5, 180);
        }
        JSONArray buses = dataObject.getJSONArray("busPosition");

        for (int i = 0; i < buses.length(); i++) {
            double absolutePosition = gap * buses.getDouble(i)+ context.getFont().getSize() * 0.5 - 15;
            context.setFill(paints[i % paints.length]);
            context.fillOval(absolutePosition, 145, 30, 30);
        }
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        VBox main = new VBox();
        main.setSpacing(10);
        main.setPadding(new Insets(10, 10, 10, 10));
        HBox operation = new HBox();
        operation.setSpacing(20);
        TextField searchText = new TextField();
        Button search = new Button("查询");
        operation.getChildren().add(searchText);
        operation.getChildren().add(search);

        main.getChildren().add(operation);
        Scene scene = new Scene(main, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("公交车实时到站查询系统  -233");
        primaryStage.show();
        primaryStage.setResizable(false);

        getter = new InfoGetter();
        getter.start();

        search.setOnAction(e -> {
            try {
                getter.setCode(Integer.parseInt(searchText.getText()));
                primaryStage.setTitle("公交车实时到站查询系统  -" + searchText.getText());
            } catch (Exception e1) {
                getter.setCode(233);
                primaryStage.setTitle("公交车实时到站查询系统  -233");
            }

        });

        Canvas canvas = new Canvas();
        canvas.setHeight(750);
        canvas.setWidth(1190);
        context = canvas.getGraphicsContext2D();
        context.setTextAlign(TextAlignment.CENTER);
        context.setFont(Font.font(20));
        main.getChildren().add(canvas);
    }

    @Override
    public void stop() {
        this.getter.kill();
    }
}
