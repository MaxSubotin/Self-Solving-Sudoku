package max.selfsolvingsudoku;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SystemController {

    // ----------------------------- Variables ----------------------------- //
    private DatabaseConfig config = new DatabaseConfig();

    @FXML
    Label header, mistakesLabel, timer, usernameLabel;
    @FXML
    GridPane gameGrid;
    @FXML
    Button solveButton, hintButton;

    private Timer gameTimer;
    private TextField activeField = null;
    private Sudoku sudoku = null;
    private boolean keyProcessing = false; // a flag to track if a key is being processed
    private boolean solvingOnGoing = false;
    private boolean hintWasUsed = false;
    private final int mistakesTotal = 5;
    private int mistakesCounter = 0;

    private final String style = "-fx-border-width: 4; -fx-border-color: blue;";
    private final String goodStyle = "-fx-text-fill: black;";
    private final String badStyle = "-fx-text-fill: red;";

    private UserProfileController listener = null;


    // ----------------------------- Game Generation Methods ----------------------------- //

    public void setup(String level) {
        generateLevel();
        header.setText("Game Mode: "+level);

        if (Objects.equals(level, "Easy"))
            removeSomeNumbers(14);
        else if (Objects.equals(level, "Medium"))
            removeSomeNumbers(29);
        else if (Objects.equals(level, "Hard"))
            removeSomeNumbers(44);

        solveButton.setVisible(true);
        usernameLabel.setText("@" + LoginController.currentPlayer.getUsername());
        gameTimer = new Timer(this.timer);
        gameTimer.startTimer();
    }

    private void generateLevel() {
        int i, j;
        this.sudoku = new Sudoku(3);
        for (Node node: gameGrid.getChildren()) {
            if (node.getId() == null) return;
            i = idToRow(node.getId());
            j = idToCol(node.getId());
            activeField = (TextField)node;
            activeField.setText(Integer.toString(this.sudoku.game[i][j]));
            activeField.setOpacity(0.75);
        }
    }

    private void removeSomeNumbers(int N) { // can make this method get easy, medium, hard and set count accordingly
        int count = 0;
        while (count <= N) {
            int randomIndex;

            do {
                randomIndex = new Random().nextInt(0, 81);
                activeField = (TextField) gameGrid.getChildren().get(randomIndex);
            } while (activeField.getText().isEmpty());

            activeField.setText("");
            activeField.setOpacity(1);
            activeField.setCursor(Cursor.HAND);
            count++;
        }
    }

    public void loadExistingGame(SudokuGameData existingGame) {
        if (existingGame == null) return;

        int i, j;
        this.sudoku.setGame(existingGame.getSolutionArray());

        setupGameScreen(existingGame);

        for (Node node: gameGrid.getChildren()) {
            if (node.getId() == null) return;
            i = idToRow(node.getId());
            j = idToCol(node.getId());
            activeField = (TextField)node;
            activeField.setText(Integer.toString(existingGame.getGameArray()[i][j]));
            if (existingGame.getGameArray()[i][j] == this.sudoku.game[i][j] && existingGame.getGameArray()[i][j] != 0) {
                activeField.setOpacity(0.75);
                activeField.setStyle(goodStyle);
            }
            else if (existingGame.getGameArray()[i][j] != this.sudoku.game[i][j] && existingGame.getGameArray()[i][j] != 0) {
                activeField.setOpacity(1);
                activeField.setStyle(badStyle);
            }
            else if (existingGame.getGameArray()[i][j] == 0) {
                activeField.setText("");
                activeField.setOpacity(1);
            }
        }
    }

    // ----------------------------- FXML Methods ----------------------------- //

    @FXML
    protected void onSquareClick(MouseEvent e) {
        if (solvingOnGoing) return;

        TextField temp =  identifyTextfield(e);
        if (activeField == temp) return;

        if (activeField != null && activeField.getStyle().contains(badStyle))
            activeField.setStyle(badStyle);
        else
            resetLabelBorder();

        if (temp.getStyle().contains(badStyle)) {
            temp.setStyle(style + badStyle);
        } else if (temp.getOpacity() == 1)
            temp.setStyle(style);

        activeField = temp;
    }

    @FXML
    private void onKeyPressed(KeyEvent event) {
        if (keyProcessing || ((TextField)event.getSource()).getOpacity() != 1 || solvingOnGoing) {
            event.consume();
        } else {
            keyProcessing = true;
        }
    }

    @FXML
    private void onKeyReleased(KeyEvent event) {
        if (keyProcessing) {
            String input = event.getText();

            if (isValidInput(input)) {
                activeField.setText(input);

                if (!isCorrectInput(activeField.getId(), input)) {
                    activeField.setStyle(style + badStyle);
                    increaseMistakes(event);
                    LoginController.currentPlayer.setTotalMistakesCounter(LoginController.currentPlayer.getTotalMistakesCounter() + 1);
                    Database.setUserMistakesCounter(config, LoginController.currentPlayer.getUsername(), LoginController.currentPlayer.getTotalMistakesCounter());
                    if (listener != null) listener.setMistakesLabel();
                } else {
                    activeField.setStyle(style + goodStyle);
                }
            }

            if (checkEndGame()) {
                try {
                    gameTimer.stopTimer();
                    handlePlayerWins(event);
                    if (!solvingOnGoing && !hintWasUsed) {
                        LoginController.currentPlayer.setSolvedPuzzlesCounter(LoginController.currentPlayer.getSolvedPuzzlesCounter() + 1);
                        Database.setUserSolvedCounter(config, LoginController.currentPlayer.getUsername(), LoginController.currentPlayer.getSolvedPuzzlesCounter());
                        if (listener != null) listener.setSolvedLabel();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            keyProcessing = false;
            event.consume();
        }
    }

    @FXML
    public void autoSolve() {
        this.solvingOnGoing = true;
        //int[] count = {0};

        // Create a list to hold the text fields to be auto-solved
        List<TextField> fieldsToAutoSolve = new ArrayList<>();

        for (Node node : gameGrid.getChildren()) {
            if (node.getId() == null) break;
            activeField = (TextField) node;
            if (activeField.getText().isEmpty() || activeField.getStyle().contains(badStyle)) {
                fieldsToAutoSolve.add((TextField) node);
            }
        }

        // Create a single Timeline for all the text fields
        Timeline timeline = new Timeline();

        int autoSolveTimer = 225;
        for (int index = 0; index < fieldsToAutoSolve.size(); index++) {
            TextField field = fieldsToAutoSolve.get(index);
            int i = idToRow(field.getId());
            int j = idToCol(field.getId());

            KeyFrame keyFrame = new KeyFrame(Duration.millis(autoSolveTimer * (index + 1)), event -> {
                field.setText(Integer.toString(sudoku.game[i][j]));
                field.setStyle(goodStyle);
                //count[0]--;

                // Check if this is the last field to be auto-solved
                //if (count[0] == 0) {
                    // The timeline has finished, execute the code you want here
                    // this code will run when the animation is finished and all numbers are loaded
                //}
            });

            timeline.getKeyFrames().add(keyFrame);
            //count[0]++;
        }

        timeline.play();
        gameTimer.stopTimer();
        solveButton.setVisible(false);
        hintButton.setVisible(false);
    }

    @FXML
    public void quitButtonClicked(ActionEvent e) throws IOException {
        SceneController s = new SceneController();
        s.switchToStartScene(e);
    }

    @FXML
    public void showHint(ActionEvent e) {
        if (this.solvingOnGoing) return;

        int randomIndex;
        TextField temp;

        do {
            randomIndex = new Random().nextInt(0, 81);
            temp = (TextField) gameGrid.getChildren().get(randomIndex);
        } while (!temp.getText().isEmpty());

        int i = idToRow(temp.getId());
        int j = idToCol(temp.getId());
        temp.setText(Integer.toString(sudoku.game[i][j]));
        if (checkEndGame()) {
            this.solveButton.setVisible(false);
            this.hintButton.setVisible(false);
        }
        hintWasUsed = true;
    }

    @FXML
    public void onSaveButtonClicked(ActionEvent e) {
        int[][] currentGame = new int[9][9];
        int i, j;
        for (Node node: gameGrid.getChildren()) {
            if (node.getId() == null) break;
            i = idToRow(node.getId());
            j = idToCol(node.getId());
            TextField temp = (TextField) node;
            if (temp.getText().isEmpty())
                currentGame[i][j] = 0; //! make sure to check later if the value is 0 replace with ""
            else
                currentGame[i][j] = Integer.parseInt(temp.getText());
        }
        // save the current game
        Database.saveCurrentGame(config, LoginController.currentPlayer, currentGame, this.sudoku.game, SceneController.level, this.timer.getText(), this.mistakesCounter);
        if (listener != null) listener.setGameList();
    }

    @FXML
    public void showUserProfile() {
        SceneController s = new SceneController();
        s.openUserProfileScene(this);
    }

    // ----------------------------- FXML Methods : Animations ----------------------------- //
    @FXML
    public void onButtonHoverStart(Event e) {
        Animator.btnOnHover(e,0.2,1.15);
    }

    @FXML
    public void onButtonHoverEnd(Event e) {
        Animator.btnOnHover(e,0.4,1);
    }

    // ----------------------------- Helper Methods ----------------------------- //

    private boolean checkEndGame() {
        int i, j;
        for (Node node: gameGrid.getChildren()) {
            if (node.getId() == null) break;
            i = idToRow(node.getId());
            j = idToCol(node.getId());
            TextField temp = (TextField) node;
            if (!Objects.equals(temp.getText(), Integer.toString(sudoku.game[i][j]))) return false;
        }
        return true;
    }

    private boolean isValidInput(String input) {
        try {
            int x = Integer.parseInt(input);
            return x != 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isCorrectInput(String id, String input) {
        int i = idToRow(id);
        int j = idToCol(id);
        return sudoku.game[i][j] == Integer.parseInt(input);
    }

    public void handleGameOver(KeyEvent e) throws IOException {
        SceneController s = new SceneController();
        s.switchToEndScene(e, "Lose");
    }

    public void handlePlayerWins(KeyEvent e) throws IOException {
        SceneController s = new SceneController();
        s.switchToEndScene(e,"Win");
    }

    private TextField identifyTextfield(MouseEvent e) { return (TextField)e.getSource(); }

    private void resetLabelBorder() {
        if (activeField != null)
            activeField.setStyle("");
    }

    private int idToRow(String id) {
        return (id.charAt(0) - 'a');
    }

    private int idToCol(String id) {
        return id.charAt(1) - '1';
    }

    private void increaseMistakes(KeyEvent event) {
        if (++this.mistakesCounter > this.mistakesTotal) {
            try {
                handleGameOver(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else
            mistakesLabel.setText("Mistakes Counter: " + this.mistakesCounter + " / " + this.mistakesTotal);
    }

    public void addListener(UserProfileController listener) {
        this.listener = listener;
    }

    public void setSudoku(Sudoku sudoku) { this.sudoku = sudoku; }

    private void setupGameScreen(SudokuGameData game) {
        this.header.setText("Game Mode: " + game.getDifficulty());

        this.mistakesCounter = game.getMistakes();
        this.mistakesLabel.setText("Mistakes Counter: " + this.mistakesCounter + " / " + this.mistakesTotal);

        if (gameTimer != null) gameTimer.stopTimer();
        gameTimer = new Timer(this.timer);
        gameTimer.startTimerFrom(game.getTimer());
    }
}