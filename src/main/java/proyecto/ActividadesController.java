
package proyecto;

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
        lblSelected.setText(ClientesController.clienteSeleccionado.getNombre()); //LLena el label con el nombre del cliente seleccionado
        App.setImage("regresar",App.pathImg,regresar);
        
        for(Game g:listaResultados){
            System.out.println(g);
        }
        
        //Lena la tabla con los valores de los objetos Game con los resultados de las sesiones
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
    
    /**
    Método que muestra la tabla de actividades del cliente seleccionado
    */
    private void mostrarTabla(){
        paneCentral.getChildren().clear();
        paneBottom.getChildren().clear();
        
        btnReplay.setDisable(true);
        
        //Lena la tabla con los valores de los objetos Game con los resultados de las sesiones
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
        regresar.setOnMouseClicked(ev ->{ // Expresión lambda que determina el comportamiento del ImageView regresar
            try {
                App.setRoot("clientes"); // Redirige a la ventana de clientes
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
    }
    
    /**
    Método que muestra los detalles de cada ejercicio de la sesión de juego seleccionada
    @throw IOException
    */
    @FXML
    private void mostrarDetalles (ActionEvent event) throws IOException{
        Game g = (Game) tableAct.getSelectionModel().getSelectedItem();
        System.out.println(g);
        if(g == null){ // Emite una alerta si no se ha seleccionado una sesión de juego de la tabla 
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al intentar consultar detalles de sesión de juego");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar una sesión de juego");
            alert.showAndWait();
        }else{
            if(!g.getFecha().equals("n/a")){// Solo si es la sesión original, se puede volver a jugar y se habilita el botón para ello
            btnReplay.setDisable(false);}
            
            paneCentral.getChildren().clear();
            paneBottom.getChildren().clear();
            
            
            int indiceGame=listaResultados.indexOf(g);
            
            System.out.println("------------------");
            System.out.println(indiceGame);
            System.out.println("------------------");
            
            //Lee el txt con el detalle de las sesines de juego 
            BufferedReader lector=new BufferedReader(new FileReader("archivos/registro/"+ClientesController.clienteSeleccionado.getCedula()+"/GamesDetalles.txt"));
            
            //Se obtiene una lista con el detalle de todas las sesiones de juego de la cual se llama a la correspondiente sesión seleccionada con su índice
            Stream<String> lineas= lector.lines();
            String[] lineasArray = lineas.toArray(size -> new String[size]);
            
            for(String a:lineasArray){
                System.out.println(a);
            }
            
            // Se separa el String en el detalle de cada ejercicio
            String detalles=lineasArray[indiceGame];
            String[] detallesTotal=detalles.split(";");
            
            for(int x=1;x<detallesTotal.length;x++){
                String[] detallesEjercicios=detallesTotal[x].split(",");
                for(String a:detallesEjercicios){
                    System.out.println(a);
            } 
           }
            
            /*
            Si se deseara mostrar el detalle del juego en general, descomentar esta sección de código
            
            
            String[] detallesGeneral=detallesTotal[0].split(",");
            
            for(String a:detallesGeneral){
                System.out.println(a);
            }
            
            VBox paneGeneral=new VBox();
            for(String a:detallesGeneral){
                Label lbl=new Label(a);
                paneGeneral.getChildren().add(lbl);
            }
            paneCentral.getChildren().add(paneGeneral);
            paneGeneral.setAlignment(Pos.CENTER);
            paneGeneral.setPadding(new Insets(20));*/
            
            // Coloca en labels el detalle de cada ejercicio de la actividad            
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
                
        regresar.setOnMouseClicked(ev ->{ // Expresión lambda que determina el comportamiento del ImageView regresar
            // Se ejecuta el método que muestra la tabla de actividades
            mostrarTabla();
        });
        
    }
    
    /**
    Método que permite rejugar una sesión de juego seleccionada
    @throw IOException
    */
    @FXML
    private void replay(ActionEvent event) throws IOException {
        Game g = (Game) tableAct.getSelectionModel().getSelectedItem();
        if(g == null){ // Si no se selecciona una sesión de juego se emite una alerta
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al intentar volver a jugar sesión");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar una sesión de juego");
            alert.showAndWait();
        }else {
                ArrayList<Game> listaActividades=Game.cargarActividades(ClientesController.clienteSeleccionado.getCedula());
                int indiceGame=listaResultados.indexOf(g); //Se obtiene el objeto Game rejugable correspondiente al objeto Game de resultados seleccionado de la tabla
                Game gSelected=listaActividades.get(indiceGame);
                replayGame=gSelected; //Asigna el objeto a la variable que se utiliza en el controlador del juego para poder ser ejecutado
                App.setRoot("game");
            
        }
    }
    
    /**
    Método que carga la lista de sesiones de juego 
    @return Devuelve una lista de objetos Game con resultados
    */
    public static ArrayList<Game> cargarLista(){
        return Game.cargarResultados(ClientesController.clienteSeleccionado.getCedula());
    }
    
}
