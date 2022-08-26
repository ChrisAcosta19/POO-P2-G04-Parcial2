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
import modelo.*;

/**
 *
 * @author Ronald Gaibor
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        llenarCampos(CitasController.citaARegistrar);
        
        btnCancelar.setOnAction(eh -> {
            try {
                App.setRoot("Citas");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    @FXML
    public void guardarAtencion(ActionEvent event) {
        ArrayList<Atencion> atenciones = Atencion.cargarAtenciones(App.pathAtenciones);//cargar la lista del archivo
        
        System.out.println("Registrando atención");
        Validacion.validarEntero("Duracion", txtDuracion.getText());
        
        if(Validacion.mensaje.equals("") && cmbTerapista.getValue() != null){
            Atencion a = new Atencion(cita,
                    Integer.parseInt(txtDuracion.getText()),
                    cmbTerapista.getValue());
            if (!atenciones.contains(a)) {
                    atenciones.add(a);//agregar servicio a la lista
                    System.out.println("Nueva Atencion:" + a);

                    //serializar la lista
                    try ( ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathAtenciones))) {
                        out.writeObject(atenciones);
                        out.flush();

                        //mostrar informacion
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Nueva atención agregada exitosamente");
                        alert.showAndWait();
                        App.setRoot("Atenciones");
                    } catch (IOException ex) {
                        System.out.println("IOException:" + ex.getMessage());
                    }
                } else {
                    int ind = atenciones.indexOf(a);
                    atenciones.set(ind, a);

                    try ( ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathAtenciones))) {
                        out.writeObject(atenciones);
                        out.flush();

                        //mostrar informacion
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Atención editada exitosamente");

                        alert.showAndWait();
                        App.setRoot("Atenciones");

                    } catch (IOException ex) {
                        System.out.println("IOException:" + ex.getMessage());
                    }
                }
        } else if(Validacion.mensaje.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Llenar todos los campos");
            alert.showAndWait();
            Validacion.mensaje = "";
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText(Validacion.mensaje);
            alert.showAndWait();
            Validacion.mensaje = "";
        }
    }
    
    public void llenarCampos(Cita c){
        cita = c;
        lblFecha.setText(c.getFecha());
        lblHora.setText(c.getHora());
        lblCliente.setText(c.getCliente().getNombre());
        
        ArrayList<Empleado> empleados = Empleado.cargarEmpleados(App.pathEmpleados);//cargar la lista del archivo
        cmbTerapista.getItems().setAll(empleados);
    }

    @FXML
    private void actividad(ActionEvent event) throws IOException {
        App.setRoot("gameMain");
    }
}

