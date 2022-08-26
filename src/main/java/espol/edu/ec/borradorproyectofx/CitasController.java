/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package espol.edu.ec.borradorproyectofx;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
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
    @FXML private Label lblTitulo;
    private static ArrayList<Cita> citas;
    private static ArrayList<Cita> citasPendientes;
    public static Cita citaARegistrar;
    @FXML private ImageView regresar;
    @FXML private ImageView icon;
    @FXML private ImageView agregar;
    @FXML private ImageView eliminar;
    @FXML private ImageView registrar;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        citas = Cita.cargarCitas(App.pathCitas);
        citasPendientes = cargarCitasPendientes();
        
        App.setImage("iconCitas",App.pathImg,icon);
        App.setImage("regresar",App.pathImg,regresar);
        App.setImage("agregarCita",App.pathImg,agregar);
        App.setImage("eliminarCita",App.pathImg,eliminar);
        App.setImage("registrar",App.pathImg,registrar);
        
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colTerapista.setCellValueFactory(new PropertyValueFactory<>("encargadoServicio"));
        colServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        tvCitas.getItems().setAll(citas);
        
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
            tvCitas.getItems().setAll(citas);
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
    private void eliminarCita(MouseEvent event) throws IOException {
        Cita c = (Cita) tvCitas.getSelectionModel().getSelectedItem();
        if(c == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Debe seleccionar una cita de la tabla");
            alert.showAndWait();
        }else{
            if (citasPendientes.contains(c)){ //si la cita está pendiente, entonces no tiene atención
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Eliminar Cita");
                alert.setContentText("¿Está seguro de que desea eliminar esta cita?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    citas.remove(c);
                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathCitas))){
                        out.writeObject(citas);
                        out.flush();

                        //mostrar informacion
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("Resultado de la operación");
                        alert.setContentText("Cita eliminada exitosamente");
                        alert.showAndWait();
                        App.setRoot("Citas");
                    } catch (IOException ex) {
                        System.out.println("IOException:" + ex.getMessage());
                    }
                } else {
                    // ... user chose CANCEL or closed the dialog
                }
            } else {
                //mostrar informacion
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Error");
                alert.setContentText("No se puede eliminar una cita con atención registrada");
                alert.showAndWait();
                try {
                    App.setRoot("Citas");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    @FXML
    public void registrarAtencion(MouseEvent event) throws IOException{
        Cita c = (Cita) tvCitas.getSelectionModel().getSelectedItem();
        System.out.println("Registrando atención");
        if(c == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al intentar registrar atención");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar una cita de la tabla");
            alert.showAndWait();
        } else if (citasPendientes.contains(c)){
            citaARegistrar = c;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("nuevaAtencion.fxml"));
                VBox root = (VBox) fxmlLoader.load();
                App.changeRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al intentar registrar atención");
            alert.setHeaderText(null);
            alert.setContentText("La cita seleccionada ya está registrada");
            alert.showAndWait();
            try {
                App.setRoot("Citas");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @FXML
    private void agregarCita (MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("nuevaCita.fxml"));
        VBox root = (VBox) fxmlLoader.load();
        App.changeRoot(root);
    }

    
}