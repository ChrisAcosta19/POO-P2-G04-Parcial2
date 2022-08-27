
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
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import modelo.*;

/**
 * FXML Controller class
 *
 * @author Juan Pablo Plúas
 */
public class ActividadesController implements Initializable {

    @FXML private Label lblSelected;
    @FXML private Button btnReplay;
    @FXML private TableColumn colActividad;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colEjercicios;
    @FXML private TableColumn colFallos;
    @FXML private Button btnDetails;
    @FXML private FlowPane paneCentral;
    @FXML private HBox paneBottom;
    @FXML private ImageView regresar;
    @FXML private TableView<Game> tableAct;
    @FXML private TableColumn colTiempo;
    @FXML private Label lblTitulo;
    
    public static ArrayList<Game> listaResultados;
    public static Game replayGame;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnReplay.setDisable(true);     
        lblSelected.setText(ClientesController.clienteSeleccionado.getNombre());
        App.setImage("regresar",App.pathImg,regresar);
        
        for(Game g:listaResultados){
            System.out.println(g);
        }
        
        colActividad.setCellValueFactory(new PropertyValueFactory<>("actividad"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEjercicios.setCellValueFactory(new PropertyValueFactory<>("numEjercicios"));
        colFallos.setCellValueFactory(new PropertyValueFactory<>("fallos"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempoEnFormato"));
          
        tableAct.getItems().setAll(listaResultados);
        
                
        regresar.setOnMouseClicked(ev ->{
            try {
                App.setRoot("clientes");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
    }    
    
    private void mostrarTabla(){
        paneCentral.getChildren().clear();
        paneBottom.getChildren().clear();
        
        btnReplay.setDisable(true);
        
        colActividad.setCellValueFactory(new PropertyValueFactory<>("actividad"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEjercicios.setCellValueFactory(new PropertyValueFactory<>("numEjercicios"));
        colFallos.setCellValueFactory(new PropertyValueFactory<>("fallos"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempoEnFormato"));
        
        for (Game g:listaResultados){
            System.out.println(g);
        }
        
        tableAct.getItems().setAll(listaResultados);
        paneCentral.getChildren().addAll(tableAct);
        paneBottom.getChildren().add(btnDetails);
        regresar.setOnMouseClicked(ev ->{
            try {
                App.setRoot("clientes");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
    }
    
    @FXML
    private void mostrarDetalles (ActionEvent event) throws IOException{
        Game g = (Game) tableAct.getSelectionModel().getSelectedItem();
        System.out.println(g);
        if(g == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al intentar consultar detalles de sesión de juego");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar una sesión de juego");
            alert.showAndWait();
        }else{
            if(!g.getFecha().equals("n/a")){// solo si es la sesión original, se puede volver a jugar
            btnReplay.setDisable(false);}
            
            paneCentral.getChildren().clear();
            paneBottom.getChildren().clear();
            
            
            int indiceGame=listaResultados.indexOf(g);
            
            System.out.println("------------------");
            System.out.println(indiceGame);
            System.out.println("------------------");
            
            BufferedReader lector=new BufferedReader(new FileReader("archivos/registro/"+ClientesController.clienteSeleccionado.getCedula()+"/GamesDetalles.txt"));
            
            Stream<String> lineas= lector.lines();
            String[] lineasArray = lineas.toArray(size -> new String[size]);
            
            for(String a:lineasArray){
                System.out.println(a);
            }
            
            String detalles=lineasArray[indiceGame];
            String[] detallesTotal=detalles.split(";");
            String[] detallesGeneral=detallesTotal[0].split(",");
            
            for(String a:detallesGeneral){
                System.out.println(a);
            }
            
            for(int x=1;x<detallesTotal.length;x++){
                String[] detallesEjercicios=detallesTotal[x].split(",");
                for(String a:detallesEjercicios){
                    System.out.println(a);
            } 
           }
            /*VBox paneGeneral=new VBox();
            for(String a:detallesGeneral){
                Label lbl=new Label(a);
                paneGeneral.getChildren().add(lbl);
            }
            paneCentral.getChildren().add(paneGeneral);
            paneGeneral.setAlignment(Pos.CENTER);
            paneGeneral.setPadding(new Insets(20));*/
            
            // coloca en labels el detalle de cada ejercicio de la actividad            
           for(int x=1;x<detallesTotal.length;x++){
               VBox pane=new VBox();
               Label lbl=new Label("Ejercicio "+x);
               pane.getChildren().addAll(lbl);
               String[] detallesEjercicios=detallesTotal[x].split(",");
               for(String a:detallesEjercicios){
                Label lbl2=new Label(a);
                pane.getChildren().add(lbl2);
            } 
               paneCentral.getChildren().add(pane);
               pane.setAlignment(Pos.CENTER);
               pane.setPadding(new Insets(20));
           }
                     
            
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
            alert.setTitle("Error al intentar volver a jugar sesión");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar una sesión de juego");
            alert.showAndWait();
        }else {
                ArrayList<Game> listaActividades=Game.cargarActividades(ClientesController.clienteSeleccionado.getCedula());
                int indiceGame=listaResultados.indexOf(g);
                Game gSelected=listaActividades.get(indiceGame);
                replayGame=gSelected;
                App.setRoot("game");
            
        }
    }
    
    public static ArrayList<Game> cargarLista(){
        return Game.cargarResultados(ClientesController.clienteSeleccionado.getCedula());
    }
    
}
