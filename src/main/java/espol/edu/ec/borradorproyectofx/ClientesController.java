/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package espol.edu.ec.borradorproyectofx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import modelo.Cliente;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class ClientesController implements Initializable {

    @FXML private TableView<Cliente> tvClientes;
    @FXML private TableColumn colCedula;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colEmail;
    @FXML private TableColumn<Cliente, Void> colRepresentante;
    @FXML  private Button btnRegresar;
    @FXML
    private ImageView regresar;
    public static Cliente cl;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRepresentante.setCellValueFactory(new PropertyValueFactory<>("datosRepresentante"));
        tvClientes.getItems().setAll(Cliente.cargarClientes(App.pathClientes));
        
        regresar.setOnMouseClicked(eh -> {
            try {
                App.setRoot("primary1");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    @FXML
    private void mostrarVentana(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("nuevoCliente.fxml"));//no tiene el controlador especificado
        VBox root = (VBox) fxmlLoader.load();
        //luego que el fxml ha sido cargado puedo utilizar el controlador para realizar cambios}
        App.changeRoot(root);
    }

    @FXML
    private void editarCliente(ActionEvent event) throws IOException {
        Cliente cl = (Cliente) tvClientes.getSelectionModel().getSelectedItem();
        if(cl == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar un cliente");
            alert.showAndWait();
        }else{
            NuevoClienteController.cliente = cl;
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("nuevoCliente.fxml"));
            VBox root = (VBox) fxmlLoader.load();
            NuevoClienteController ct = (NuevoClienteController) fxmlLoader.getController();
            ct.llenarCampos(cl);
            App.changeRoot(root);
        }
    }

    @FXML
    private void mostrarActividades(ActionEvent event) throws IOException{
        Cliente cl = (Cliente) tvClientes.getSelectionModel().getSelectedItem();
        if(cl == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar un cliente");
            alert.showAndWait();
        }else{
            clienteSeleccionado=cl;
            App.setRoot("actividades");
        }
    }
}