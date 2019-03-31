package javanet.c02;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javanet.c02.entity.Ball;
import javanet.c02.entity.Property;

/**
 * 利用Java FX、定时器、多线程完成一个Java动画程序，
 *  要求该程序能够随机生成10不同颜色、不同大小的圆，
 *  每个圆能够颜色渐变、直线运动以及碰撞反弹。
 */
public class Exercise3 extends Application implements Runnable {
    private GraphicsContext gc;
    private Ball[] balls;
    private boolean flag;

    @Override
    public void start(Stage primaryStage) {
        this.flag = true;
        Group root = new Group();
        Canvas canvas = new Canvas(Property.WINDOW_WIDTH, Property.WINDOW_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        balls = new Ball[Property.BALL_NUM];

        for (int i = 0; i < Property.BALL_NUM; i++) {
            balls[i] = new Ball(gc);
        }


        new Thread(this).start();



        root.getChildren().add(canvas);
        primaryStage.setTitle("多彩球球");
        primaryStage.setScene(new Scene(root, Property.WINDOW_WIDTH, Property.WINDOW_HEIGHT));
        primaryStage.show();


    }

    @Override
    public void run() {

        long start = System.currentTimeMillis();
        while (flag) {
            render();
            long end = System.currentTimeMillis();
            try {
                if(end - start <20)
                    Thread.sleep(20-(end - start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            start = System.currentTimeMillis();
        }
    }

    private void render() {
        gc.setFill(Property.BACKGROUND_COLOR);
        gc.fillRect(0, 0, Property.WINDOW_WIDTH, Property.WINDOW_HEIGHT);
        for (Ball ball : balls) {
            ball.update();
            ball.render();
        }
    }

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void stop() {
        this.kill();
    }

    private void kill() {
        this.flag = false;
    }
}

