package max.selfsolvingsudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

import java.io.IOException;

public class System extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(System.class.getResource("LoginScreen.fxml"));
            Parent root = fxmlLoader.load();

            // Create the LinearGradient for the background color
            LinearGradient backgroundGradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#74c2e3")),
                    new Stop(1, Color.web("#00a8e8"))
            );

            // Set the background color for the Scene
            Scene scene = new Scene(root);
            scene.setFill(backgroundGradient);

            stage.setTitle("SUDOKU");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}