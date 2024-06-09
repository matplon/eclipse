package org.example.eclipse;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    static List<Integer> numOfShipsToDeploy = new ArrayList<>();
    static Phase phase = Phase.STARTING_SECTORS;
    static int player = 1;
    Sector chosenSector;
    Spaceship chosenShip;
    boolean deployingShip = false;

    @Override
    public void start(Stage stage) throws IOException {
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        stage.setScene(scene);
        stage.show();
        generateSectors();
        keyEvents();
        numOfShipsToDeploy.add(1);
        numOfShipsToDeploy.add(1);
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
                    if(sector.contains(clickPoint) && sector.player == 0 && !sector.isHidden && (sector.getCenterX() != WIDTH / 2 || sector.getCenterY() != HEIGHT/2)){
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
                            for(Spaceship spaceship : neighbour.spaceships){
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
                        chosenSector = null;
                        HUD.invalidSector();
                    }
                }
            }
            else if(mouseEvent.getButton() == MouseButton.PRIMARY && phase == Phase.SHIPS){
                HUD.priceOfBasicShip.setOnMouseClicked(event -> {
                    if (player == 1 && HUD.pointsPlayer1 >= 3) {
                        numOfShipsToDeploy.set(0, numOfShipsToDeploy.get(0) + 1);
                        HUD.pointsPlayer1 -= 3;
                    }
                    if (player == 2 && HUD.pointsPlayer2 >= 3) {
                        numOfShipsToDeploy.set(1, numOfShipsToDeploy.get(1) + 1);
                        HUD.pointsPlayer2 -= 3;
                    }
                });

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
                        System.out.println("lol");
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
                        if(spaceship.getLayoutBounds().contains(point2D) && spaceship.player == player){
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
            else if(keyEvent.getCode() == KeyCode.BACK_SPACE && phase == Phase.SECTORS && chosenSector != null){
                chosenSector.hide();
                chosenSector.reroll();
                chosenSector = null;
                if(player == 2){
                    player = 1;
                    phase = Phase.SHIPS;
                }
                else{
                    player = 2;
                }
            }
            HUD.update(player, phase);
        });
    }

    public static void startBattles() {

        int ships1 = 0;
        int ships2 = 0;
        for(Sector sector : sectors){
            sector.battle();
            ships1 += (int)(sector.spaceships.stream().filter(s -> s.player == 1).count());
            ships2 += (int)(sector.spaceships.stream().filter(s -> s.player == 2).count());
        }
        phase = Phase.SECTORS;
        player = 1;


        if (ships1 == 0 && HUD.pointsPlayer1 < 3 && numOfShipsToDeploy.get(0) == 0) {
            winnerWinnerChickenDinner(2);
        }
        else if (ships2 == 0 && HUD.pointsPlayer2 < 3 && numOfShipsToDeploy.get(1) == 0){
            winnerWinnerChickenDinner(1);
        }

    }

    private static void winnerWinnerChickenDinner(int player) {
        root.getChildren().clear();

        Text winText = new Text();
        winText.setText("WINNER WINNER CHICKEN DINNER \n CONGRATULATIONS PLAYER" + " " + player);
        if (player == 1) {
            winText.setStroke(Color.BLUE);
        }
        if (player == 2) {
            winText.setStroke(Color.RED);
        }
        Font font = Font.font("Roboto", 76);
        winText.setFont(font);
        winText.setX(300);
        winText.setY(HEIGHT/2);
        root.getChildren().add(winText);

    }

    public static void main(String[] args) {
        launch();
    }
}