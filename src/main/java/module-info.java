module com.maxim.subotin.selfsolvingsudoku {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires com.zaxxer.hikari;


    opens max.selfsolvingsudoku to javafx.fxml;
    exports max.selfsolvingsudoku;
}