package max.selfsolvingsudoku;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class SystemController {

    @FXML
    Label header,mistakesLabel,timer;
    @FXML
    GridPane gameGrid;

    private Timer gameTimer;
    private TextField activeField = null;
    private Sudoku sudoku = null;
    private boolean keyProcessing = false; // a flag to track if a key is being processed
    private int mistakesTotal = 5, mistakesCounter = 0;

    private final String style = "-fx-border-width: 4; -fx-border-color: blue;";
    private final String goodStyle = "-fx-text-fill: black;";
    private final String badStyle = "-fx-text-fill: red;";
    private final int autoSolveTimer = 225;

    public void setup(String level) {
        generateLevel();
        header.setText("Game Mode: "+level);

        if (Objects.equals(level, "Easy"))
            removeSomeNumbers(14);
        else if (Objects.equals(level, "Medium"))
            removeSomeNumbers(29);
        else if (Objects.equals(level, "Hard"))
            removeSomeNumbers(44);

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
            activeField.setEditable(false);
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
            count++;
        }
    }

    // Start the timer


    @FXML
    protected void onSquareClick(MouseEvent e) {
        TextField temp =  identifyTextfield(e);

        if (activeField == temp) return;

        if (activeField != null && activeField.getStyle().contains(badStyle))
            activeField.setStyle(badStyle);
        else
            resetLabelBorder();

        if (temp.getStyle().contains(badStyle)) {
            temp.setStyle(style + badStyle);
        } else
            temp.setStyle(style);

        activeField = temp;
    }

    @FXML
    private void onKeyPressed(KeyEvent event) {
        if (keyProcessing) {
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
                } else {
                    activeField.setStyle(style + goodStyle);
                }
            }

            if (checkEndGame()) {
                try {
                    gameTimer.stopTimer();
                    handlePlayerWins(event);
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
        int i, j, count = 0;
        for (Node node: gameGrid.getChildren()) {
            if (node.getId() == null) break;
            i = idToRow(node.getId());
            j = idToCol(node.getId());
            activeField = (TextField) node;
            if (activeField.getText().isEmpty() || activeField.getStyle().contains(badStyle)) {
                int finalI = i;
                int finalJ = j;
                Timeline timeline = new Timeline();
                KeyFrame keyFrame = new KeyFrame(Duration.millis(autoSolveTimer).multiply(count++ + 1), event -> {
                    TextField temp = (TextField) node;
                    temp.setText(Integer.toString(sudoku.game[finalI][finalJ]));
                    temp.setStyle(goodStyle);
                });
                timeline.getKeyFrames().add(keyFrame);
                timeline.play();
                gameTimer.stopTimer();
            }
        }
    }

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


    @FXML
    public void quitButtonClicked(ActionEvent e) throws IOException {
        SceneController s = new SceneController();
        s.switchToStartScene(e);
    }

    public void handleGameOver(KeyEvent e) throws IOException {
        SceneController s = new SceneController();
        s.switchToEndScene(e, "Lose");
    }

    public void handlePlayerWins(KeyEvent e) throws IOException {
        SceneController s = new SceneController();
        s.switchToEndScene(e,"Win");
    }


    // Helper Methods
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
            mistakesLabel.setText("mistakes counter: " + this.mistakesCounter + " / " + this.mistakesTotal);
    }
}