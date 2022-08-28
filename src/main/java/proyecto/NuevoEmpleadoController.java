package proyecto;

import modelo.Empleado;
import modelo.Validacion;
import modelo.Servicio;
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
import java.io.*;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class NuevoEmpleadoController implements Initializable {

    @FXML private TextField txtCedula;
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private RadioButton rbA;
    @FXML private ToggleGroup estado;
    @FXML private RadioButton rbI;
    @FXML private ComboBox<Servicio> cmbServicios;
    @FXML private Button btnAgregarServicio;
    @FXML private Button btnEliminarServicio;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private TableView<Servicio> tvServicios;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colDuracion;
    @FXML private TableColumn colPrecio;
    @FXML private TableColumn colEstado;
    @FXML private Label lblTitulo;
    
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
            alert.setTitle("Error al intentar agregar servicio");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar un servicio de la lista");
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
            alert.setTitle("Error al intentar editar servicio");
            alert.setHeaderText(null);
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
        String estado = "";
        try{
            RadioButton selectedRadioButton = (RadioButton) this.estado.getSelectedToggle();
            estado = selectedRadioButton.getText();
            System.out.println(estado);
        }catch(Exception e){
            Validacion.mensaje = "Debe seleccionar un estado\n";
        }
        boolean estadoBoolean = estado.equals("Activo");
        String cedula = txtCedula.getText();
        Validacion.validarEntero("Cedula",cedula);
        String nombre = txtNombre.getText();
        Validacion.validarNombre("Empleado",nombre);
        String telefono = txtTelefono.getText();
        Validacion.validarEntero("Telefono",telefono);
        String email = txtEmail.getText();
        Validacion.validarEmail("Empleado",email);
        Empleado e = new Empleado(cedula,nombre,telefono,email,estadoBoolean);
        e.setListaServicios(servicios);
        
        if(empleado == null){
            Validacion.validarPersona("Empleado",e, null);
        }else{
            Validacion.validarPersona("Empleado",e, empleado); 
        }
        
        if(Validacion.mensaje.equals("") && empleado == null){
            empleados.add(e);
            System.out.println("Nuevo Empleado:" + e);
            //serializar la lista
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathEmpleados))) {
                out.writeObject(empleados);
                out.flush();

                //mostrar informacion
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Agregar Empleado");
                alert.setHeaderText(null);
                alert.setContentText("Nuevo empleado agregado exitosamente");

                alert.showAndWait();
                App.setRoot("Empleados");

            } catch (IOException ex) {
                System.out.println("IOException:" + ex.getMessage());
            }
        } else if (Validacion.mensaje.equals("") && empleado != null) {
            int indice = empleados.indexOf(empleado);
            empleados.set(indice, e);

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathEmpleados))) {
                out.writeObject(empleados);
                out.flush();

                //mostrar informacion
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Editar Empleado");
                alert.setHeaderText(null);
                alert.setContentText("Empleado editado exitosamente");

                alert.showAndWait();
                App.setRoot("Empleados");

            } catch (IOException ex) {
                System.out.println("IOException:" + ex.getMessage());
            }
            empleado = null;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al intentar guardar empleado");
            alert.setHeaderText(null);
            alert.setContentText(Validacion.mensaje);
            alert.showAndWait();
            Validacion.mensaje = "";
        }
    }
    
    /**
     * metodo para colocar los datos del empleado a editar
     * @param e empleado cuyos datos se van a editar
     */
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