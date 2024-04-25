package org.example.eclipse;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Side extends Line {
    static double RADIUS = 7;
    static Color COLOR = Color.BLUE;
    boolean hasWormhole = false;
    Circle wormhole;

    public Side(double startX, double startY, double endX, double endY){
        super(startX, startY ,endX, endY);
    }

    public void showWormhole(){
        Main.root.getChildren().add(wormhole);
    }

    public void addWormhole(){
        hasWormhole = true;
        double x = (getStartX() + getEndX()) / 2;
        double y = (getStartY() + getEndY()) / 2;
        wormhole = new Circle(x, y, RADIUS, COLOR);
    }
}
