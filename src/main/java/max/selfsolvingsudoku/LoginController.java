package max.selfsolvingsudoku;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

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

        if (!checkUserExistence(username,password) || username == null || password == null)
            loginErrorMessage.setText("Error, check your username and password and try again.");
        else {
            if (Database.isPasswordCorrectForUsername(username,password)) {
                LoginController.currentPlayer = Database.getUserFromDatabase(username);
                showStartScene(e);
            } else {
                loginErrorMessage.setText("Error, wrong password.");
            }
        }
    }

    @FXML
    public void onSignupButtonClicked(ActionEvent e) throws IOException {
        String username = signupUsername.getText(), password = signupPassword.getText();
        if (checkUserExistence(username,password) || username == null || password == null)
            signupErrorMessage.setText("Error, username already exists, try another one.");
        else if (checkUserCredentials(username,password)) {
            Database.addUserToDatabase(username, password);
            LoginController.currentPlayer = new Player(username,0,0); // pass in the real username
            LoginController.currentPlayer.setSolvedPuzzlesCounter(0);
            LoginController.currentPlayer.setTotalMistakesCounter(0);
            showStartScene(e);
        }
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

    public boolean checkUserExistence(String username, String password) {
        if (username != null && password != null) {
            return Database.isUsernameUnique(username);
        }
        return false;
    }

    public boolean checkUserCredentials(String username, String password) {
        // Regular expressions for username and password validation
        String usernameRegex = "^[a-zA-Z0-9]{3,20}$";
        String passwordRegex = "^(?=.*[a-zA-Z])(?=.*\\d).{6,20}$";

        // Check username
        if (!username.matches(usernameRegex)) {
            signupErrorMessage.setText("Invalid username format. It should contain only letters and numbers, and its length should be between 3 and 20 characters.");
            return false;
        }

        // Check password
        if (!password.matches(passwordRegex)) {
            signupErrorMessage.setText("Invalid password format. It should contain at least one letter, one number, and its length should be between 6 and 20 characters.");
            return false;
        }

        return true;
    }

    public void showStartScene(ActionEvent e) throws IOException {
        SceneController s = new SceneController();
        s.switchToStartScene(e);
    }
}
