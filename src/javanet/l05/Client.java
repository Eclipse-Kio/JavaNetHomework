package javanet.l05;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Client  extends Application {
    public static void main(String[] args){
        launch();
    }
    @Override
    public void start(Stage primaryStage) {
        VBox main = new VBox();
        main.setSpacing(10);
        main.setPadding(new Insets(10,10,10,10));
        HBox operation = new HBox();
        operation.setSpacing(20);
        TextField searchText = new TextField();
        Button search = new Button("查询");
        operation.getChildren().add(searchText);
        operation.getChildren().add(search);

        main.getChildren().add(operation);
        Scene scene = new Scene(main,800,600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("公交车实时到站查询系统");
        primaryStage.show();
    }
}
