module espol.edu.ec.borradorproyectofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.media;

    opens espol.edu.ec.borradorproyectofx to javafx.fxml;
    opens modelo to javafx.base;
    exports espol.edu.ec.borradorproyectofx;
}
