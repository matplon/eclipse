package org.example.eclipse;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class HUD {
    static Font font = Font.font("Roboto", 30);
    static Text currentPlayer = new Text();
    static Text currentPhase = new Text();
    static Button roll = new Button("Roll");

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

        Main.root.getChildren().addAll(currentPlayer, currentPhase);
        Main.scene.setFill(Color.BLACK);
    }

    public static void update(int player, Main.Phase phase){
        currentPlayer.setText("Turn: Player "+player);
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
        }
        else if(phase == Main.Phase.SECTORS){
            currentPhase.setText("Choose a new sector");
        }
        else{
            currentPhase.setText("Battle");
            Main.startBattles();
        }
    }

    public static void invalidSector(){
        Text text = new Text("Invalid sector");
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

    public static void battle(){
        Button button
    }
}
