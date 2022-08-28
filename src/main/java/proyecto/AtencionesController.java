package proyecto;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
/**
 * FXML Controller class
 *
 * @author Ronald Gaibor
 */
public class AtencionesController implements Initializable {


    @FXML private TableView<Atencion> tvAtenciones;
    @FXML private TableColumn colCliente;
    @FXML private TableColumn colTerapista;
    @FXML private TableColumn colDuracion;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colHora;
    @FXML private TextField txtCedCliente;
    @FXML private TextField txtCedEmpleado;
    @FXML private TextField txtFecha;
    @FXML private Button btnConsCliente;
    @FXML private Button btnConsEmpleado;
    @FXML private Button btnConsFecha;
    @FXML private Button btnBorrarFiltros;
    @FXML private Label lblTitulo;
    @FXML private ImageView regresar;
    @FXML private ImageView icon;
    
    ArrayList<Atencion> atenciones = Atencion.cargarAtenciones(App.pathAtenciones);
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        App.setImage("iconAtenciones",App.pathImg,icon);
        App.setImage("regresar",App.pathImg,regresar);
        
        colCliente.setCellValueFactory(new PropertyValueFactory<>("clienteCita"));
        colTerapista.setCellValueFactory(new PropertyValueFactory<>("empleadoAtencion"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaCita"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("horaCita"));
        tvAtenciones.getItems().setAll(atenciones);
        
        regresar.setOnMouseClicked(eh -> {
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
