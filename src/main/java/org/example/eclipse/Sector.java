package org.example.eclipse;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sector extends BetterPolygon {
    static List<Double> points = List.of(-10.0, 4.0, -9.0, 4.0, -8.5, 4.87, -9.0, 5.73, -10.0, 5.73, -10.5, 4.87);
    static double RADIUS = 60;
    boolean isHidden = true;
    List<Side> sides = new ArrayList<>();
    List<Sector> neighbours = new ArrayList<>();
    List<Spaceship> spaceships = new ArrayList<>();

    public Sector(double centerX, double centerY) {
        super(points);
        scale(RADIUS / getRadius());
        moveTo(centerX, centerY);
        setFill(Color.TRANSPARENT);
        setStroke(Color.BLACK);
        generateSides();
        checkForNeighbours();
    }

    private void generateSides() {
        for (int i = 0; i < getPoints().size(); i += 2) {
            Side side;
            if (i < getPoints().size() - 2) {
                side = new Side(getPoints().get(i), getPoints().get(i + 1), getPoints().get(i + 2), getPoints().get(i + 3));
            } else {
                side = new Side(getPoints().get(i), getPoints().get(i + 1), getPoints().get(0), getPoints().get(1));
            }
            sides.add(side);
        }
        Random random = new Random();
        int wormholeNumber = random.nextInt(1, 6);
        while(wormholeNumber > 0){
            int index = random.nextInt(sides.size());
            if(!sides.get(index).hasWormhole){
                sides.get(index).addWormhole();
                wormholeNumber--;
            }
        }
    }

    private void checkForNeighbours(){
        for(Side side : sides){
            double sideMidX = (side.getStartX() + side.getEndX()) / 2;
            double sideMidY = (side.getStartY() + side.getEndY()) / 2;
            double xDiff = sideMidX - getCenterX();
            double yDiff = sideMidY - getCenterY();
            double centerX = getCenterX() + 2 * xDiff;
            double centerY = getCenterY() + 2 * yDiff;
            for(Sector sector : Main.sectors){
                if(Math.round(sector.getCenterX())  == Math.round(centerX) && Math.round(sector.getCenterY()) == Math.round(centerY)){
                    if(!sector.neighbours.contains(this)) sector.neighbours.add(this);
                    if(!neighbours.contains(sector)) neighbours.add(sector);
                }
            }
        }
    }

    public void show() {
        Main.root.getChildren().add(this);
        isHidden = false;
        for (Side side : sides) {
            if (side.hasWormhole) side.showWormhole();
        }
    }

    public void generateNeighbours(){
        for(Side side : sides){
            double sideMidX = (side.getStartX() + side.getEndX()) / 2;
            double sideMidY = (side.getStartY() + side.getEndY()) / 2;
            double xDiff = sideMidX - getCenterX();
            double yDiff = sideMidY - getCenterY();
            double centerX = getCenterX() + 2 * xDiff;
            double centerY = getCenterY() + 2 * yDiff;
            boolean addNeighbour = true;
            for(Sector sector : Main.sectors){
                if(Math.round(sector.getCenterX())  == Math.round(centerX) && Math.round(sector.getCenterY()) == Math.round(centerY)){
                    addNeighbour = false;
                    break;
                }
            }
            if(addNeighbour){
                Sector sector = new Sector(centerX, centerY);
                if(!Main.sectors.contains(sector)) Main.sectors.add(sector);
                if(!neighbours.contains(sector)) neighbours.add(sector);
            }
        }
    }

    public void spin(double angle){
        rotate(angle);
        List<Integer> wormholeSides = new ArrayList<>();
        for(Side side : sides){
            if(side.hasWormhole){
                wormholeSides.add(sides.indexOf(side));
                Main.root.getChildren().remove(side.wormhole);
            }
        }
        sides.clear();
        for (int i = 0; i < getPoints().size(); i += 2) {
            Side side;
            if (i < getPoints().size() - 2) {
                side = new Side(getPoints().get(i), getPoints().get(i + 1), getPoints().get(i + 2), getPoints().get(i + 3));
            } else {
                side = new Side(getPoints().get(i), getPoints().get(i + 1), getPoints().get(0), getPoints().get(1));
            }
            sides.add(side);
        }
        for (int i = 0; i < wormholeSides.size(); i++) {
            sides.get(wormholeSides.get(i)).addWormhole();
            sides.get(wormholeSides.get(i)).showWormhole();
        }
    }

    static public boolean ifConnect(Sector sector1, Sector sector2){
        if(sector1.equals(sector2)) return true;
        if(sector1.neighbours.contains(sector2)){
            for(Side side1 : sector1.sides){
                for(Side side2 : sector2.sides){
                    if(Math.round(side1.getStartX()) == Math.round(side2.getStartX()) &&
                            Math.round(side1.getStartY()) == Math.round(side2.getStartY()) &&
                            Math.round(side1.getEndX()) == Math.round(side2.getEndX()) &&
                            Math.round(side1.getEndY()) == Math.round(side2.getEndY())){
                        return (side1.hasWormhole && side2.hasWormhole);
                    }
                }
            }
        }
        return false;
    }
}
