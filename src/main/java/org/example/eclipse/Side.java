package org.example.eclipse;

import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;

public class Side extends Line {
    static double RADIUS = 7;
    static Color COLOR = Color.BLUE;
    boolean hasWormhole = false;
    Arc wormhole;

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
        wormhole = new Arc();
        wormhole.setType(ArcType.ROUND);
        wormhole.setCenterX(x);
        wormhole.setCenterY(y);
        wormhole.setRadiusX(RADIUS);
        wormhole.setRadiusY(RADIUS);
        wormhole.setStartAngle(180-Math.toDegrees(Math.atan2(getEndY() - getStartY(), getEndX() - getStartX())));
        wormhole.setLength(180);
        wormhole.setFill(COLOR);
    }
}
