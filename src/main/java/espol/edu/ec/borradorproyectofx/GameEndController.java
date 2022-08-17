/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package espol.edu.ec.borradorproyectofx;

import static espol.edu.ec.borradorproyectofx.GameMainController.numEjercicios;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class GameEndController implements Initializable {


    @FXML
    private BorderPane mainPane;
    @FXML
    private ImageView btnHome;
    @FXML
    private ImageView aplausos;
    @FXML
    private Label lblTiempo;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarImagenes();
        
        btnHome.setOnMouseClicked(eh -> {
            try {
                App.setRoot("primary1");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        
        
        
    }

    private void cargarImagenes(){
        InputStream input = null;
        Image image = null;
        try {
            input = new FileInputStream(App.pathImgGame + "home.png");
            image = new Image(input, 100, 100, false, false);
            btnHome.setImage(image);
            
            input = new FileInputStream(App.pathImgGame + "aplausos.gif");
            image = new Image(input, 100, 100, false, false);
            aplausos.setImage(image);
            
            
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