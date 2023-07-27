package max.selfsolvingsudoku;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    // ----------------------------- Variables ----------------------------- //
    @FXML
    Button loginButton, signupButton;
    @FXML
    TextField loginUsername, loginPassword, signupUsername, signupPassword;
    @FXML
    Label loginErrorMessage, signupErrorMessage;

    public static Player currentPlayer;

    // ----------------------------- FXML Methods ----------------------------- //
    @FXML
    public void onLoginButtonClicked(ActionEvent e) throws IOException {
        String username = loginUsername.getText(), password = loginPassword.getText();
        // maybe look to encrypt the username and password ?

        if (!checkUserCredentials(username,password) || username == null || password == null)
            loginErrorMessage.setText("Error, check your username and password and try again.");
        else {
            LoginController.currentPlayer = new Player("Maxim"); // pass in the real username
            showStartScene(e);
        }
    }

    @FXML
    public void onSignupButtonClicked(ActionEvent e) {

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

    public boolean checkUserCredentials(String username, String password) {
        if (username != null && password != null)
            return Objects.equals(username, "a") && Objects.equals(password, "a");
        return false;
    }

    public void showStartScene(ActionEvent e) throws IOException {
        SceneController s = new SceneController();
        s.switchToStartScene(e);
    }
}
