
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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class GameController implements Initializable {

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
    @FXML
    private Button btnVerificarRespuesta;
    
    String[] images={"cow","cowg","cowh","duck","horse","horsea","horseb","pig","pigb","rooster","roosterb","sheep"};
    ImageView[] imagesLocation={img01,img02,img11,img12,img03,img13,img00,img10};
    private ArrayList <Integer> numImagenesXEjercicio=new ArrayList<>();
    private Boolean[] ToF={true,false};
    private int ejercicio=0;
    ArrayList<Ejercicio> ejercicios=new ArrayList<>();
    @FXML
    private ImageView respuestaVisualMal;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setImage("arrow_right",btnAvanzar);
        setImage("arrow_left",btnRetroceder);
        
        btnRetroceder.setOnMouseClicked(eh -> {
            if(ejercicio==0){
            try {
                App.setRoot("gameMain");
            } catch (IOException ex) {
                ex.printStackTrace();
            }} else{ejercicio--;}
        });
        //asumiendo que este es el numero ingresado en el text field del main controller
        ArrayList <Ejercicio> ejerciciosVacio= new ArrayList<>();
        System.out.println("++++++++++++++++++++++++");
        System.out.println();
        System.out.println("++++++++++++++++++++++++");
        Game g1=new Game(GameMainController.numEjercicios,ejerciciosVacio);
        numImagenesXEjercicio=g1.imagesPerQuestion();
        for(int x:numImagenesXEjercicio){
            ArrayList <String> imagenesModelo= new ArrayList <>();
            int j=(int) Math.floor(Math.random()*2); boolean bool=false;
            if (j==1){bool=true;}
            g1.imagesSelection(x,bool,imagenesModelo);
            Ejercicio lista=new Ejercicio(x,imagenesModelo,1);
            g1.ejercicios.add(lista); 
            
        }
        System.out.println("AAAAAAAAAAAAAAAAAA");
        
        for(Ejercicio ej:g1.ejercicios){
            for(String s:ej.imagenes){
                System.out.println(s);
            }
            System.out.println("--------------------");
        }
        
        
        try{
        ejercicio(g1,ejercicio);
        int intentos=0;
        btnVerificarRespuesta.setOnMouseClicked(eh -> {
            if(Integer.valueOf(fieldRespuesta.getText())==g1.ejercicios.get(ejercicio).respuesta){
            setImage("happy",respuestaVisual);
            } else{setGif("globoe",respuestaVisual,g1.ejercicios.get(ejercicio));
            }
            });
        }catch (Exception ex) {
                ex.printStackTrace();}
        
        btnAvanzar.setOnMouseClicked(eh -> {
            respuestaVisual.imageProperty().set(null);
            fieldRespuesta.clear();
            System.out.println("--------------------------");
            ejercicio++;
            try {
                ejercicio(g1,ejercicio);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (IndexOutOfBoundsException ex){
                try {
                App.setRoot("gameEnd");
            } catch (IOException exe){
                exe.printStackTrace();
            }
            }
        });
        
        btnRetroceder.setOnMouseClicked(eh -> {
            if(ejercicio==0){
            try {
                App.setRoot("gameMain");
            } catch (IOException ex) {
                ex.printStackTrace();
            }} else{
            respuestaVisual.imageProperty().set(null);
            fieldRespuesta.clear();
            System.out.println("--------------------------");
            ejercicio--;
            try {
                ejercicio(g1,ejercicio);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (IndexOutOfBoundsException ex){
                System.out.println("SE ACABO EL PROGRAMA");
            }
            }
        });
        
        
    }
    
    void ejercicio(Game g, int ejercicio)throws IOException {
        img00.imageProperty().set(null);img01.imageProperty().set(null);img02.imageProperty().set(null);
        img03.imageProperty().set(null);img10.imageProperty().set(null);img11.imageProperty().set(null);
        img12.imageProperty().set(null);img13.imageProperty().set(null);
        ArrayList<String> imgs=new ArrayList<>();
        g.imagesLocation(g.ejercicios.get(ejercicio).imagenes);
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
                    } catch (Exception ex) {
                        System.out.println("Error al cerrar el recurso");
                    }
                }
            }
    }
    
    void setGif(String name,ImageView iView,Ejercicio e){
        e.intentosAumentar();
        InputStream input = null;
           Image image = null;
            try {
                input = new FileInputStream(App.pathImgGame + name +".gif");
                image = new Image(input, 100, 100, false, false);
                iView.setImage(image);

            } catch (Exception ex) {
                System.out.println("No se pudo cargar imagen");
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception ex) {
                        System.out.println("Error al cerrar el recurso");
                    }
                }
            }
    }

    
    
    
    class Game {

    private int numEjercicios;
    private ArrayList<Ejercicio> ejercicios;
    private ArrayList<Integer> respuestasCorrectas;
    private ArrayList<Integer> respuestasObtenidas;
    private ArrayList<Integer> numIntentosxEjercicio; //intentos hasta acertar

    public Game(int numEjercicios){
        this.numEjercicios = numEjercicios;
    }

    public Game(int numEjercicios, ArrayList<Ejercicio> ejercicios) {
        this.numEjercicios = numEjercicios;
        this.ejercicios = ejercicios;
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
              a= (int)Math.floor(Math.random()*6)+1;
              imagesPerQ.add(a);}
        
        } else if (numEjercicios>10){
            for(int b=0;b<4;b++){
              a= (int)Math.floor(Math.random()*3)+2;
              imagesPerQ.add(a);}
            
            for(int b=0;b<4;b++){
              a= (int)Math.floor(Math.random()*6)+1;
              imagesPerQ.add(a);}
  
            if (numEjercicios==11){
              for(int b=0;b<3;b++){
                a= (int)Math.floor(Math.random()*7)+2;
                imagesPerQ.add(a);}
              
            } else{
              for(int b=0;b<3;b++){
                a= (int)Math.floor(Math.random()*5)+4;
                imagesPerQ.add(a);}
              
              for(int b=0;b<numEjercicios-11;b++){
                a= (int)Math.floor(Math.random()*8)+1;
                imagesPerQ.add(a);}
            }
 
        }
        return imagesPerQ;
    }
    
    
    
    void imagesSelection(int n, boolean imgDif,ArrayList<String> imagenesElegidas){
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
    }
    
    ImageView getIView(int i){
        ImageView iv=null;
        if(i==0){iv= img01;}
        if(i==1){iv= img02;}
        if(i==2){iv= img11;}
        if(i==3){iv= img12;}
        if(i==4){iv= img03;}
        if(i==5){iv= img13;}
        if(i==6){iv= img00;}
        if(i==7){iv= img10;}
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
    
    class Ejercicio{
        int respuesta;
        ArrayList<String> imagenes;
        int intentos;

        public Ejercicio(int respuesta, ArrayList<String> imagenes, int intentos) {
            this.respuesta = respuesta;
            this.imagenes = imagenes;
            this.intentos = intentos;
        }
        
        public void intentosAumentar(){
            intentos++;
        }
        

 
    }
    
}