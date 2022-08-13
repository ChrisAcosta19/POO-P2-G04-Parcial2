/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package espol.edu.ec.borradorproyectofx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.io.*;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class PrimaryController implements Initializable {

    @FXML
    private Button btnServicios;
    @FXML
    private Button btnEmpleados;
    @FXML
    private Button btnCitas;
    @FXML
    private Button btnClientes;
    @FXML
    private Button btnAtenciones;
    @FXML
    private Button btnSalir;
    @FXML
    private ImageView imgServicios;
    @FXML
    private ImageView imgCitas;
    @FXML
    private ImageView imgAtenciones;
    @FXML
    private ImageView imgEmpleados;
    @FXML
    private ImageView imgClientes;
    @FXML
    private ImageView imgSalir;

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
        
        btnSalir.setOnAction(eh -> {
            Node source = (Node) eh.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
    }

    private void cargarImagenes(){
        InputStream input = null;
        Image image = null;
        try {
            input = new FileInputStream(App.pathImg + "iconServicios.png");
            image = new Image(input, 100, 100, false, false);
            imgServicios.setImage(image);
            
            input = new FileInputStream(App.pathImg + "iconCitas.png");
            image = new Image(input, 100, 100, false, false);
            imgCitas.setImage(image);
            
            input = new FileInputStream(App.pathImg + "iconAtenciones.png");
            image = new Image(input, 100, 100, false, false);
            imgAtenciones.setImage(image);
            
            input = new FileInputStream(App.pathImg + "iconEmpleados.png");
            image = new Image(input, 100, 100, false, false);
            imgEmpleados.setImage(image);
            
            input = new FileInputStream(App.pathImg + "iconClientes.png");
            image = new Image(input, 100, 100, false, false);
            imgClientes.setImage(image);
            
            input = new FileInputStream(App.pathImg + "iconSalir.png");
            image = new Image(input, 100, 100, false, false);
            imgSalir.setImage(image);
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