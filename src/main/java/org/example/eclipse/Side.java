package org.example.eclipse;

import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Side extends Line {
    static double RADIUS = 7;
    static Color COLOR = Color.BLUE;
    boolean hasWormhole = false;
    Circle wormhole;
    Arc wormholeV2;

    public Side(double startX, double startY, double endX, double endY){
        super(startX, startY ,endX, endY);
    }

    public void showWormhole(){
        //Main.root.getChildren().add(wormhole);
        Main.root.getChildren().add(wormholeV2);
    }

    public void addWormhole(){
        hasWormhole = true;
        double x = (getStartX() + getEndX()) / 2;
        double y = (getStartY() + getEndY()) / 2;
        wormholeV2 = new Arc();
        wormholeV2.setType(ArcType.ROUND);
        wormholeV2.setCenterX(x);
        wormholeV2.setCenterY(y);
        wormholeV2.setRadiusX(RADIUS);
        wormholeV2.setRadiusY(RADIUS);
        wormholeV2.setStartAngle(180-Math.toDegrees(Math.atan2(getEndY() - getStartY(), getEndX() - getStartX())));
        System.out.println(Math.toDegrees(Math.atan((getEndX() - getStartX())/(getEndY() - getStartY()))));
        wormholeV2.setLength(180);
        wormholeV2.setFill(COLOR);
        //wormhole = new Circle(x, y, RADIUS, COLOR);
    }
}
