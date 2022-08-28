package proyecto;

import modelo.Empleado;
import modelo.Validacion;
import modelo.Atencion;
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
 * @author Ronald Gaibor, Juan Pablo Plúas 
 */

public class NuevaAtencionController implements Initializable{
    
    @FXML private Label lblFecha;
    @FXML private Label lblHora;
    @FXML private Label lblCliente;
    @FXML private TextField txtDuracion;
    @FXML private ComboBox<Empleado> cmbTerapista;
    @FXML private Label lblTitulo;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private Button btnActividad;
    private Cita cita;
        
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Evalúa si se estpa ingresando a la ventana para registrar la atención o se está volviendo de haber realizado la actividad
        if(GameEndController.juegoAcabado){ //Si se está volviendo de realizar la actividad
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información importante");
            alert.setHeaderText(null);
            alert.setContentText("Actividad registrada");
            alert.showAndWait();
            btnActividad.setDisable(true); // Se inhabilita el botón para realizar actividad
            btnGuardar.setDisable(false); // Se habilita el boton de guardado
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información importante");
            alert.setHeaderText(null);
            alert.setContentText("Para registrar la atención, primero debe realizar la actividad");
            alert.showAndWait();
            btnGuardar.setDisable(true); // Se inhabilita el botón hasta que se realice la actividad
        }
        
        llenarCampos(CitasController.citaARegistrar);
        
        btnCancelar.setOnAction(eh -> {// Expresión lambda que determina el comportamiento del botón btnCancelar
            try {
                App.setRoot("Citas");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    /**
    Método que guarda la atención
    */
    @FXML
    public void guardarAtencion(ActionEvent event) {
        ArrayList<Atencion> atenciones = Atencion.cargarAtenciones(App.pathAtenciones);//cargar la lista del archivo
        
        System.out.println("Registrando atención");
        Validacion.validarEntero("Duracion", txtDuracion.getText());
        
        //Valida si es posible guardar la atención
        if(Validacion.mensaje.equals("") && cmbTerapista.getValue() != null && GameEndController.juegoAcabado){ 
            Atencion a = new Atencion(cita,
                    Integer.parseInt(txtDuracion.getText()),
                    cmbTerapista.getValue());
            if (!atenciones.contains(a)) {
                    atenciones.add(a);// Agrega la atención a la lista
                    System.out.println("Nueva Atencion:" + a);

                    //Serializa la lista
                    try ( ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathAtenciones))) {
                        out.writeObject(atenciones);
                        out.flush();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Agregar atención");
                        alert.setHeaderText(null);
                        alert.setContentText("Nueva atención agregada exitosamente");
                        alert.showAndWait();
                        App.setRoot("Atenciones");
                    } catch (IOException ex) {
                        System.out.println("IOException:" + ex.getMessage());
                    }
                }
            GameEndController.juegoAcabado = false;
            
        } else if(Validacion.mensaje.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al intentar guardar atención");
            alert.setHeaderText(null);
            alert.setContentText("Llenar todos los campos");
            alert.showAndWait();
            Validacion.mensaje = "";
        }  else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al intentar guardar atención");
            alert.setHeaderText(null);
            alert.setContentText(Validacion.mensaje);
            alert.showAndWait();
            Validacion.mensaje = "";
        }   
    }
    
    /**
    Método que llena los labels con la infromación de la cita que se registra
    @param c Cita que se está registrando
    */
    public void llenarCampos(Cita c){
        cita = c;
        lblFecha.setText(c.getFecha());
        lblHora.setText(c.getHora());
        lblCliente.setText(c.getCliente().getNombre());
        
        ArrayList<Empleado> empleados = Empleado.cargarEmpleados(App.pathEmpleados);//cargar la lista del archivo
        cmbTerapista.getItems().setAll(empleados);
    }
    
    /**
    Método para realizar la actividad correspondiente a la atención
    */
    @FXML
    private void actividad(ActionEvent event) throws IOException {
        App.setRoot("gameMain");
    }
}
