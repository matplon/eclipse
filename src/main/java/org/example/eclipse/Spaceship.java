package org.example.eclipse;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class Spaceship extends ImageView {
    static String filepath = "spaceship.png";
    int player;
    Sector sector;
    public Spaceship(double centerX, double centerY, int player, Sector sector){
        super(new Image("file:"+filepath));
        setX(centerX);
        setY(centerY);
        setFitWidth(40);
        setFitHeight(40);
        this.player = player;
        this.sector = sector;
    }

}
