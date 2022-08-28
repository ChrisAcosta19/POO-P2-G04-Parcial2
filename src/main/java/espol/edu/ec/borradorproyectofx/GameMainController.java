package espol.edu.ec.borradorproyectofx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
/**
 * FXML Controller class
 *
 * @author Juan Pablo Plúas
 */
public class GameMainController implements Initializable {
    @FXML private ImageView btnAvanzar;
    @FXML private TextField fieldNumEjercicios;
    @FXML private Label lblEjercicios;
    @FXML private BorderPane mainPane;
    @FXML private ImageView regresar;
    public static int numEjercicios;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setImage("arrow_right",App.pathImgGame,btnAvanzar);
        App.setImage("regresar",App.pathImg,regresar);
        
        btnAvanzar.setOnMouseClicked(eh -> { // Expresión lambda que determina el comportamiento del botón btnAvanzar
            try {
                numEjercicios=Integer.valueOf(fieldNumEjercicios.getText());
                App.setRoot("game"); //Se redirige a la ventana para jugar la sesión
            } catch (Exception ex) { // Si se ingresa un valor que no sea número, se emite una alerta
                fieldNumEjercicios.clear();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error al ingresar número");
                alert.setHeaderText(null);
                alert.setContentText("Ingrese un número válido");
                alert.showAndWait();
            }
        });

        regresar.setOnMouseClicked(eh -> { // Expresión lambda que determina el comportamiento del ImageView regresar
            try {
                App.setRoot("nuevaAtencion"); // Regresa a la ventana en que se está registrando la atención
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

}
