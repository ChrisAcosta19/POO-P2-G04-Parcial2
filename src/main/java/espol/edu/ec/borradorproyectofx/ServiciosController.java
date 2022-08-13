/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package espol.edu.ec.borradorproyectofx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import modelo.Servicio;
import java.io.*;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class ServiciosController implements Initializable {

    @FXML
    private TableView<Servicio> tvServicios;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colDuracion;
    @FXML
    private TableColumn colPrecio;
    @FXML
    private TableColumn colEstado;
    
    @FXML
    private Button btnRegresar;

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tvServicios.getItems().setAll(Servicio.cargarServicios(App.pathServicios));
        
        btnRegresar.setOnAction(eh -> {
            try {
                App.setRoot("primary");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    @FXML
    private void mostrarVentana(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("nuevoServicio.fxml"));//no tiene el controlador especificado
        VBox root = (VBox) fxmlLoader.load();
        //luego que el fxml ha sido cargado puedo utilizar el controlador para realizar cambios}
        App.changeRoot(root);
    }

    @FXML
    private void editarServicio(ActionEvent event) throws IOException {
        Servicio s = (Servicio) tvServicios.getSelectionModel().getSelectedItem();
        if(s == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Debe seleccionar un servicio");
            alert.showAndWait();
        }else{
            NuevoServicioController.servicio = s;
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("nuevoServicio.fxml"));
            VBox root = (VBox) fxmlLoader.load();
            NuevoServicioController ct = (NuevoServicioController) fxmlLoader.getController();
            ct.llenarCampos(s);
            App.changeRoot(root);
        }
    }

    @FXML
    private void eliminarServicio(ActionEvent event) {
        Servicio s = (Servicio) tvServicios.getSelectionModel().getSelectedItem();
        if(s == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Debe seleccionar un servicio");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Eliminar Servicio");
            alert.setContentText("¿Está seguro de que desea eliminar este servicio?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                ArrayList<Servicio> servicios = Servicio.cargarServicios(App.pathServicios);
                servicios.remove(s);
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathServicios))){
                    out.writeObject(servicios);
                    out.flush();

                    //mostrar informacion
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Resultado de la operación");
                    alert.setContentText("Servicio eliminado exitosamente");
                    alert.showAndWait();
                    App.setRoot("Servicios");
                } catch (IOException ex) {
                    System.out.println("IOException:" + ex.getMessage());
                }
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
    }
}