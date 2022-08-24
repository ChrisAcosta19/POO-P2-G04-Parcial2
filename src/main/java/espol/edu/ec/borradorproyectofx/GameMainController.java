
package espol.edu.ec.borradorproyectofx;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
/**
 * FXML Controller class
 *
 * @author Juan Pablo Plúas
 */
public class GameMainController implements Initializable {


    @FXML private BorderPane mainPane;
    @FXML private ImageView btnAvanzar;
    @FXML private ImageView btnHome;
    @FXML private TextField fieldNumEjercicios;
    public static int numEjercicios;
    
    /*private int numEjercicio=Integer.valueOf(fieldNumEjercicios.getText());
    
    public int getNumEjercicio(){
        return numEjercicio;
    }*/
      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setImage("arrow_right",App.pathImgGame,btnAvanzar);
        App.setImage("home",App.pathImgGame,btnHome);
        
        btnAvanzar.setOnMouseClicked(eh -> {
            try {
                numEjercicios=Integer.valueOf(fieldNumEjercicios.getText());
                App.setRoot("game");
            } catch (Exception ex) {
                fieldNumEjercicios.clear();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al ingresar número");
                    alert.setHeaderText(null);
                    alert.setContentText("Ingrese un número válido");
                    alert.showAndWait();
            }
        });

        btnHome.setOnMouseClicked(eh -> {
            try {
                App.setRoot("primary");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

}
