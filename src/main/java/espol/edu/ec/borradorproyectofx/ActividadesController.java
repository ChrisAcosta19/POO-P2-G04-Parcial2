/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package espol.edu.ec.borradorproyectofx;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import modelo.*;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class ActividadesController implements Initializable {

    @FXML
    private Label lblSelected;
    @FXML
    private Button btnReplay;
    @FXML
    private TableColumn colActividad;
    @FXML
    private TableColumn colFecha;
    @FXML
    private TableColumn colEjercicios;
    @FXML
    private TableColumn colFallos;
    @FXML
    private Button btnDetails;
    @FXML
    private AnchorPane paneCentral;
    @FXML
    private HBox paneBottom;
    @FXML
    private ImageView regresar;
    @FXML
    private TableView<Game> tableAct;
    @FXML
    private TableColumn colTiempo;
    
    ArrayList<Game> lista=Game.cargarActividades(ClientesController.clienteSeleccionado.getCedula());
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        lblSelected.setText(ClientesController.clienteSeleccionado.getNombre());
        
        colActividad.setCellValueFactory(new PropertyValueFactory<>("actividad"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEjercicios.setCellValueFactory(new PropertyValueFactory<>("numEjercicios"));
        colFallos.setCellValueFactory(new PropertyValueFactory<>("fallos"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempoEnFormato"));
        ArrayList<Game> lista=Game.cargarActividades(ClientesController.clienteSeleccionado.getCedula());   
        tableAct.getItems().setAll(lista);
        
                
        regresar.setOnMouseClicked(ev ->{
            try {
                App.setRoot("clientes");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
    }    
    
    private void mostrarTabla(){
                
        colActividad.setCellValueFactory(new PropertyValueFactory<>("actividad"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEjercicios.setCellValueFactory(new PropertyValueFactory<>("numEjercicios"));
        colFallos.setCellValueFactory(new PropertyValueFactory<>("fallos"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempoEnFormato"));
        
        for (Game g:lista){
            System.out.println(g);
        }
        tableAct.getItems().setAll(lista);
        paneCentral.getChildren().addAll(tableAct);
        paneBottom.getChildren().add(btnDetails);
        
    }
    
    @FXML
    private void mostrarDetalles (ActionEvent event) throws IOException{
        Game g = (Game) tableAct.getSelectionModel().getSelectedItem();
        if(g == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar una sesión de juego");
            alert.showAndWait();
        }else{
            paneCentral.getChildren().clear();
            paneBottom.getChildren().clear();
            int indiceGame=lista.indexOf(g);
            BufferedReader lector=new BufferedReader(new FileReader("archivos/"+ClientesController.clienteSeleccionado.getCedula()+"/GamesDetalles.txt"));
            for(int x=0;x<indiceGame;x++){
                lector.readLine();
            }
            String detalles=lector.readLine();
            String[] detallesLista=detalles.split(",");
            
            
        }
                
        regresar.setOnMouseClicked(ev ->{
            mostrarTabla();
        });
        
    }

    @FXML
    private void replay(ActionEvent event) throws IOException {
        Game g = (Game) tableAct.getSelectionModel().getSelectedItem();
        if(g == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar una sesión de juego");
            alert.showAndWait();
        }else{
            int indiceGame=lista.indexOf(g);
            
            
        }
    }
    
    
}
