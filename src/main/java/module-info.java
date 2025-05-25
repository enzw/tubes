module com.tubes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires transitive javafx.graphics;

    opens com.tubes.controller to javafx.fxml;
    opens com.tubes.model to javafx.base;
    opens com.tubes.main to javafx.graphics;

    exports com.tubes.main;
}