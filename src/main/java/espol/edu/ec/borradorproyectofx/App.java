package espol.edu.ec.borradorproyectofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import modelo.*;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static String pathServicios = "archivos/Servicio.bin";
    public static String pathEmpleados = "archivos/Empleado.bin";
    public static String pathClientes = "archivos/Clientes.bin";
    public static String pathCitas = "archivos/Citas.bin";
    public static String pathAtenciones = "archivos/Atenciones.bin";
    public static String pathImg = "images/";
    public static String pathImgGame = "src/main/resources/images/game/";

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary1"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        Servicio.crearArchivo(pathServicios);
        Empleado.crearArchivo(pathEmpleados, pathServicios);
        Cliente.crearArchivo(pathClientes);
        //Cita.crearArchivo(pathCitas);
        //Atencion.crearArchivo(pathAtenciones);
        launch();
    }

    //metodo para cambiar el contenido de la escena
    static void changeRoot(Parent rootNode) {
        scene.setRoot(rootNode);
    }
}