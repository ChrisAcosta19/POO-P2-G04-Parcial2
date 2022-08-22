
package espol.edu.ec.borradorproyectofx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.io.*;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class PrimaryController implements Initializable {

    @FXML private BorderPane mainPane;
    @FXML private ImageView iconSalir;
    @FXML private GridPane buttonsPane;
    @FXML private Button btnCitas;
    @FXML private ImageView iconCitas;
    @FXML private ImageView iconSrv;
    @FXML private ImageView iconAtens;
    @FXML private ImageView iconClientes;
    @FXML private ImageView iconEmpl;
    @FXML private Button btnGame;
    @FXML private Button btnServicios;
    @FXML private Button btnEmpleados;
    @FXML private Button btnClientes;
    @FXML private Button btnAtenciones;


       /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setImage("iconServicios",App.pathImg,iconSrv);
        App.setImage("iconCitas",App.pathImg,iconCitas);
        App.setImage("iconAtenciones",App.pathImg,iconAtens);
        App.setImage("iconEmpleados",App.pathImg,iconEmpl);
        App.setImage("iconClientes",App.pathImg,iconClientes);
        App.setImage("iconSalir",App.pathImg,iconSalir);
        btnServicios.setOnAction(eh -> {
            try {
                App.setRoot("Servicios");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        btnEmpleados.setOnAction(eh -> {
            try {
                App.setRoot("Empleados");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        btnClientes.setOnAction(eh -> {
            try {
                App.setRoot("Clientes");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        btnCitas.setOnAction(eh -> {
            try {
                App.setRoot("Citas");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        iconSalir.setOnMouseClicked(eh -> {
            Node source = (Node) eh.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        
        btnGame.setOnAction(eh -> {
            try {
                App.setRoot("gameMain");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    
}