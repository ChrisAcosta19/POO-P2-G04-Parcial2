/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package espol.edu.ec.borradorproyectofx;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
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
    private TextField fieldRespuesta;
    @FXML
    private ImageView img00;
    @FXML
    private ImageView img01;
    @FXML
    private ImageView img02;
    @FXML
    private ImageView img03;
    @FXML
    private ImageView img10;
    @FXML
    private ImageView img11;
    @FXML
    private ImageView img12;
    @FXML
    private ImageView img13;
    @FXML
    private ImageView btnAvanzar;
    @FXML
    private ImageView btnRetroceder;
    @FXML
    private ImageView respuestaVisual;
    
    String[] images={"cow","cowg","cowh","duck","horse","horsea","horseb","pig","pigb","rooster","roosterb","sheep"};
    ImageView[] imagesLocation={img01,img02,img11,img12,img03,img13,img00,img10};
    private ArrayList <Integer> numImagenesXEjercicio=new ArrayList<>();
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setImage("arrow_right",btnAvanzar);
        setImage("arrow_left",btnRetroceder);
        
        btnRetroceder.setOnMouseClicked(eh -> {
            try {
                App.setRoot("cuantosHayMain");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        //asumiendo que este es el numero ingresado en el text field del main controller
        ArrayList <ImagenesEjercicio> imagenesGame= new ArrayList<>();
        Game g1=new Game(20);
        numImagenesXEjercicio=g1.imagesPerQuestion();
        for(int x:numImagenesXEjercicio){
            ImagenesEjercicio l1=new ImagenesEjercicio(g1.imagesSelection(x,true));
            imagenesGame.add(l1);
        }

                
        
    }

    void setImage(String name,ImageView iView){
        InputStream input = null;
           Image image = null;
            try {
                input = new FileInputStream(App.pathImgGame + name +".png");
                image = new Image(input, 100, 100, false, false);
                iView.setImage(image);

            } catch (Exception ex) {
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
    
    class Game {

    private int numEjercicios;
    private ArrayList<Integer> respuestasCorrectas;
    private ArrayList<Integer> respuestasObtenidas;
    private ArrayList<Integer> numIntentosxEjercicio; //intentos hasta acertar

    public Game(int numEjercicios){
        this.numEjercicios = numEjercicios;
    }
    
    public Game(int numEjercicios, ArrayList<Integer> numImagenesxEjercicio, ArrayList<String> imagenesxEjercicio, ArrayList<Integer> numIntentosxEjercicio) {
        this.numEjercicios = numEjercicios;
        this.numImagenesxEjercicio = numImagenesxEjercicio;
        this.imagenesxEjercicio = imagenesxEjercicio;
        this.numIntentosxEjercicio = numIntentosxEjercicio;
    }
    
    ArrayList <Integer> imagesPerQuestion(){
    ArrayList <Integer> imagesPerQ=new ArrayList<>();
        int a;
        if (numEjercicios<=5){
          for(int b=0;b<numEjercicios;b++){
            a= (int)Math.floor(Math.random()*3)+2;
            imagesPerQ.add(a);}
          
        } else if (numEjercicios<=10){
            for(int b=0;b<4;b++){
              a= (int)Math.floor(Math.random()*3)+2;
              imagesPerQ.add(a);}
            
            for(int b=0;b<numEjercicios-4;b++){
              a= (int)Math.floor(Math.random()*5)+2;
              imagesPerQ.add(a);}
        
        } else if (numEjercicios>10){
            for(int b=0;b<4;b++){
              a= (int)Math.floor(Math.random()*3)+2;
              imagesPerQ.add(a);}
            
            for(int b=0;b<4;b++){
              a= (int)Math.floor(Math.random()*5)+2;
              imagesPerQ.add(a);}
  
            if (numEjercicios==11){
              for(int b=0;b<3;b++){
                a= (int)Math.floor(Math.random()*5)+4;
                imagesPerQ.add(a);}
              
            } else{
              for(int b=0;b<3;b++){
                a= (int)Math.floor(Math.random()*5)+4;
                imagesPerQ.add(a);}
              
              for(int b=0;b<numEjercicios-11;b++){
                a= (int)Math.floor(Math.random()*8)+2;
                imagesPerQ.add(a);}
            }
 
        }
        return imagesPerQ;
    }
    
    
    
    ArrayList<String> imagesSelection(int n, boolean imgDif){
        ArrayList<String> imagenesElegidas=new ArrayList<>();
        String imagenElegida=null;
        ImageView iv=null;
        if (imgDif){
            for(int r=0;r<=(n-1);r++){
                imagenElegida=images[(int) Math.floor(Math.random()*12)];
                imagenesElegidas.add(imagenElegida);
            }   
        } else if (!imgDif){
            imagenElegida=images[(int) Math.floor(Math.random()*12)];
            for(int r=0;r<=(n-1);r++){
                imagenesElegidas.add(imagenElegida);
            }
        }
        return imagenesElegidas;
    }
    
    ImageView getIView(int i){
        ImageView iv=null;
        if(i==0){iv=img01;}
        if(i==1){iv=img02;}
        if(i==2){iv=img11;}
        if(i==3){iv=img12;}
        if(i==4){iv=img03;}
        if(i==5){iv=img13;}
        if(i==6){iv=img00;}
        if(i==7){iv=img10;}
        return iv;
    }
    
    void imagesLocation(ArrayList <String> imagenes){
        int n=imagenes.size();
            for(int r=0;r<=(n-1);r++){
                String imagenElegida= imagenes.get(r);
                ImageView iv=getIView(r);
                setImage(imagenElegida,iv);
            }

    }

    }
    
    class ImagenesEjercicio{
        ArrayList<String> imagenesxEjercicio;

        public ImagenesEjercicio(ArrayList<String> imagenesxEjercicio) {
            this.imagenesxEjercicio = imagenesxEjercicio;
        }      
        
    }
  
}