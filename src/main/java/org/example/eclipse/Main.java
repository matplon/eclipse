package org.example.eclipse;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    enum Phase{
        STARTING_SECTORS,
        SECTORS,
        SHIPS,
        BATTLES
    }

    static double WIDTH = 1800, HEIGHT = 900;
    static AnchorPane root = new AnchorPane();
    static Scene scene = new Scene(root, WIDTH, HEIGHT);
    static List<Sector> sectors = new ArrayList<>();
    static Glow glow = new Glow(1);
    List<Spaceship> spaceships = new ArrayList<>();
    List<Integer> numOfShipsToDeploy = new ArrayList<>();
    Phase phase = Phase.STARTING_SECTORS;
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
        HUD.init();
    }

    public void generateSectors() {
        Sector sector = new Sector(WIDTH / 2, HEIGHT / 2);
        sectors.add(sector);
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
            if(phase == Phase.STARTING_SECTORS && mouseEvent.getButton() == MouseButton.PRIMARY){
                Point2D clickPoint = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                for(Sector sector : sectors){
                    if(sector.contains(clickPoint) && sector.player == 0 && (sector.getCenterX() != WIDTH / 2 || sector.getCenterY() != HEIGHT/2)){
                        sector.setPlayer(player);
                        player++;
                        break;
                    }
                }
                if(player > 2){
                    player = 1;
                    phase = Phase.SECTORS;
                    for (Sector sector : sectors){
                        if(!sector.isHidden && sector.player == 0 && (sector.getCenterX() != WIDTH / 2 || sector.getCenterY() != HEIGHT/2)){
                            sector.hide();
                        }
                    }
                }
            }
            else if(mouseEvent.getButton() == MouseButton.PRIMARY && phase == Phase.SECTORS && chosenSector == null){
                double x = mouseEvent.getX(), y = mouseEvent.getY();
                Point2D point2D = new Point2D(x, y);
                for(Sector sector : sectors){
                    if(sector.contains(point2D) && sector.isHidden){
                        chosenSector = sector;
                    }
                }
                if(chosenSector != null){
                    boolean sectorValid = false;
                    for(Sector neighbour : chosenSector.neighbours){
                        if(neighbour.player == player){
                            sectorValid = true;
                            break;
                        }
                        else{
                            for(Spaceship spaceship : chosenSector.spaceships){
                                if(spaceship.player == player){
                                    sectorValid = true;
                                    break;
                                }
                            }
                        }
                    }
                    if(sectorValid){
                        chosenSector.show();
                        chosenSector.generateNeighbours();
                    }
                    else{
                        HUD.invalidSector();
                    }
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
                        if(Sector.ifConnected(chosenShip.sector, sector)){
                            root.getChildren().remove(chosenShip);
                            chosenShip.sector.spaceships.remove(chosenShip);
                            chosenShip.moveTo(x, y);
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
                    }
                }
                else{
                    for(Spaceship spaceship : spaceships){
                        if(spaceship.getLayoutBounds().contains(point2D)){
                            chosenShip = spaceship;
                            break;
                        }
                    }
                }
            }
            HUD.update(player, phase);
        });

        scene.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.R && chosenSector != null){
                chosenSector.spin(60);
            }
            else if(keyEvent.getCode() == KeyCode.ENTER && chosenSector != null){
                chosenSector = null;
                if(player == 1){
                    player = 2;
                }
                else{
                    player = 1;
                    phase = Phase.SHIPS;
                }
            }
            else if(keyEvent.getCode() == KeyCode.ENTER && phase == Phase.SHIPS && chosenShip == null){
                if(player == 1){
                    player = 2;
                }
                else{
                    player = 1;
                    phase = Phase.BATTLES;
                }
            }
            else if(keyEvent.getCode() == KeyCode.E && phase == Phase.SHIPS && chosenShip == null && numOfShipsToDeploy.get(player-1) > 0){
                deployingShip = true;
                numOfShipsToDeploy.set(player-1, numOfShipsToDeploy.get(player-1)-1);
            }
            HUD.update(player, phase);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}