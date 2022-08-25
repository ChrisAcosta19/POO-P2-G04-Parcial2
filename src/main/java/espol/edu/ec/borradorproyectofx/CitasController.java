/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package espol.edu.ec.borradorproyectofx;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import modelo.Atencion;
import modelo.Cita;
import modelo.Cliente;
/**
 * FXML Controller class
 *
 * @author chris
 */
public class CitasController implements Initializable {


    @FXML private TableView<Cita> tvCitas;
    @FXML private TableColumn colCliente;
    @FXML private TableColumn colTerapista;
    @FXML private TableColumn colServicio;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colHora;
    @FXML private TextField txtCedCliente;
    @FXML private TextField txtFecha;
    @FXML private Button btnConsCliente;
    @FXML private Button btnConsFecha;
    @FXML private Button btnBorrarFiltros;
    @FXML private ImageView regresar;
    @FXML private ImageView icon;
    @FXML private ImageView agregar;
    @FXML private ImageView registrar;
    @FXML
    private Label lblTitulo;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colTerapista.setCellValueFactory(new PropertyValueFactory<>("encargadoServicio"));
        colServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        tvCitas.getItems().setAll(cargarCitasPendientes());
        
        App.setImage("iconCitas",App.pathImg,icon);
        App.setImage("regresar",App.pathImg,regresar);
        App.setImage("agregarCita",App.pathImg,agregar);
        App.setImage("registrar",App.pathImg,registrar);
        
        regresar.setOnMouseClicked(eh -> {
            try {
                App.setRoot("primary");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        btnConsCliente.setOnAction(eh -> {
            txtFecha.setText("");
            String cedula = txtCedCliente.getText();
            Cliente cliente = new Cliente(cedula);
            ArrayList<Cita> pendientes = new ArrayList<>();
            for(Cita cita : cargarCitasPendientes()){
                if(cliente.equals(cita.getCliente())){
                    pendientes.add(cita);
                }
            }
            tvCitas.getItems().setAll(pendientes);
        });
        
        btnConsFecha.setOnAction(eh -> {
            txtCedCliente.setText("");
            String fecha = txtFecha.getText();
            ArrayList<Cita> pendientes = new ArrayList<>();
            for(Cita cita : cargarCitasPendientes()){
                if(fecha.equals(cita.getFecha())){
                    pendientes.add(cita);
                }
            }
            tvCitas.getItems().setAll(pendientes);
        });
        
        btnBorrarFiltros.setOnAction(eh -> {
            txtFecha.setText("");
            txtCedCliente.setText("");
            tvCitas.getItems().setAll(cargarCitasPendientes());
        });
        
        agregar.setOnMouseClicked(eh -> {
            try {
                App.setRoot("nuevaCita");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }    
    
    public ArrayList<Cita> cargarCitasPendientes(){
        ArrayList<Cita> citas = Cita.cargarCitas(App.pathCitas);
        ArrayList<Atencion> atenciones = Atencion.cargarAtenciones(App.pathAtenciones);
        
        ArrayList<Cita> atendidas = new ArrayList<>();
        for(Atencion atencion: atenciones){
            atendidas.add(atencion.getCita());
        }
        
        ArrayList<Cita> pendientes = new ArrayList<>();
        for(Cita cita: citas){
            if(!atendidas.contains(cita)){
                pendientes.add(cita);
            }
        }
        return pendientes;
    }

    @FXML
    private void agregarCita(MouseEvent event) {
    }

    @FXML
    private void registrarAtencion(MouseEvent event) {
        Cita c = (Cita) tvCitas.getSelectionModel().getSelectedItem();
        System.out.println("Registrando atención");
        if(c == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Debe seleccionar una cita de la tabla");
            alert.showAndWait();
        } else {
            try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("nuevaAtencion.fxml"));
            VBox root = (VBox) fxmlLoader.load();
            NuevaAtencionController ct = (NuevaAtencionController) fxmlLoader.getController();
            ct.llenarCampos(c);
            App.changeRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
