
package proyecto;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
/**
 * FXML Controller class
 *
 * @author Juan Pablo Plúas
 */
public class GameEndController implements Initializable {


    @FXML private BorderPane mainPane;
    @FXML private ImageView aplausos;
    @FXML private Label lblTiempoMedio;
    @FXML private Label lblTiempoTotal;
    @FXML private Label lblGracias;
    @FXML private Label lblTimeP;
    @FXML private Label lblTimeT;
    @FXML private Label lblTitulo;
    @FXML private ImageView regresar;
    
    public static boolean juegoAcabado=false;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setImage("regresar", App.pathImg, regresar);
        App.setGif("aplausos",aplausos);
        
        regresar.setOnMouseClicked(eh -> { // Expresión lambda que determina el comportamiento del ImageView regresar
            if(ActividadesController.replayGame==null){ // Cuando se esta registrando una atención, regresa a la ventana para terminar el guardado
            try {
                juegoAcabado=true; 
                App.setRoot("nuevaAtencion");   
                CitasController.citaARegistrar=null;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            }else{
              try {
                App.setRoot("clientes");// Cuando se esta rejugando una sesión, vuelve a la ventana de clientes   
                ActividadesController.replayGame=null;
            } catch (IOException ex) {
                ex.printStackTrace();
            }  
            }
        });
        
        // Escribe el tiempo promedio de cada pregunta y el tiempo total del juego en labels con el formato correspondiente (ya sea en minutos o segundos)
        int tiempoSec=GameController.timeTotal;
        if(tiempoSec<60){
            lblTiempoTotal.setText(String.valueOf(tiempoSec)+"s");
        } else{
            int min=0; int sec=0;
            min=tiempoSec/60;
            sec=tiempoSec%60;
            lblTiempoTotal.setText(String.valueOf(min)+" min "+String.valueOf(sec)+" seg");
        }
        int tiempoMedioSec=GameController.timePromedio;
        if(tiempoMedioSec<60){
            lblTiempoMedio.setText(String.valueOf(tiempoMedioSec)+"s");
        } else{
            int min=0; int sec=0;
            min=tiempoMedioSec/60;
            sec = tiempoMedioSec % 60;
            lblTiempoMedio.setText(String.valueOf(min) + " min " + String.valueOf(sec) + " seg");
        }
    }
}