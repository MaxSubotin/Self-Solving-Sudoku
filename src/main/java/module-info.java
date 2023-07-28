module com.maxim.subotin.selfsolvingsudoku {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;


    opens max.selfsolvingsudoku to javafx.fxml;
    exports max.selfsolvingsudoku;
}