package max.selfsolvingsudoku;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.ArrayList;


public class UserProfileController {
    // ----------------------------- Variables ----------------------------- //
    @FXML
    Label usernameLabel, solvedLabel, mistakesLabel;
    @FXML
    ListView gameList;
    private SystemController listener = null;
    public static int windowCounter = 0;

    // ----------------------------- FXML Methods ----------------------------- //
    @FXML
    public void onDeleteHistoryButtonClicked() {
        Database.deleteRowsByUsername(LoginController.currentPlayer.getUsername());
        setGameList();
        LoginController.currentPlayer.setSavedGamesCounter(1);
    }

    @FXML
    public void onLoadGameClicked(ActionEvent e) {
        String selectedItem = gameList.getSelectionModel().getSelectedItem().toString();
        if (selectedItem != null) {
            SudokuGameData clickedGame = Database.getGameByUsernameAndDate(LoginController.currentPlayer.getUsername(), selectedItem);
            listener.loadExistingGame(clickedGame);
            windowCounter = 0;
            ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
        }
    }

    // ----------------------------- FXML Methods : Animations ----------------------------- //

    @FXML
    public void onButtonHoverStart(Event e) {
        Animator.btnOnHover(e,0.2,0.875);
    }

    @FXML
    public void onButtonHoverEnd(Event e) {
        Animator.btnOnHover(e,0.4,1);
    }

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
        ArrayList<String> temp = Database.getAllUserGameDates(LoginController.currentPlayer.getUsername());
        gameList.getItems().clear();
        for (String date: temp) {
            gameList.getItems().add(date);
        }
    }

    public void addListener(SystemController listener) {
        this.listener = listener;
    }
}
