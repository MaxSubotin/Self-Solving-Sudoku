package max.selfsolvingsudoku;

import javafx.animation.ScaleTransition;
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
import javafx.util.Duration;


import java.io.IOException;
import java.util.Objects;

public class SceneController {

    // ----------------------------- Variables ----------------------------- //

    private Stage stage;
    private Scene scene;
    private Parent root;
    private static String level;

    @FXML
    Label result;

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

        FXMLLoader loader = new FXMLLoader (getClass().getResource("MainScreen.fxml")) ;
        root = loader.load();
        SystemController s = loader.getController();
        s.setup(level);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.setFill(backgroundGradient);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToStartScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.setFill(backgroundGradient);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToEndScene(KeyEvent event, String temp) throws IOException {
        FXMLLoader loader = new FXMLLoader (getClass().getResource("EndScreen.fxml")) ;
        root = loader.load();
        SceneController s = loader.getController();
        s.result.setText("You "+temp+"!");

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.setFill(backgroundGradient);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onButtonHoverStart(Event e) {
        Button btn = (Button) e.getSource();

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.275), btn);
        scaleTransition.setToX(0.9);
        scaleTransition.setToY(0.9);

        scaleTransition.play();
    }

    @FXML
    public void onButtonHoverEnd(Event e) {
        Button btn = (Button)e.getSource();

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), btn);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);

        scaleTransition.play();
    }

}
