package javanet.c03;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class Exercise3_1_Customer extends Application implements EventHandler<ActionEvent> {

    private TextArea input;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ui(primaryStage);
    }

    private void ui(Stage primaryStage) {
        VBox root = new VBox();

        root.setPadding(new Insets(20, 20, 20, 20));
        Label label = new Label("请输入数据，以逗号分隔，例如 \"1,2,3,4\"\n");
        label.setFont(new Font("Serial", 16));
        input = new TextArea();


        Button send = new Button("计算");

        root.getChildren().addAll(label, input, send);


        primaryStage.setTitle("实用计算器");
        primaryStage.setScene(new Scene(root, 500, 320));
        primaryStage.show();
        root.setAlignment(Pos.TOP_CENTER);
        send.setMinWidth(root.getWidth() * 0.92);
        send.setMinHeight(50);
        input.setMinSize(root.getWidth() * 0.9, root.getHeight() / 2);
        label.setMinWidth(root.getWidth()*0.9);
        root.setSpacing(20);

        send.setOnAction(this);
    }

    @Override
    public void handle(ActionEvent event) {
        try {
           Button source =  (Button)event.getSource();
           source.setFocusTraversable(false);
           input.setFocusTraversable(false);
            Socket socket = new Socket("localhost", 1314);

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            String content = input.getText();
            String[] params = content.split(",");

            for (String param:params){
                //检测是否为整数
                Integer.parseInt(param);
                writer.println(param);
            }
            writer.println("#");
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String result = reader.readLine();

            new Alert(Alert.AlertType.INFORMATION,result).showAndWait();

            source.setFocusTraversable(true);
            input.setFocusTraversable(true);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).showAndWait();
        }
    }
}
