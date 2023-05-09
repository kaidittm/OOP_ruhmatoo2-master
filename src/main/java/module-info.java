module com.example.ruhmatoo2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ruhmatoo2 to javafx.fxml;
    exports com.example.ruhmatoo2;
}