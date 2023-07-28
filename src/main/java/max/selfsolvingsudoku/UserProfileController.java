package max.selfsolvingsudoku;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;


public class UserProfileController {
    // ----------------------------- Variables ----------------------------- //
    @FXML
    Label usernameLabel, solvedLabel, mistakesLabel;
    @FXML
    ListView gameList;
    private DatabaseConfig config = new DatabaseConfig();
    public static int windowCounter = 0;

    // ----------------------------- FXML Methods ----------------------------- //


    // ----------------------------- FXML Methods : Animations ----------------------------- //


    // ----------------------------- Helper Methods ----------------------------- //
    public void initialize() { // this method will run when the user profile window first opens
        UserProfileController.windowCounter++;

        usernameLabel.setText("Username: " + LoginController.currentPlayer.getUsername());
        setSolvedLabel();
        setMistakesLabel();

        setGameList();
    }

    public void setMistakesLabel() {
        mistakesLabel.setText("Total Mistakes: " + LoginController.currentPlayer.getTotalMistakesCounter());
    }

    public void setSolvedLabel() {
        solvedLabel.setText("Solved Puzzles: " + LoginController.currentPlayer.getSolvedPuzzlesCounter());
    }

    public void setGameList() {
        ArrayList<String> temp = Database.getAllUserGameDates(config, LoginController.currentPlayer.getUsername());
        gameList.getItems().clear();
        for (String date: temp) {
            gameList.getItems().add(date);
        }
    }
}
