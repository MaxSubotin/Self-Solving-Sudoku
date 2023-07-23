package max.selfsolvingsudoku;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class SystemController {

    @FXML
    Label header;
    @FXML
    GridPane gameGrid;
    @FXML
    Label mistakesLabel;

    private TextField activeField = null;
    private Sudoku sudoku = null;
    private boolean keyProcessing = false; // a flag to track if a key is being processed
    private int mistakesTotal = 5;
    private int mistakesCounter = 0;

    public void setup(String level) {
        generateLevel();
        header.setText("Game Mode: "+level);

        if (Objects.equals(level, "Easy"))
            removeSomeNumbers(15);
        else if (Objects.equals(level, "Medium"))
            removeSomeNumbers(30);
        else if (Objects.equals(level, "Hard"))
            removeSomeNumbers(45);
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

    @FXML
    protected void onSquareClick(MouseEvent e) {
        if (activeField.getStyle().contains("-fx-text-fill: red;"))
            activeField.setStyle("-fx-text-fill: red;");
        else
            this.resetLabelBorder();
        activeField = identifyTextfield(e);
        activeField.setStyle("-fx-border-width: 4; -fx-border-color: blue; ");
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
            activeField = (TextField) event.getSource();
            String input = event.getText();

            if (!isCorrectInput(activeField.getId(), input)) {
                activeField.setStyle(activeField.getStyle() + "-fx-text-fill: red;");
                increaseMistakes(event);
            } else {
                activeField.setStyle(activeField.getStyle() + "-fx-text-fill: black;");
            }

            if (isValidInput(input)) {
                activeField.setText(input);
            }
            keyProcessing = false;
            event.consume();
        }
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
        s.switchToEndScene(e);
    }

    public void handlePlayerWins(KeyEvent e) throws IOException {
        SceneController s = new SceneController();
        s.switchToEndScene(e);
    }


    // Helper Methods
    private TextField identifyTextfield(MouseEvent e) { return (TextField)e.getSource(); }

    private void resetLabelBorder() {
        if (activeField != null)
            activeField.setStyle("-fx-border-width: 0;");
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