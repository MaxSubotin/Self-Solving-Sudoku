package max.selfsolvingsudoku;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class LoginController {
    // ----------------------------- Variables ----------------------------- //
    @FXML
    Button loginButton, signupButton;

    // ----------------------------- FXML Methods ----------------------------- //
    @FXML
    public void onLoginButtonClicked(ActionEvent e) {
        try {
            handleUserLoggedIn(e);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    public void onSigninButtonClicked(ActionEvent e) {

    }

    // ----------------------------- FXML Methods : Animations ----------------------------- //

    @FXML
    public void onButtonHoverStart(Event e) {
        Animator.btnOnHover(e,0.2,1.1);
    }

    @FXML
    public void onButtonHoverEnd(Event e) {
        Animator.btnOnHover(e,0.4,1);
    }

    // ----------------------------- Helper Methods ----------------------------- //

    public void handleUserLoggedIn(ActionEvent e) throws IOException {
        SceneController s = new SceneController();
        s.switchToStartScene(e, "USER NAME");
    }
}
