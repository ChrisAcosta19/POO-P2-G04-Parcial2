package espol.edu.ec.borradorproyectofx;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.*;

public class AtencionesController implements Initializable {


    @FXML
    private TableView<Atencion> tvAtenciones;
    @FXML
    private TableColumn colCliente;
    @FXML
    private TableColumn colTerapista;
    @FXML
    private TableColumn colDuracion;
    @FXML
    private TableColumn colFecha;
    @FXML
    private TableColumn colHora;
    @FXML
    private Button btnRegresar;
    @FXML
    private TextField txtCedCliente;
    @FXML
    private TextField txtCedEmpleado;
    @FXML
    private TextField txtFecha;
    @FXML
    private Button btnConsCliente;
    @FXML
    private Button btnConsEmpleado;
    @FXML
    private Button btnConsFecha;
    @FXML
    private Button btnBorrarFiltros;
    ArrayList<Atencion> atenciones = Atencion.cargarAtenciones(App.pathAtenciones);
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cita"));
        colTerapista.setCellValueFactory(new PropertyValueFactory<>("empleadoAtencion"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("cita"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("cita"));
        tvAtenciones.getItems().setAll(atenciones);
        
        btnRegresar.setOnAction(eh -> {
            try {
                App.setRoot("primary");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        btnConsCliente.setOnAction(eh -> {
            txtFecha.setText("");
            txtCedEmpleado.setText("");
            String cedula = txtCedCliente.getText();
            Cliente cliente = new Cliente(cedula);
            ArrayList<Atencion> atencionesFiltradas = new ArrayList<>();
            for(Atencion atencion : atenciones){
                if(cliente.equals(atencion.getCita().getCliente())){
                    atencionesFiltradas.add(atencion);
                }
            }
            tvAtenciones.getItems().setAll(atencionesFiltradas);
        });
        
        btnConsEmpleado.setOnAction(eh -> {
            txtCedCliente.setText("");
            txtFecha.setText("");
            String cedula = txtCedEmpleado.getText();
            Empleado empleado = new Empleado(cedula);
            ArrayList<Atencion> atencionesFiltradas = new ArrayList<>();
            for(Atencion atencion : atenciones){
                if(empleado.equals(atencion.getCita().getEncargadoServicio())){
                    atencionesFiltradas.add(atencion);
                }
            }
            tvAtenciones.getItems().setAll(atencionesFiltradas);
        });
        
        btnConsFecha.setOnAction(eh -> {
            txtCedCliente.setText("");
            txtCedEmpleado.setText("");
            String fecha = txtFecha.getText();
            ArrayList<Atencion> atencionesFiltradas = new ArrayList<>();
            for(Atencion atencion : atenciones){
                if(fecha.equals(atencion.getCita().getFecha())){
                    atencionesFiltradas.add(atencion);
                }
            }
            tvAtenciones.getItems().setAll(atencionesFiltradas);
        });
        
        btnBorrarFiltros.setOnAction(eh -> {
            txtFecha.setText("");
            txtCedCliente.setText("");
            txtCedEmpleado.setText("");
            tvAtenciones.getItems().setAll(atenciones);
        });
    }    
}
