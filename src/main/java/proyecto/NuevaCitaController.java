package proyecto;

import modelo.Empleado;
import modelo.Validacion;
import modelo.Servicio;
import modelo.Cliente;
import modelo.Cita;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Ronald Gaibor
 */

public class NuevaCitaController implements Initializable{
    
    @FXML private TextField txtFecha;
    @FXML private TextField txtHora;
    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private ComboBox<Servicio> cmbServicio;
    @FXML private ComboBox<Empleado> cmbTerapista;
    @FXML private Label lblTitulo;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    ArrayList<Cita> citas;
    public static Cita cita;
    
     /**
     * Inicialización del controlador para una nueva cita
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        citas = Cita.cargarCitas(App.pathCitas);//cargar la lista del archivo
        btnCancelar.setOnAction(eh -> {
            try {
                App.setRoot("Citas");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        llenarCampos();
    }
    
    /**
     * Método que crea una cita a partir de la información que el usuario introduzca,
     * asegurándose que se llenen todos los campos y que estos sean válidos
     */
    @FXML
    public void crearCita(ActionEvent event) {
        System.out.println("Guardando cita");
        String hora = txtHora.getText();
        if(hora.length() == 5){
            hora += ":00";
        }
        Validacion.validarFecha(txtFecha.getText());
        Validacion.validarHora(hora);
        
        if(Validacion.mensaje.equals("") && cmbCliente.getValue()!= null &&cmbServicio.getValue() != null && cmbTerapista.getValue() != null){
            Cita c = new Cita(txtFecha.getText(),
                              hora,
                              cmbCliente.getValue(),cmbServicio.getValue(),cmbTerapista.getValue());
            if (!citas.contains(c)) {
                citas.add(c);//agregar cita a la lista
                System.out.println("Nueva Cita:" + c);

                //serializar la lista
                try ( ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathCitas))) {
                    out.writeObject(citas);
                    out.flush();

                    //mostrar informacion
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Resultado de la operación");
                    alert.setContentText("Nueva cita agregada exitosamente");
                    alert.showAndWait();
                    App.setRoot("Citas");
                } catch (IOException ex) {
                    System.out.println("IOException:" + ex.getMessage());
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Error");
                alert.setContentText("No se puede crear la cita porque:\n"
                        + "1. Ya hay otra cita a esa fecha y hora con ese terapista\n"
                        + "2. El cliente ya tiene una cita a esa fecha y hora");
                alert.showAndWait();
            }
        } else if(Validacion.mensaje.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Llenar todos los campos");
            alert.showAndWait();
            Validacion.mensaje = "";
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error");
            alert.setContentText(Validacion.mensaje);
            alert.showAndWait();
            Validacion.mensaje = "";
        }
    }
    
    /**
     * Método que llena las combo boxes con los servicios, empleados y clientes registrados
     */
    public void llenarCampos(){
        ArrayList<Servicio> servicios = Servicio.cargarServicios(App.pathServicios);//cargar la lista del archivo
        ArrayList<Empleado> empleados = Empleado.cargarEmpleados(App.pathEmpleados);//cargar la lista del archivo
        ArrayList<Cliente> clientes = Cliente.cargarClientes(App.pathClientes);//cargar la lista del archivo
        
        cmbServicio.getItems().setAll(servicios);
        cmbTerapista.getItems().setAll(empleados);
        cmbCliente.getItems().setAll(clientes);
    }
}
