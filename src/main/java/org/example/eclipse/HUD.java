package org.example.eclipse;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class HUD {
    static Font font = Font.font("Roboto", 30);
    static Text currentPlayer = new Text();
    static Text currentPhase = new Text();

    public static void init(){
        currentPlayer.setText("Turn: Player 1");
        currentPlayer.setFont(font);
        currentPlayer.setStroke(Color.BLUE);
        currentPlayer.setX(Main.WIDTH - 300);
        currentPlayer.setY(50);

        currentPhase.setText("Choose a new sector");
        currentPhase.setFont(font);
        currentPhase.setStroke(Color.BLUE);
        currentPhase.setX(Main.WIDTH - 300);
        currentPhase.setY(100);

        Main.root.getChildren().addAll(currentPlayer, currentPhase);
        Main.scene.setFill(Color.BLACK);
    }

    public static void update(int player, Main.Phase phase){
        currentPlayer.setText("Turn: Player "+player);
        if(player == 1){
            currentPlayer.setStroke(Color.BLUE);
            currentPhase.setStroke(Color.BLUE);
        }
        else{
            currentPlayer.setStroke(Color.RED);
            currentPhase.setStroke(Color.RED);
        }
        if(phase == Main.Phase.SHIPS){
            currentPhase.setText("Prepare your ships");
        }
        else if(phase == Main.Phase.SECTORS){
            currentPhase.setText("Choose a new sector");
        }
        else{
            currentPhase.setText("Battle");
        }
    }
}
