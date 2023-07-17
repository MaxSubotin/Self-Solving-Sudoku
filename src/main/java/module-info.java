module com.maxim.subotin.selfsolvingsudoku {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.maxim.subotin.selfsolvingsudoku to javafx.fxml;
    exports com.maxim.subotin.selfsolvingsudoku;
}