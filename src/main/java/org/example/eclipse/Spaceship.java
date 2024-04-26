package org.example.eclipse;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.List;

public class Spaceship extends ImageView {
    static String filepath = "spaceship.png";
    static double WIDTH = 30;
    static double HEIGHT = 20;
    int player;
    Sector sector;
    public Spaceship(double centerX, double centerY, int player, Sector sector){
        super(new Image("file:"+filepath));
        moveTo(centerX, centerY);
        setFitWidth(WIDTH);
        setFitHeight(HEIGHT);
        this.player = player;
        this.sector = sector;
        setColor();
    }

    private void setColor(){
        Lighting lighting;
        if(player == 1) lighting = new Lighting(new Light.Distant(90, 90, Color.BLUE));
        else lighting = new Lighting(new Light.Distant(90, 90, Color.RED));
        ColorAdjust bright = new ColorAdjust(0, 0, 0, 0);
        lighting.setContentInput(bright);
        lighting.setSurfaceScale(0.0);
        setEffect(lighting);
    }

    public void moveTo(double newX, double newY){
        setX(newX - WIDTH/2);
        setY(newY - HEIGHT/2);
    }
}
