package org.example.eclipse;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    enum Phase{
        SECTORS,
        SHIPS,
        BATTLES
    }

    static double WIDTH = 1800, HEIGHT = 900;
    static AnchorPane root = new AnchorPane();
    static Scene scene = new Scene(root, WIDTH, HEIGHT);
    static List<Sector> sectors = new ArrayList<>();
    List<Spaceship> spaceships = new ArrayList<>();
    List<Integer> numOfShipsToDeploy = new ArrayList<>();
    Phase phase = Phase.SECTORS;
    int player = 1;
    Sector chosenSector;
    Spaceship chosenShip;
    boolean deployingShip = false;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setScene(scene);
        stage.show();
        generateSectors();
        keyEvents();
        numOfShipsToDeploy.add(5);
        numOfShipsToDeploy.add(5);
    }

    public void generateSectors() {
        Sector sector = new Sector(WIDTH / 2, HEIGHT / 2);
        sector.show();
        sector.generateNeighbours();
        for (Sector neighbour : sector.neighbours) {
            neighbour.generateNeighbours();
        }
        for (int i = 0; i < sector.sides.size(); i++) {
            Side side = sector.sides.get(i);
            double sideMidX = (side.getStartX() + side.getEndX()) / 2;
            double sideMidY = (side.getStartY() + side.getEndY()) / 2;
            double xDiff = sideMidX - sector.getCenterX();
            double yDiff = sideMidY - sector.getCenterY();
            double centerX = sector.getCenterX() + 4 * xDiff;
            double centerY = sector.getCenterY() + 4 * yDiff;

            List<Sector> sectorsToShow = new ArrayList<>();

            for (Sector sectorSector : sectors) {
                if (Math.round(sectorSector.getCenterX()) == Math.round(centerX) && Math.round(sectorSector.getCenterY()) == Math.round(centerY)) {
                    sectorsToShow.add(sectorSector);
                }
            }

            for (Sector sectorSector : sectorsToShow) {
                sectorSector.generateNeighbours();
                sectorSector.show();
            }
        }
    }

    public void keyEvents(){
        scene.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY && phase == Phase.SECTORS && chosenSector == null){
                double x = mouseEvent.getX(), y = mouseEvent.getY();
                Point2D point2D = new Point2D(x, y);
                for(Sector sector : sectors){
                    if(sector.contains(point2D) && sector.isHidden){
                        chosenSector = sector;
                    }
                }
                if(chosenSector != null){
                    chosenSector.show();
                    chosenSector.generateNeighbours();
                }
            }
            else if(mouseEvent.getButton() == MouseButton.PRIMARY && phase == Phase.SHIPS){
                double x = mouseEvent.getX(), y = mouseEvent.getY();
                Point2D point2D = new Point2D(x, y);
                if(chosenShip != null){
                    Sector sector = null;
                    for(Sector sectorSector : sectors){
                        if(sectorSector.contains(point2D) && !sectorSector.isHidden){
                            sector = sectorSector;
                            break;
                        }
                    }
                    if(sector != null){
                        if(Sector.ifConnect(chosenShip.sector, sector)){
                            root.getChildren().remove(chosenShip);
                            chosenShip.sector.spaceships.remove(chosenShip);
                            chosenShip.setX(x);
                            chosenShip.setY(y);
                            chosenShip.sector = sector;
                            sector.spaceships.add(chosenShip);
                            root.getChildren().add(chosenShip);
                            chosenShip = null;
                        }
                    }
                }
                else if(deployingShip){
                    Sector sector = null;
                    for(Sector sectorSector : sectors){
                        if(sectorSector.contains(point2D) && !sectorSector.isHidden){
                            sector = sectorSector;
                            break;
                        }
                    }
                    if(sector != null){
                        Spaceship ship = new Spaceship(x, y, player, sector);
                        spaceships.add(ship);
                        sector.spaceships.add(ship);
                        deployingShip = false;
                        root.getChildren().add(ship);
                        System.out.println("deployed!");
                    }
                }
                else{
                    for(Spaceship spaceship : spaceships){
                        Circle circle = new Circle(spaceship.getX(), spaceship.getY(), 10);
                        if(circle.contains(point2D)){
                            chosenShip = spaceship;
                            break;
                        }
                    }
                }
            }
        });

        scene.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.R && chosenSector != null){
                chosenSector.spin(60);
            }
            if(keyEvent.getCode() == KeyCode.ENTER && chosenSector != null){
                chosenSector = null;
                phase = Phase.SHIPS;
            }
            if(keyEvent.getCode() == KeyCode.ENTER && phase == Phase.SHIPS){
                chosenShip = null;
                phase = Phase.BATTLES;
            }
            if(keyEvent.getCode() == KeyCode.E && phase == Phase.SHIPS && chosenShip == null && numOfShipsToDeploy.get(player-1) > 0){
                System.out.println("deploying...");
                deployingShip = true;
                numOfShipsToDeploy.set(player-1, numOfShipsToDeploy.get(player-1)-1);
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}