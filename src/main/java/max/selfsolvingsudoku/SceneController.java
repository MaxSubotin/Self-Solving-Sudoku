package max.selfsolvingsudoku;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.control.Label;


import java.io.IOException;
import java.util.Objects;

public class SceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private static String level;

    @FXML
    Label a, result;

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
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToStartScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
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
        stage.setScene(scene);
        stage.show();
    }

}
