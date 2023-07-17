module com.maxim.subotin.selfsolvingsudoku {
    requires javafx.controls;
    requires javafx.fxml;


    opens max.selfsolvingsudoku to javafx.fxml;
    exports max.selfsolvingsudoku;
}