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
import java.io.*;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import modelo.Servicio;
/**
 * FXML Controller class
 *
 * @author chris
 */
public class NuevoServicioController implements Initializable {


    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDuracion;
    @FXML
    private TextField txtPrecio;
    @FXML
    private RadioButton rbI;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private ToggleGroup estado;
    @FXML
    private Label lblTitulo;
    @FXML
    private RadioButton rbA;
    
    public static Servicio servicio;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCancelar.setOnAction(eh -> {
            try {
                App.setRoot("Servicios");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }    
    
    @FXML
    private void guardarServicio(ActionEvent event) {
        ArrayList<Servicio> servicios = Servicio.cargarServicios(App.pathServicios);//cargar la lista del archivo
        System.out.println("Guardando servicio");
        RadioButton selectedRadioButton = (RadioButton) estado.getSelectedToggle();
        String estado = selectedRadioButton.getText();
        System.out.println(estado);
        boolean estadoBoolean = estado.equals("Activo");
        Servicio s = new Servicio(txtNombre.getText(),
                Integer.parseInt(txtDuracion.getText()),
                Double.parseDouble(txtPrecio.getText()),
                estadoBoolean);
        
        if(servicio == null){
            servicios.add(s);//agregar servicio a la lista
            System.out.println("Nuevo Servicio:" + s);

            //serializar la lista
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathServicios))){
                out.writeObject(servicios);
                out.flush();

                //mostrar informacion
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Resultado de la operación");
                alert.setContentText("Nuevo servicio agregado exitosamente");

                alert.showAndWait();
                App.setRoot("Servicios");

            } catch (IOException ex) {
                System.out.println("IOException:" + ex.getMessage());
            }
        } else {
            int indice = servicios.indexOf(servicio);
            servicios.set(indice, s);
            
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathServicios))){
                out.writeObject(servicios);
                out.flush();

                //mostrar informacion
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Resultado de la operación");
                alert.setContentText("Servicio editado exitosamente");

                alert.showAndWait();
                App.setRoot("Servicios");

            } catch (IOException ex) {
                System.out.println("IOException:" + ex.getMessage());
            }
            servicio = null;
        }
    }
    
    public void llenarCampos(Servicio s){
        lblTitulo.setText("Editar Servicio");
        txtNombre.setText(s.getNombre());
        txtDuracion.setText(String.valueOf(s.getDuracion()));
        txtPrecio.setText(String.valueOf(s.getPrecio()));
        
        if (s.getEstado().equals("Activo"))
            rbA.setSelected(true);
        else
            rbI.setSelected(true);
    }
}