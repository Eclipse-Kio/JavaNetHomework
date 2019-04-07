package javanet.c04;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javanet.c04.entity.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.*;


public class Exercise3C extends Application {

    private String JSessionID = java.util.UUID.randomUUID().toString();
    private TextArea container;
    private int currentQuestionId;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        VBox box = new VBox();
        box.setSpacing(20);
        box.setPadding(new Insets(10, 10, 10, 10));
        TextArea container = new TextArea();
        container.setFont(Font.font(20));
        this.container = container;
        container.setEditable(false);
        nextHandler.handle(null);
        box.getChildren().add(container);

        HBox operationBoard = new HBox();
        operationBoard.setSpacing(20);
        box.getChildren().add(operationBoard);

        Button choiceA = new Button("A");
        Button choiceB = new Button("B");
        Button choiceC = new Button("C");
        Button choiceD = new Button("D");
        operationBoard.getChildren().add(choiceA);
        operationBoard.getChildren().add(choiceB);
        operationBoard.getChildren().add(choiceC);
        operationBoard.getChildren().add(choiceD);
        choiceA.setOnAction(choiceHandler);
        choiceB.setOnAction(choiceHandler);
        choiceC.setOnAction(choiceHandler);
        choiceD.setOnAction(choiceHandler);

        Button next = new Button("下一题");
        Button showRecord = new Button("统计");
        showRecord.setOnAction(recordHandler);

        next.setOnAction(nextHandler);
        operationBoard.getChildren().add(next);
        operationBoard.getChildren().add(showRecord);

        primaryStage.setScene(new Scene(box));
        primaryStage.setTitle("在线答题系统");
        primaryStage.show();
    }

    private EventHandler<ActionEvent> nextHandler = event -> {
        Request request = new Request(JSessionID, 0, (byte) 0, (byte) 0);
        String requestData = request.toJsonString();
        DatagramSocket socket = null;
        try {
            DatagramPacket requestPacket = new DatagramPacket(requestData.getBytes(),
                    requestData.length(),
                    InetAddress.getByName("localhost"),
                    1314);
            socket = new DatagramSocket();
            socket.send(requestPacket);
            byte[] received = new byte[1024];
            requestPacket.setData(received);
            socket.receive(requestPacket);
            JSONObject response = new JSONObject(new String(received));
            JSONArray choices = response.getJSONArray("choices");
            System.out.println(response);
            this.container.setText(response.getString("questionContent") +
                    "(" + response.getInt("score") + "分)" + "\n\n\n"
                    + "A:" + choices.get(0) + "\n"
                    + "B:" + choices.get(1) + "\n"
                    + "C:" + choices.get(2) + "\n"
                    + "D:" + choices.get(3) + "\n"
            );
            this.currentQuestionId = response.getInt("id");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null)
                socket.close();
        }
    };

    private EventHandler<ActionEvent> recordHandler = event -> {
        Request request = new Request(JSessionID, 1, (byte) 0, (byte) 0);
        String requestData = request.toJsonString();
        DatagramSocket socket = null;
        try {
            DatagramPacket requestPacket = new DatagramPacket(requestData.getBytes(),
                    requestData.length(),
                    InetAddress.getByName("localhost"),
                    1314);
            socket = new DatagramSocket();
            socket.send(requestPacket);
            byte[] received = new byte[1024];
            requestPacket.setData(received);
            socket.receive(requestPacket);
            JSONObject response = new JSONObject(new String(received));
            System.out.println(response);
            this.container.setText("总答题数:" + response.getInt("totalNum") + "\n"
                    + "正确题数:" + response.getInt("correctNum") + "\n"
                    + "总分值  :" + response.getInt("totalScore") + "\n"
                    + "已得分值:" + response.getInt("userScore") + "\n"
            );
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null)
                socket.close();
        }
    };

    private EventHandler<ActionEvent> choiceHandler = event -> {
        String label = ((Button) event.getSource()).getText();
        int answerIndex;
        switch (label) {
            case "A":
                answerIndex = 0;
                break;
            case "B":
                answerIndex = 1;
                break;
            case "C":
                answerIndex = 2;
                break;
            case "D":
                answerIndex = 3;
                break;
            default:
                return;
        }
        Request request = new Request(JSessionID, 2, (byte) currentQuestionId, (byte) answerIndex);
        String requestData = request.toJsonString();
        DatagramSocket socket;
        try {
            DatagramPacket requestPacket = new DatagramPacket(requestData.getBytes(),
                    requestData.length(),
                    InetAddress.getByName("localhost"),
                    1314);
            socket = new DatagramSocket();
            socket.send(requestPacket);
            socket.close();
            //自动下一题
            nextHandler.handle(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };
}
