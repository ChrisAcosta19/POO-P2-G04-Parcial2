package proyecto;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class ServiciosController implements Initializable {

    @FXML private TableView<Servicio> tvServicios;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colDuracion;
    @FXML private TableColumn colPrecio;
    @FXML private TableColumn colEstado;
    @FXML private ImageView regresar;
    @FXML private ImageView agregar;
    @FXML private ImageView editar;
    @FXML private ImageView eliminar;
    @FXML private ImageView icon;
    @FXML
    private Label lblTitulo;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tvServicios.getItems().setAll(Servicio.cargarServicios(App.pathServicios));
        
        App.setImage("iconServicios",App.pathImg,icon);
        App.setImage("regresar",App.pathImg,regresar);
        App.setImage("agregar",App.pathImg,agregar);
        App.setImage("editar",App.pathImg,editar);
        App.setImage("eliminar",App.pathImg,eliminar);
        regresar.setOnMouseClicked(eh -> {
            try {
                App.setRoot("primary");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    @FXML
    private void agregarServicio(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("nuevoServicio.fxml"));//no tiene el controlador especificado
        VBox root = (VBox) fxmlLoader.load();
        //luego que el fxml ha sido cargado puedo utilizar el controlador para realizar cambios}
        App.changeRoot(root);
    }

    @FXML
    private void editarServicio(MouseEvent event) throws IOException {
        Servicio s = (Servicio) tvServicios.getSelectionModel().getSelectedItem();
        if(s == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al intentar editar un servicio");
            alert.setHeaderText(null);
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
    private void eliminarServicio(MouseEvent event) {
        Servicio s = (Servicio) tvServicios.getSelectionModel().getSelectedItem();
        if(s == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al intentar eliminar un servicio");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar un servicio");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar Servicio");
            alert.setHeaderText(null);
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
                    alert.setTitle("Eliminar Servicio");
                    alert.setHeaderText(null);
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