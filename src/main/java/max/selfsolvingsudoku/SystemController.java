package max.selfsolvingsudoku;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.IntPredicate;

public class SystemController {
    @FXML
    Label header;

    @FXML
    GridPane gameGrid;

    private TextField activeField = null;

    @FXML
    protected void onSquareClick(MouseEvent e) {
        this.resetLabelBorder();
        activeField = identifyTextfield(e);
        activeField.setStyle("-fx-border-width: 4; -fx-border-color: blue;");
    }

    @FXML
    protected void enterUserInput(KeyEvent e) {
        if (activeField != null) {
            if (checkNumberInput(e.getText())) {
                if (gameRules(activeField.getId(), e.getText()))
                    activeField.setText(e.getText());
            }
        }
    }

    // Game Logic
    public boolean gameRules(String id, String value) {
        ObservableList<Node> children = gameGrid.getChildren();
        return (checkRow(children, idToRow(id), value) &&
                checkColumn(children, idToCol(id), value) &&
                checkSquare(children, idToRow(id), idToCol(id), value));
    }

    private boolean checkRow(ObservableList<Node> children, int row, String value) {
        for (Node node: children) {
            if (node.getId() == null) break;
            if (idToRow(node.getId()) == row) {
                if (Objects.equals(((TextField) node).getText(), value)) return false;
            } else if (idToRow(node.getId()) > row) return true;
        }
        return true;
    }

    private boolean checkColumn(ObservableList<Node> children, int col, String value) {
        for (Node node: children) {
            if (node.getId() == null) break;
            if (idToCol(node.getId()) != col) continue;
            else if (Objects.equals(((TextField) node).getText(), value)) return false;
        }
        return true;
    }

    private boolean checkSquare(ObservableList<Node> children,int row, int col, String value) {
        int[] sqr = findSquare(row,col);
        if (sqr[0] == -1) return false;

        for (Node node: children) {
            String id = node.getId();
            if (id == null) break;
            if ((idToRow(id) == sqr[0] || idToRow(id) == sqr[0]-1 || idToRow(id) == sqr[0]-2) &&
                    (idToCol(id) == sqr[1] || idToCol(id) == sqr[1]-1 || idToCol(id) == sqr[1]-2)) {
                if (Objects.equals(((TextField) node).getText(), value)) return false;
            }
        }
        return true;
    }

    // Helper Methods
    private TextField identifyTextfield(MouseEvent e) { return (TextField)e.getSource(); }

    private void resetLabelBorder() {
        if (activeField != null)
            activeField.setStyle("-fx-border-width: 0;");
    }

    private boolean checkNumberInput(String input) {
        if (input.length() != 1) return false;
        try {
            int x = Integer.parseInt(input);
            return x != 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int idToRow(String id) {
        return (id.charAt(0) - 'a');
    }

    private int idToCol(String id) {
        return id.charAt(1) - '1';
    }

    private int[] findSquare(int row, int col) {
        if ((row >= 0 && row <= 2) && (col >= 0 && col <= 2)) return new int[]{2,2};
        else if ((row >= 0 && row <= 2) && (col >= 3 && col <= 5)) return new int[]{2,5};
        else if ((row >= 0 && row <= 2) && (col >= 6 && col <= 8)) return new int[]{2,8};
        else if ((row >= 3 && row <= 5) && (col >= 0 && col <= 2)) return new int[]{5,2};
        else if ((row >= 3 && row <= 5) && (col >= 3 && col <= 5)) return new int[]{5,5};
        else if ((row >= 3 && row <= 5) && (col >= 6 && col <= 8)) return new int[]{5,8};
        else if ((row >= 6 && row <= 8) && (col >= 0 && col <= 2)) return new int[]{8,2};
        else if ((row >= 6 && row <= 8) && (col >= 3 && col <= 5)) return new int[]{8,5};
        else if ((row >= 6 && row <= 8) && (col >= 6 && col <= 8)) return new int[]{8,8};
        return new int[]{-1,-1};
    }
}