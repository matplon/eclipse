package org.example.eclipse;

import javafx.scene.shape.Polyline;

import java.util.List;

public class BetterPolygon extends javafx.scene.shape.Polygon {
    public BetterPolygon(List<Double> points) {
        super();
        if (points != null)
            getPoints().setAll(points);
    }

    public double getCenterX() {    // Mean average of the X coordinates
        double sum = 0;
        for (int i = 0; i < getPoints().size(); i += 2) {
            sum += getPoints().get(i);
        }
        return sum / ((double) getPoints().size() / 2);
    }

    public double getCenterY() {    // Mean average of the Y coordinates
        double sum = 0;
        for (int i = 1; i < getPoints().size(); i += 2) {
            sum += getPoints().get(i);
        }
        return sum / ((double) getPoints().size() / 2);
    }

    public double getRadius() { // Calculate radius by finding the furthest coordinate
        double maxOffset = 0;
        for (int i = 2; i < getPoints().size(); i += 2) {
            maxOffset = Math.max(maxOffset, Math.sqrt(Math.pow(getCenterX() - getPoints().get(i), 2) + Math.pow(getCenterY() - getPoints().get(i + 1), 2)));
        }
        return maxOffset;
    }

    public void scale(double scale) {
        double centerX = getCenterX();
        double centerY = getCenterY();
        for (int i = 0; i < getPoints().size(); i += 2) {
            double newX = scale * (getPoints().get(i) - centerX) + centerX;
            double newY = scale * (getPoints().get(i + 1) - centerY) + centerY;
            getPoints().set(i, newX);
            getPoints().set(i + 1, newY);
        }
    }

    public static BetterPolygon scale(BetterPolygon polygon, double scale) {
        BetterPolygon tempPolygon = new BetterPolygon(polygon.getPoints());
        tempPolygon.scale(scale);
        return tempPolygon;
    }

    public void moveTo(double newCenterX, double newCenterY) {
        // Calculate new point coordinates
        double centerX = getCenterX();
        double centerY = getCenterY();
        for (int i = 0; i < getPoints().size(); i += 2) {
            double newX = newCenterX + getPoints().get(i) - centerX;
            double newY = newCenterY + getPoints().get(i + 1) - centerY;
            getPoints().set(i, newX);
            getPoints().set(i + 1, newY);
        }
    }

    public void rotate(double angle) {
        double centerX1 = getCenterX();
        double centerY1 = getCenterY();

        // Convert angle to radians
        double radianAngle = Math.toRadians(angle);

        // Apply rotation to each point
        for (int i = 0; i < getPoints().size(); i += 2) {
            double x = getPoints().get(i);
            double y = getPoints().get(i + 1);

            // Perform rotation
            double rotatedX = centerX1 + (x - centerX1) * Math.cos(radianAngle) - (y - centerY1) * Math.sin(radianAngle);
            double rotatedY = centerY1 + (x - centerX1) * Math.sin(radianAngle) + (y - centerY1) * Math.cos(radianAngle);

            // Update the coordinates
            getPoints().set(i, rotatedX);
            getPoints().set(i + 1, rotatedY);
        }
    }

    public void rotate(double angle, double pivotX, double pivotY) {
        // Convert angle to radians
        double radianAngle = Math.toRadians(angle);

        // Apply rotation to each point
        for (int i = 0; i < getPoints().size(); i += 2) {
            double x = getPoints().get(i);
            double y = getPoints().get(i + 1);

            // Perform rotation
            double rotatedX = pivotX + (x - pivotX) * Math.cos(radianAngle) - (y - pivotY) * Math.sin(radianAngle);
            double rotatedY = pivotY + (x - pivotX) * Math.sin(radianAngle) + (y - pivotY) * Math.cos(radianAngle);

            // Update the coordinates
            getPoints().set(i, rotatedX);
            getPoints().set(i + 1, rotatedY);
        }
    }

    public static BetterPolygon rotate(BetterPolygon polygon, double angle) {
        polygon.rotate(angle);
        return polygon;
    }

    public static Polyline scalePolyline(Polyline polyline, double scale){
        BetterPolygon polygon = new BetterPolygon(polyline.getPoints());
        polygon.scale(scale);
        Polyline polyline1 = new Polyline();
        polyline1.getPoints().addAll(polygon.getPoints());
        return polyline1;
    }
}
