module com.maxim.subotin.selfsolvingsudoku {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens max.selfsolvingsudoku to javafx.fxml;
    exports max.selfsolvingsudoku;
}