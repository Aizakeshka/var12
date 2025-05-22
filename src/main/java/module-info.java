module com.example.var12 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.var12 to javafx.fxml;
    exports com.example.var12;
}