package max.selfsolvingsudoku;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class SystemController {

    TextField activeField = null;
    @FXML
    protected void onHelloButtonClick(MouseEvent e) {
        this.resetLabelBorder();
        activeField = identifyLabel(e);
        activeField.setStyle("-fx-border-width: 4; -fx-border-color: blue;");
    }

    @FXML
    protected void enterUserInput(KeyEvent e) {
        if (activeField != null) {
            if (checkNumberInput(e.getText())) {
                activeField.setText(e.getText());
            }
        }
    }

    // Helper Methods
    private TextField identifyLabel(MouseEvent e) { return (TextField)e.getSource(); }

    private void resetLabelBorder() {
        if (activeField != null)
            activeField.setStyle("-fx-border-width: 0;");
    }

    private boolean checkNumberInput(String input) {
        if (input.length() != 1) return false;
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }
}