package espol.edu.ec.borradorproyectofx;

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
import modelo.Empleado;
import javafx.scene.control.Label;
import modelo.Servicio;
import modelo.*;

/**
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
    
    @FXML
    public void crearCita(ActionEvent event) {
        System.out.println("Guardando cita");
        Validacion.validarFecha(txtFecha.getText());
        Validacion.validarHora(txtHora.getText());
        
        if(Validacion.mensaje.equals("") && cmbCliente.getValue()!= null &&cmbServicio.getValue() != null && cmbTerapista.getValue() != null){
            
            
            Cita c = new Cita(txtFecha.getText(),
                              txtHora.getText(),
                              cmbCliente.getValue(),cmbServicio.getValue(),cmbTerapista.getValue());
            if (!citas.contains(c)) {
                citas.add(c);//agregar servicio a la lista
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
                int ind = citas.indexOf(c);
                citas.set(ind, c);

                try ( ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathCitas))) {
                    out.writeObject(citas);
                    out.flush();

                    //mostrar informacion
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Resultado de la operación");
                    alert.setContentText("Cita editada exitosamente");

                    alert.showAndWait();
                    App.setRoot("Citas");

                } catch (IOException ex) {
                    System.out.println("IOException:" + ex.getMessage());
                }
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

    public void llenarCampos(){
        ArrayList<Servicio> servicios = Servicio.cargarServicios(App.pathServicios);//cargar la lista del archivo
        ArrayList<Empleado> empleados = Empleado.cargarEmpleados(App.pathEmpleados);//cargar la lista del archivo
        ArrayList<Cliente> clientes = Cliente.cargarClientes(App.pathClientes);//cargar la lista del archivo
        
        cmbServicio.getItems().setAll(servicios);
        cmbTerapista.getItems().setAll(empleados);
        cmbCliente.getItems().setAll(clientes);
    }
}

