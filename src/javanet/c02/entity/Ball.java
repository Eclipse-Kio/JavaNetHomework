package javanet.c02.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {
    private double radius,metaRadius,radiusSpeed;
    private double x, y, xSpeed, ySpeed;
    private GraphicsContext gc;
    private int color, startColor, endColor;
    private int colorSpeed;

    public Ball(GraphicsContext gc) {
        this.metaRadius = Math.random() * (Property.MAX_RADIUS);
        this.radius = metaRadius;
        this.radiusSpeed = Math.random() * 4;

        this.x = Math.random() * (Property.WINDOW_WIDTH - 2 * radius);
        this.y = Math.random() * (Property.WINDOW_HEIGHT - 2 * radius);
        this.xSpeed = Math.random() * 2 * Property.MAX_X_SPEED - Property.MAX_X_SPEED;
        this.ySpeed = Math.random() * 2 * Property.MAX_Y_SPEED - Property.MAX_Y_SPEED;
        this.gc = gc;

        startColor = (int) (Math.random() * 0x1000000);
        endColor = (int) (Math.random() * 0x1000000);

        colorSpeed = (int) (Math.random() * 4 + 1);

        if (endColor < startColor){
            int temp = endColor;
            endColor = startColor;
            startColor = temp;
        }
        color = startColor;
    }


    public void update() {
        this.x += xSpeed;
        this.y += ySpeed;
        color += colorSpeed;


        radius=radius+radiusSpeed;

        if(radius<metaRadius||radius>metaRadius+50){
            radiusSpeed=-radiusSpeed;
        }

        if (color < startColor || color > endColor){
            colorSpeed = -colorSpeed;
        }


        if (color > 0xffffff)
            color = 0xffffff;
        if (color < 0)
            color = 0;


        if ((x > Property.WINDOW_WIDTH - 2 * radius) || (x < 0))
            xSpeed = -xSpeed;
        if ((y > Property.WINDOW_HEIGHT - 2 * radius) || (y < 0))
            ySpeed = -ySpeed;
    }

    public void render() {

        StringBuilder colorString = new StringBuilder(Integer.toHexString(color));
        while (colorString.length() < 6) {
            colorString.insert(0, "0");
        }

        gc.setFill(Color.valueOf( colorString+"99"));
        gc.fillOval(x, y, radius * 2, radius * 2);
    }
}
