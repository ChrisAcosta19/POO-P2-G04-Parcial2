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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import modelo.Cliente;
import modelo.Game;

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
    @FXML private ImageView regresar;
    public static Cliente clienteSeleccionado;
    @FXML private ImageView agregar;
    @FXML private ImageView editar;
    @FXML private ImageView actividades;
    @FXML private ImageView icon;
    @FXML private Label lblTitulo;
    
    public static boolean juegosExists; 
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
        
        App.setImage("iconClientes",App.pathImg,icon);
        App.setImage("regresar",App.pathImg,regresar);
        App.setImage("agregarPersona",App.pathImg,agregar);
        App.setImage("editarPersona",App.pathImg,editar);
        App.setImage("actividades",App.pathImg,actividades);
        regresar.setOnMouseClicked(eh -> {
            try {
                App.setRoot("primary");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    @FXML
    private void agregarCliente(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("nuevoCliente.fxml"));//no tiene el controlador especificado
        VBox root = (VBox) fxmlLoader.load();
        //luego que el fxml ha sido cargado puedo utilizar el controlador para realizar cambios}
        App.changeRoot(root);
    }
    
    @FXML
    private void editarCliente(MouseEvent event) throws IOException {
        Cliente cl = (Cliente) tvClientes.getSelectionModel().getSelectedItem();
        if(cl == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al intentar editar cliente");
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
    private void mostrarActividades(MouseEvent event) throws IOException{
        juegosExists=true;
        Cliente cl = (Cliente) tvClientes.getSelectionModel().getSelectedItem();
        clienteSeleccionado=cl;
        ActividadesController.listaResultados = ActividadesController.cargarLista();
        if(cl == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al intentar consultar actividades");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar un cliente");
            alert.showAndWait();
        }else if(!juegosExists){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("D:");
            alert.setHeaderText(null);
            alert.setContentText("El cliente seleccionado no tiene actividades realizadas");
            alert.showAndWait();
        } else {
            App.setRoot("actividades");
        }
    }

    @FXML
    private void mostarInfo(MouseEvent event) {
        
    }

}