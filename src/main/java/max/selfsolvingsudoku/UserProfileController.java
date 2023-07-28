package max.selfsolvingsudoku;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;


public class UserProfileController {
    // ----------------------------- Variables ----------------------------- //
    @FXML
    Label usernameLabel, solvedLabel, mistakesLabel;
    @FXML
    ListView gameList;
    public static int windowCounter = 0;

    // ----------------------------- FXML Methods ----------------------------- //


    // ----------------------------- FXML Methods : Animations ----------------------------- //


    // ----------------------------- Helper Methods ----------------------------- //
    public void initialize() { // this method will run when the user profile window first opens
        UserProfileController.windowCounter++;

        usernameLabel.setText("Username: " + LoginController.currentPlayer.getUsername());
        solvedLabel.setText("Solved Puzzles: " + LoginController.currentPlayer.getSolvedPuzzlesCounter());
        mistakesLabel.setText("Total Mistakes: " + LoginController.currentPlayer.getTotalMistakesCounter());

        // temporary data
        gameList.getItems().add(new String("gameDate1"));
        gameList.getItems().add(new String("gameDate2"));
        gameList.getItems().add(new String("gameDate3"));
        gameList.getItems().add(new String("gameDate4"));
    }

}
