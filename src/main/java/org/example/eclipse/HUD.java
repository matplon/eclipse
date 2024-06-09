package org.example.eclipse;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class HUD {
    static Font font = Font.font("Roboto", 30);
    static Text currentPlayer = new Text();
    static Text currentPhase = new Text();
    static Text currentPoints = new Text();
    static Text hintText = new Text();
    static Text availableShips = new Text();
    static ImageView imageView;
    static ImageView dice;
    static Button roll = new Button("Roll");
    static int pointsPlayer1;
    static int pointsPlayer2;
    static Text priceOfBasicShip = new Text();

    public static void init(){
        currentPlayer.setText("Turn: Player 1");
        currentPlayer.setFont(font);
        currentPlayer.setStroke(Color.BLUE);
        currentPlayer.setX(Main.WIDTH - 400);
        currentPlayer.setY(50);

        currentPhase.setText("Choose your starting sector");
        currentPhase.setFont(font);
        currentPhase.setStroke(Color.BLUE);
        currentPhase.setX(Main.WIDTH - 400);
        currentPhase.setY(100);

        currentPoints.setText(" ");
        currentPoints.setFont(font);
        currentPoints.setStroke(Color.BLUE);
        currentPoints.setX(Main.WIDTH - 400);
        currentPoints.setY(150);

        hintText.setText("Press E, then LMB to deploy a new ship\nIn order to move a ship, click on it,\nthen click where you want to move it");
        hintText.setFont(font);
        hintText.setStroke(Color.GREEN);
        hintText.setX(20);
        hintText.setY(50);

        availableShips.setText("Available ships: "+Main.numOfShipsToDeploy.get(0));
        availableShips.setFont(font);
        availableShips.setStroke(Color.BLUE);
        availableShips.setX(Main.WIDTH - 400);
        availableShips.setY(200);

        String filepath = "spaceship.png";
        imageView = new ImageView("file:"+filepath);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
        imageView.setX(Main.WIDTH - 400);
        imageView.setY(250);

        priceOfBasicShip.setText("Buy for 3 Points");
        priceOfBasicShip.setFont(font);
        priceOfBasicShip.setStroke(Color.BLUE);
        priceOfBasicShip.setX(Main.WIDTH - 400);
        priceOfBasicShip.setY(350);
        priceOfBasicShip.setOnMouseClicked(event -> {
            if (Main.player == 1) {
                if(HUD.pointsPlayer1 >= 3){
                    Main.numOfShipsToDeploy.set(0, Main.numOfShipsToDeploy.get(0) + 1);
                    availableShips.setText("Available ships: "+Main.numOfShipsToDeploy.get(0));
                    HUD.pointsPlayer1 -= 3;
                }
                else{
                    HUD.notify("Not enough points");
                }
            }
            if (Main.player == 2) {
                if(HUD.pointsPlayer2 >= 3){
                    Main.numOfShipsToDeploy.set(1, Main.numOfShipsToDeploy.get(1) + 1);
                    availableShips.setText("Available ships: "+Main.numOfShipsToDeploy.get(1));
                    HUD.pointsPlayer2 -= 3;
                }
                else{
                    HUD.notify("Not enough points");
                }
            }
        });

        Main.root.getChildren().addAll(currentPlayer, currentPhase, currentPoints, imageView, priceOfBasicShip, availableShips);
        Main.scene.setFill(Color.BLACK);
    }

    public static void update(int player, Main.Phase phase){
        currentPlayer.setText("Turn: Player "+player);
        if (player == 1) {
            currentPoints.setText("Points:"+ " " + pointsPlayer1);
            currentPoints.setStroke(Color.BLUE);
            priceOfBasicShip.setStroke(Color.BLUE);
            availableShips.setStroke(Color.BLUE);
            availableShips.setText("Available ships: "+Main.numOfShipsToDeploy.get(0));
        }
        if (player == 2) {
            currentPoints.setText("Points:"+ " " + pointsPlayer2);
            currentPoints.setStroke(Color.RED);
            priceOfBasicShip.setStroke(Color.RED);
            availableShips.setStroke(Color.RED);
            availableShips.setText("Available ships: "+Main.numOfShipsToDeploy.get(1));
        }
        if(phase == Main.Phase.BATTLES){
            currentPlayer.setVisible(false);
        }
        else currentPlayer.setVisible(true);
        if(player == 1){
            currentPlayer.setStroke(Color.BLUE);
            currentPhase.setStroke(Color.BLUE);
        }
        else{
            currentPlayer.setStroke(Color.RED);
            currentPhase.setStroke(Color.RED);
        }
        if(phase == Main.Phase.STARTING_SECTORS){
            currentPhase.setText("Choose your starting sector");
        }
        else if(phase == Main.Phase.SHIPS){
            currentPhase.setText("Prepare your ships");
            if(!Main.root.getChildren().contains(hintText)) Main.root.getChildren().add(hintText);
        }
        else if(phase == Main.Phase.SECTORS){
            currentPhase.setText("Choose a new sector");
            if(!Main.root.getChildren().contains(currentPoints)) Main.root.getChildren().add(currentPoints);
            if(!Main.root.getChildren().contains(priceOfBasicShip)) Main.root.getChildren().add(priceOfBasicShip);
            if(!Main.root.getChildren().contains(imageView)) Main.root.getChildren().add(imageView);
        }
        else{
            currentPhase.setText("Battle");
            currentPhase.setStroke(Color.GREEN);
            Main.root.getChildren().remove(hintText);
            if(Main.root.getChildren().contains(currentPoints)) Main.root.getChildren().remove(currentPoints);
            if(Main.root.getChildren().contains(availableShips)) Main.root.getChildren().remove(availableShips);
            if(Main.root.getChildren().contains(priceOfBasicShip)) Main.root.getChildren().remove(priceOfBasicShip);
            if(Main.root.getChildren().contains(imageView)) Main.root.getChildren().remove(imageView);
            Main.startBattles();
        }
    }

    public static void notify(String message){
        Text text = new Text(message);
        text.setFont(font);
        text.setStroke(Color.GREEN);
        text.setX(Main.WIDTH / 2 - 50);
        text.setY(Main.HEIGHT - 50);
        Main.root.getChildren().add(text);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> {
            Main.root.getChildren().remove(text);
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
