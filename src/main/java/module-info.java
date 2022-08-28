module proyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.media;

    opens proyecto to javafx.fxml;
    opens proyecto.modelo to javafx.base;
    exports proyecto to javafx.graphics;
}
