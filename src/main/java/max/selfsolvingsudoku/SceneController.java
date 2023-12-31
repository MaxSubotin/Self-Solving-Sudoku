package max.selfsolvingsudoku;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.scene.control.Label;


import java.io.IOException;
import java.util.Objects;

public class SceneController {

    // ----------------------------- Variables ----------------------------- //

    private Stage stage;
    private Scene scene;
    private Parent root;
    public static String level;

    @FXML
    Label result, welcomeLabel, errorMessage;

    // Create the LinearGradient for the background color
    private LinearGradient backgroundGradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#74c2e3")),
            new Stop(1, Color.web("#00a8e8"))
    );


    // ----------------------------- FXML Methods ----------------------------- //

    @FXML
    public void switchToMainScene(ActionEvent event) throws IOException {
        String temp = ((Button)event.getSource()).getText();
        if (!Objects.equals(temp, "Retry"))
            level = temp;

        FXMLLoader loader = new FXMLLoader (getClass().getResource("MainScreen.fxml"));
        root = loader.load();
        SystemController s = loader.getController();
        s.setup(level);

        showStage(event);
    }

    @FXML
    public void switchToStartScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader (getClass().getResource("StartScreen.fxml"));
        root = loader.load();
        SceneController s = loader.getController();
        s.setWelcomeLabelText(LoginController.currentPlayer.getUsername());

        showStage(event);
    }

    @FXML
    public void switchToEndScene(KeyEvent event, String temp) throws IOException {
        FXMLLoader loader = new FXMLLoader (getClass().getResource("EndScreen.fxml"));
        root = loader.load();
        SceneController s = loader.getController();
        s.result.setText("You "+temp+"!");

        showStage(event);
    }

    public void switchToLoginScene(ActionEvent event) throws IOException{
        Database.setUserMistakesCounter(LoginController.currentPlayer.getUsername(), LoginController.currentPlayer.getTotalMistakesCounter());

        FXMLLoader loader = new FXMLLoader (getClass().getResource("LoginScreen.fxml"));
        root = loader.load();
        showStage(event);
    }

    @FXML
    public void openUserProfileScene(SystemController s) {
        if (UserProfileController.windowCounter != 0) return; // makes sure there is only one window open at a time
        try {
            // Load the pop-up content from FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserProfile.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new Stage for the info window
            Stage stage = new Stage();
            stage.setTitle("User Profile");

            // Add the setOnCloseRequest event handler
            stage.setOnCloseRequest(event -> {
                UserProfileController.windowCounter = 0;
            });

            Scene scene = new Scene(root);
            scene.setFill(backgroundGradient);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setX(240);
            stage.setY(300);

            // add listeners so that main screen and user profile can update in real time
            s.addListener(fxmlLoader.getController());
            ((UserProfileController)fxmlLoader.getController()).addListener(s);

            // Show the info window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onLoadLastGameButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader (getClass().getResource("MainScreen.fxml"));
        root = loader.load();

        SystemController s = loader.getController();
        s.setSudoku(new Sudoku(3));

        SudokuGameData loadedGame = Database.getLastGame(LoginController.currentPlayer.getUsername());
        if (loadedGame != null) {
            s.loadExistingGame(loadedGame);
            showStage(event);
        }
        else this.errorMessage.setText("You don't have any saved games.");
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


    private void setWelcomeLabelText(String name) {
        this.welcomeLabel.setText("Welcome "+ name + "!");
    }

    private void showStage(Event event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.setFill(backgroundGradient);
        stage.setScene(scene);
        stage.show();
    }

}
