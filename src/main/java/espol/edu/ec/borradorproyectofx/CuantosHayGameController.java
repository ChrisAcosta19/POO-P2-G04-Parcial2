/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package espol.edu.ec.borradorproyectofx;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class CuantosHayGameController implements Initializable {


    @FXML
    private BorderPane mainPane;
    @FXML
    private GridPane buttonsPane;
    @FXML
    private ImageView iconCitas;
    @FXML
    private ImageView iconSrv;
    @FXML
    private ImageView iconAtens;
    @FXML
    private TextField fieldRespuesta;
    @FXML
    private ImageView img11;
    @FXML
    private ImageView img12;
    @FXML
    private ImageView img13;
    @FXML
    private ImageView img21;
    @FXML
    private ImageView img22;
    @FXML
    private ImageView img23;
    @FXML
    private ImageView img24;
    @FXML
    private ImageView img14;
    @FXML
    private ImageView btnAvanzar;
    @FXML
    private ImageView btnRetroceder;
 
       /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarImagenes();
        
        btnRetroceder.setOnMouseClicked(eh -> {
            try {
                App.setRoot("cuantosHayMain");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void cargarImagenes(){
        InputStream input = null;
        Image image = null;
        try {
            input = new FileInputStream(App.pathImgGame + "arrow_right.png");
            image = new Image(input, 100, 100, false, false);
            btnAvanzar.setImage(image);
            
            input = new FileInputStream(App.pathImgGame + "arrow_left.png");
            image = new Image(input, 100, 100, false, false);
            btnRetroceder.setImage(image);
            
        } catch (IOException ex) {
            System.out.println("No se pudo cargar imagen");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    System.out.println("Error al cerrar el recurso");
                }
            }
        }
    }
}