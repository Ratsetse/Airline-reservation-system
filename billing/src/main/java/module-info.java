module com.example.billing {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Open controller package for FXML
    opens com.example.controllers to javafx.fxml;

    // Export all packages that JavaFX needs access to
    exports com.example.billing;
    exports com.example.controllers;
    exports com.example.models;
}
