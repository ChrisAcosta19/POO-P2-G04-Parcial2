/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package espol.edu.ec.borradorproyectofx;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.*;
import java.io.*;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class NuevoEmpleadoController implements Initializable {

    @FXML
    private TextField txtCedula;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtEmail;
    @FXML
    private RadioButton rbA;
    @FXML
    private ToggleGroup estado;
    @FXML
    private RadioButton rbI;
    @FXML
    private ComboBox<Servicio> cmbServicios;
    @FXML
    private Button btnAgregarServicio;
    @FXML
    private Button btnEliminarServicio;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TableView<Servicio> tvServicios;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colDuracion;
    @FXML
    private TableColumn colPrecio;
    @FXML
    private TableColumn colEstado;
    @FXML
    private Label lblTitulo;
    
    private ArrayList<Servicio> servicios;
    public static Empleado empleado;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(empleado == null){
            servicios = new ArrayList<>();
        }else {
            servicios = empleado.getListaServicios();
        }
        
        cmbServicios.getItems().setAll(Servicio.cargarServicios(App.pathServicios));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        btnCancelar.setOnAction(eh -> {
            try {
                App.setRoot("Empleados");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }    
    
    @FXML
    private void agregarServicio(ActionEvent event) {
        Servicio s = cmbServicios.getValue();
        if(s == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Debe seleccionar un servicio del ComboBox");
            alert.showAndWait();
        } else {
            if(!servicios.contains(s)){
                servicios.add(s);
            }
            tvServicios.getItems().setAll(servicios);
        }
    }

    @FXML
    private void eliminarServicio(ActionEvent event) {
        Servicio s = (Servicio) tvServicios.getSelectionModel().getSelectedItem();
        if(s == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Debe seleccionar un servicio de la tabla");
            alert.showAndWait();
        }else{
            servicios.remove(s);
            tvServicios.getItems().setAll(servicios);
        }
    }

    @FXML
    private void guardarEmpleado(ActionEvent event) {
        ArrayList<Empleado> empleados = Empleado.cargarEmpleados(App.pathEmpleados);
        System.out.println("Guardando empleado");
        RadioButton selectedRadioButton = (RadioButton) estado.getSelectedToggle();
        String estado = selectedRadioButton.getText();
        System.out.println(estado);
        boolean estadoBoolean = estado.equals("Activo");
        Empleado e = new Empleado(txtCedula.getText(),
                txtNombre.getText(),
                txtTelefono.getText(),
                txtEmail.getText(),
                estadoBoolean);
        e.setListaServicios(servicios);
        
        if(empleado == null){
            empleados.add(e);
            System.out.println("Nuevo Empleado:" + e);
            //serializar la lista
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathEmpleados))){
                out.writeObject(empleados);
                out.flush();

                //mostrar informacion
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Resultado de la operación");
                alert.setContentText("Nuevo empleado agregado exitosamente");

                alert.showAndWait();
                App.setRoot("Empleados");

            } catch (IOException ex) {
                System.out.println("IOException:" + ex.getMessage());
            }
        }else{
            int indice = empleados.indexOf(empleado);
            empleados.set(indice, e);
            
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathEmpleados))){
                out.writeObject(empleados);
                out.flush();

                //mostrar informacion
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Resultado de la operación");
                alert.setContentText("Empleado editado exitosamente");

                alert.showAndWait();
                App.setRoot("Empleados");

            } catch (IOException ex) {
                System.out.println("IOException:" + ex.getMessage());
            }
            empleado = null;
        }
    }
    
    public void llenarCampos(Empleado e){
        lblTitulo.setText("Editar Empleado");
        txtCedula.setText(e.getCedula());
        txtNombre.setText(e.getNombre());
        txtTelefono.setText(e.getTelefono());
        txtEmail.setText(e.getEmail());
        if (e.getEstado().equals("Activo"))
            rbA.setSelected(true);
        else
            rbI.setSelected(true);
        tvServicios.getItems().setAll(empleado.getListaServicios());
    }
}