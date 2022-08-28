/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package espol.edu.ec.borradorproyectofx;

import modelo.Empleado;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class EmpleadosController implements Initializable {


    @FXML private TableView<Empleado> tvEmpleados;
    @FXML private TableColumn colCedula;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colEmail;
    @FXML private TableColumn colEstado;
    @FXML private TableColumn<Empleado, Void> colServicios;
    @FXML private ImageView regresar;
    @FXML private ImageView agregar;
    @FXML private ImageView editar;
    @FXML private ImageView eliminar;
    @FXML private ImageView icon;
    @FXML
    private Label lblTitulo;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        cargarServicios();
        tvEmpleados.getItems().setAll(Empleado.cargarEmpleados(App.pathEmpleados));
        
        App.setImage("iconEmpleados",App.pathImg,icon);
        App.setImage("regresar",App.pathImg,regresar);
        App.setImage("agregarPersona",App.pathImg,agregar);
        App.setImage("editarPersona",App.pathImg,editar);
        App.setImage("eliminarPersona",App.pathImg,eliminar);
        regresar.setOnMouseClicked(eh -> {
            try {
                App.setRoot("primary");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }    
    
    private void cargarServicios(){
        Callback<TableColumn<Empleado, Void>, TableCell<Empleado, Void>> cellFactory = new Callback<TableColumn<Empleado, Void>, TableCell<Empleado, Void>>() {
            @Override
            public TableCell<Empleado, Void> call(final TableColumn<Empleado, Void> param) {
                TableCell<Empleado, Void> cell = new TableCell<Empleado, Void>() {
                   
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            //hbox para ubicar los botones
                            HBox hbOpciones = new HBox(5);
                            //recuperar el empleado de la fila
                            Empleado emp = getTableView().getItems().get(getIndex());
                            //ComboBox para la lista de servicios del empleado
                            ComboBox cmbServicios = new ComboBox();
                            cmbServicios.getItems().setAll(emp.getListaServicios());
                            //se agregan combobox al hbox
                            hbOpciones.getChildren().addAll(cmbServicios);
                            //se ubica hbox en la celda
                            setGraphic(hbOpciones);
                        }
                    }
                };
                return cell;
            }
        };

        colServicios.setCellFactory(cellFactory);
    }
    
    @FXML
    private void agregarEmpleado(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("nuevoEmpleado.fxml"));//no tiene el controlador especificado
        VBox root = (VBox) fxmlLoader.load();
        //luego que el fxml ha sido cargado puedo utilizar el controlador para realizar cambios}
        App.changeRoot(root);
    }

    @FXML
    private void editarEmpleado(MouseEvent event) throws IOException {
        Empleado e = (Empleado) tvEmpleados.getSelectionModel().getSelectedItem();
        if(e == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al intentar editar empleado");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar un empleado");
            alert.showAndWait();
        }else{
            NuevoEmpleadoController.empleado = e;
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("nuevoEmpleado.fxml"));
            VBox root = (VBox) fxmlLoader.load();
            NuevoEmpleadoController ct = (NuevoEmpleadoController) fxmlLoader.getController();
            ct.llenarCampos(e);
            App.changeRoot(root);
        }
    }

    @FXML
    private void eliminarEmpleado(MouseEvent event) throws IOException {
        Empleado e = (Empleado) tvEmpleados.getSelectionModel().getSelectedItem();
        if(e == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al intentar eliminar empleado");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar un empleado");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar Empleado");
            alert.setHeaderText(null);
            alert.setContentText("¿Está seguro de que desea eliminar este empleado?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                ArrayList<Empleado> empleados = Empleado.cargarEmpleados(App.pathEmpleados);
                empleados.remove(e);
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathEmpleados))){
                    out.writeObject(empleados);
                    out.flush();

                    //mostrar informacion
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Eliminar Empleado");
                    alert.setHeaderText(null);
                    alert.setContentText("Empleado eliminado exitosamente");
                    alert.showAndWait();
                    App.setRoot("Empleados");
                } catch (IOException ex) {
                    System.out.println("IOException:" + ex.getMessage());
                }
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
    }
}