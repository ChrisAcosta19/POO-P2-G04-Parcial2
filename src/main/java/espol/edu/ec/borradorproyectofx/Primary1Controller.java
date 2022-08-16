
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
public class Primary1Controller implements Initializable {

    @FXML
    private BorderPane mainPane;
    @FXML
    private ImageView iconSalir;
    @FXML
    private GridPane buttonsPane;
    @FXML
    private Button btnCitas;
    @FXML
    private ImageView iconCitas;
    @FXML
    private ImageView iconSrv;
    @FXML
    private ImageView iconAtens;
    @FXML
    private ImageView iconClientes;
    @FXML
    private ImageView iconEmpl;
    @FXML
    private Button btnGame;
    @FXML
    private Button btnServicios;
    @FXML
    private Button btnEmpleados;
    @FXML
    private Button btnClientes;
    @FXML
    private Button btnAtenciones;


       /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarImagenes();
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

    private void cargarImagenes(){
        InputStream input = null;
        Image image = null;
        try {
            input = new FileInputStream(App.pathImg + "iconServicios.png");
            image = new Image(input, 100, 100, false, false);
            iconSrv.setImage(image);
            
            input = new FileInputStream(App.pathImg + "iconCitas.png");
            image = new Image(input, 100, 100, false, false);
            iconCitas.setImage(image);
            
            input = new FileInputStream(App.pathImg + "iconAtenciones.png");
            image = new Image(input, 100, 100, false, false);
            iconAtens.setImage(image);
            
            input = new FileInputStream(App.pathImg + "iconEmpleados.png");
            image = new Image(input, 100, 100, false, false);
            iconEmpl.setImage(image);
            
            input = new FileInputStream(App.pathImg + "iconClientes.png");
            image = new Image(input, 100, 100, false, false);
            iconClientes.setImage(image);
            
            input = new FileInputStream(App.pathImg + "iconSalir.png");
            image = new Image(input, 100, 100, false, false);
            iconSalir.setImage(image);
        } catch (IOException ex) {
            System.out.println("No se pudo cargar imagen");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    System.out.println("Error al cerrar el recurso");
                }
            }
        }
    }
}